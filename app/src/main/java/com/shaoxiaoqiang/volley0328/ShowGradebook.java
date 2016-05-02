package com.shaoxiaoqiang.volley0328;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;
import app.AppController;

public class ShowGradebook extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private TextView ViewGrade;
   // protected static String email = "rrr5e";
  //  protected static String status = "Student";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_gradebook);
        ViewGrade = (TextView) findViewById(R.id.Grade);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        String status = getIntent().getExtras().getString("status");
        String email = getIntent().getExtras().getString("email");
        showgradebook(email,status);

    }
    private void showgradebook(final String email,final String status) {
        // Tag used to cancel the request
        String tag_string_req = "req_show_gradebook";

        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SHOWGRADEBOOK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Showquestion Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String SetsCount = jObj.getString("SetsCount");
                        String StudentsCount = jObj.getString("StudentsCount");
                        String[][] quizzes = new String[Integer.parseInt(StudentsCount)][Integer.parseInt(SetsCount)+2];
                        for(int j=0; j<Integer.parseInt(StudentsCount);j++){
                            String studentname= "Student"+(j+1);
                            JSONObject student = jObj.getJSONObject(studentname);
                            quizzes[j][0] = student.getString("name");
                            quizzes[j][1] = student.getString("email");
                            for(int i=0; i<Integer.parseInt(SetsCount);i++){
                                String quizname= "Quiz"+(i+1);
                                if(student.getString(quizname)=="null"){
                                    quizzes[j][i+2] = "Not Available Yet";
                                }
                                else {
                                    quizzes[j][i+2] = student.getString(quizname);
                                }
                            }
                        }
                        Toast.makeText(getApplicationContext(),
                                "Grades has been loaded", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ShowGradebook.this, GradeDisplay.class);
                        Bundle b=new Bundle();
                        b.putSerializable("quizzes", quizzes);
                        b.putString("email", email);
                        b.putString("status",status);
                        intent.putExtras(b);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Show Question Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {};

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
