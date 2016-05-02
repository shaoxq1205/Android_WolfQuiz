package com.shaoxiaoqiang.volley0328;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class ChooseSetAdmin extends AppCompatActivity {
    private Button btnBacktomain;
    private static final String TAG = ChooseQuizSet.class.getSimpleName();
    private SQLiteHandler db;

    Spinner sp1;
    Spinner sp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_set_admin);
        btnBacktomain = (Button) findViewById(R.id.btnBacktomain);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String identity = user.get("identity");
        //StaticVariable.identity = identity;


        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2 = (Spinner) findViewById(R.id.spinner2);

        // Creating adapter for spinner
        String maxquiz = StaticVariable.maxquiz;
        int maxquiznum = Integer.parseInt(maxquiz);

        List<String> spinneritem1 = new ArrayList<>();
        List<String> spinneritem2 = new ArrayList<>();

        spinneritem1.add("Selete a quiz");
        for (int i=0;i<maxquiznum;i++){
            String tempstring = "Quiz"+Integer.toString(i+1);
            spinneritem1.add(tempstring);
        }

        spinneritem2.add("Add A New Set of Questions");
        spinneritem2.add("Multiple Choice");
        spinneritem2.add("True or False");
        spinneritem2.add("Fill in Blanks");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinneritem1);

        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinneritem2);

        // Drop down layout style - list view with radio button
       dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
       sp1.setAdapter(dataAdapter1);
        sp2.setAdapter(dataAdapter2);

        sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (sp1.getSelectedItem().toString().equals("Selete a quiz")) {
                } else {
                    //Toast.makeText(getApplicationContext(),
                     //       "Loading Quizzes...", Toast.LENGTH_LONG)
                     //       .show();
                    StaticVariable.setnum = sp1.getSelectedItem().toString();
                    getId();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        sp2.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                if (sp2.getSelectedItem().toString().equals("Add A New Set of Questions")) {
                }
                else {

                    //To add a new set of questions, we need to set the setnum varaible maxquiznum+1, and change it back when done
                    String maxquiz = StaticVariable.maxquiz;
                    int maxquiznum = Integer.parseInt(maxquiz);
                    StaticVariable.setnum = "Quiz"+Integer.toString(maxquiznum+1);

                    StaticVariable.questiontype = sp2.getSelectedItem().toString();
                    if(StaticVariable.questiontype.equals("Multiple Choice")){
                        Intent intent = new Intent(ChooseSetAdmin.this,
                                CreateQuiz_MC.class);
                        startActivity(intent);
                    }
                    else if(StaticVariable.questiontype.equals("True or False")){
                        Intent intent = new Intent(ChooseSetAdmin.this,
                                CreateQuiz_TF.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(ChooseSetAdmin.this,
                                CreateQuiz_FB.class);
                        startActivity(intent);
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        //Go back to Main button
        btnBacktomain.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(ChooseSetAdmin.this, MainActivity.class);
                startActivity(intent);
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
}
