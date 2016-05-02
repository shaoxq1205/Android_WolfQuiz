package com.shaoxiaoqiang.volley0328;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class ChooseQuizSet extends AppCompatActivity {

    private static final String TAG = ChooseQuizSet.class.getSimpleName();
    private EditText etIdentity;
    private EditText etChooseSet;
    private Button btnEnter;
    private Button btnBacktomain;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;
    protected String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_quiz_set);

        etIdentity = (EditText) findViewById(R.id.identity);
        etChooseSet = (EditText) findViewById(R.id.chooseSet);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        btnBacktomain = (Button) findViewById(R.id.btnBacktomain);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        btnEnter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StaticVariable.identity = etIdentity.getText().toString().trim();
                StaticVariable.setnum = etChooseSet.getText().toString().trim();
                getId();

            }
        });

        btnBacktomain.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void getId() {
        String tag_string_req = "req_choose_quiz_set";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETFIRSTID, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "ChooseQuizSet Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONArray uid = jObj.getJSONArray("uid");
                        //JSONObject uid = jObj.getJSONObject("uid");
                        StaticVariable.size = jObj.getInt("size");
                        for (int i = 0; i < StaticVariable.size; i++) {

                            StaticVariable.index.put(i+1, uid.getString(i));
                        }
                        //id = jObj.getString("id");

                        Intent intent = new Intent(getApplicationContext(), FetchQuestion.class);
                        intent.putExtra("uid", StaticVariable.index.get(1));
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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("setnum", StaticVariable.setnum);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ChooseQuizSet.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
