package com.shaoxiaoqiang.volley0328;

/**
 * Created by shaox on 2016/3/28.
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.shaoxiaoqiang.volley0328.R;
import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText confirmPassword;
    private EditText inputSerial;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmpassword);
        inputSerial = (EditText) findViewById(R.id.serial);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String serial = inputSerial.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                String confirmpw = confirmPassword.getText().toString().trim();
                String identity;

                if(confirmpw.equals(password)) {
                    if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                        //Check if this serial is the same as we provided, working as a CD-KEY.
                        if(serial.equals("ABCDEF")) {

                            identity = Character.toString('1').trim();
                            registerUser(name, email, password, identity);
                        } else if (!serial.isEmpty()) {
                            Toast.makeText(getApplicationContext(),
                                    "Wrong serial number!", Toast.LENGTH_LONG)
                                    .show();
                        } else
                        {
                            identity = Character.toString('0').trim();
                            registerUser(name, email, password, identity);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Please enter your details! Debug here!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Passwords don't match! Please try again.", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerUser(final String name, final String email,
                              final String password, final String identity) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        //Bugs here.
        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String identity = user.getString("identity");
                        String created_at = user.getString("created_at");

                        // Inserting row in users table SQLite
                        db.addUser(name, email, uid, identity, created_at);

                        //Transform String to Integer
                        int numberofidentity = 0;
                        try {
                            numberofidentity = Integer.parseInt(identity);
                        } catch(NumberFormatException e) {
                            System.out.println("Transform String to Integer Failed : " + e);
                        }

                        if (numberofidentity==0){
                            Toast.makeText(getApplicationContext(), "User successfully registered as a Student!", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "User successfully registered a Teacher!", Toast.LENGTH_LONG).show();
                        }

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("identity", identity);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}