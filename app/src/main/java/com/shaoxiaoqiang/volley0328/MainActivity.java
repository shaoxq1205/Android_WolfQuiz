package com.shaoxiaoqiang.volley0328;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtIdentity;
    private Button btnLogout;
    private Button btnChooseSet;
    private Button btnShowGrade;
    private Button btnCreatequestion;
    private Button btnShowquestion;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        txtIdentity = (TextView) findViewById(R.id.identity);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnChooseSet = (Button) findViewById(R.id.btnChooseSet);
        btnShowGrade = (Button) findViewById(R.id.btnShowGrade);
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

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        //String identity = user.get("identity");
        String identity = StaticVariable.identity;

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        String stu="Student";
        String tea="Teacher";

        if(identity!=null) {
            if (identity.equals(Character.toString('0'))) {
                txtIdentity.setText(stu);

            } else {
                txtIdentity.setText(tea);
            }
        }

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnChooseSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Fetching user details from sqlite
                HashMap<String, String> user = db.getUserDetails();

                String name = user.get("name");
                String email = user.get("email");
                //String identity = user.get("identity");
                String identity = StaticVariable.identity;
                if(identity!=null) {
                    if (identity.equals(Character.toString('0'))) {
                       ChooseSet();

                    } else {
                        ChooseSet2();
                    }
                }

            }
        });
        btnShowGrade.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowGrade();
            }
        });
    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void ShowGrade() {
        Intent intent = new Intent(MainActivity.this, ShowGradebook.class);
        HashMap<String, String> user = db.getUserDetails();
        String name = user.get("name");
        String email = user.get("email");
//        String identity = user.get("identity");//1 : Admin; 0: Student
        String identity = StaticVariable.identity;

        String status;
        if(identity.equals("1")){
            status = "Administrator";
        }else{
            status = "Student";
        }
        intent.putExtra("status",status);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    private void ChooseSet() {
        getMaxSet(1);//Stu
    }

    private void ChooseSet2() {
        getMaxSet(2);//Admin
    }

    private void getMaxSet(final int identityflag) {
        String tag_string_req = "req_choose_quiz_set";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETMAXSETNUM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.d(TAG, "ChooseQuizSet Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    StaticVariable.maxquiz = jObj.getString("maxquiz");

                    Intent intent;
                    if (identityflag==1) {//Stu
                       intent = new Intent(MainActivity.this, ChooseSet.class);
                    }
                    else{//Admin
                        intent = new Intent(MainActivity.this, ChooseSetAdmin.class);
                    }
                    startActivity(intent);
                    finish();
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
        }) ;

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