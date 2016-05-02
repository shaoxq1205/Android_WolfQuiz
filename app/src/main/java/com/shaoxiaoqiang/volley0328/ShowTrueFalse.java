package com.shaoxiaoqiang.volley0328;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import helper.SQLiteHandler;
import helper.SessionManager;

public class ShowTrueFalse extends AppCompatActivity {

    private TextView ViewQuestion;
    private TextView ViewOptiona;
    private TextView ViewOptionb;
    private Button btnPrevious;
    private Button btnNext;
    private Button btnEdit;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnGoBack;
    private SQLiteHandler db;
    private ProgressDialog pDialog;
    private SessionManager session;
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private String question0;
    private String optiona0;
    private String optionb0;
    private String uid;
    private TextView txtQuizNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_true_false);

        ViewQuestion = (TextView) findViewById(R.id.question);
        ViewOptiona = (TextView) findViewById(R.id.optiona);
        ViewOptionb = (TextView) findViewById(R.id.optionb);
        btnPrevious = (Button) findViewById(R.id.btnPreviousQuestion);
        btnNext = (Button) findViewById(R.id.btnNextQuestion);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnGoBack = (Button) findViewById(R.id.btnBackToChooseSet);
        txtQuizNum = (TextView) findViewById(R.id.quiznum);
        txtQuizNum.setText(StaticVariable.setnum);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        uid =  StaticVariable.tempQuestion.get("qid");
        question0 = StaticVariable.tempQuestion.get("question");
        optiona0 =  "True";
        optionb0 =  "False";

        String question = "Q" + StaticVariable.quesnum + ": " + question0;
        String optiona = optiona0;
        String optionb = optionb0;

        ViewQuestion.setText(question);
        ViewOptiona.setText(optiona);
        ViewOptionb.setText(optionb);

        btnPrevious.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (StaticVariable.quesnum == 1) {
                    Toast.makeText(getApplicationContext(),
                            "This is the first quesiton!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    StaticVariable.quesnum--;
                    Intent intent = new Intent(getApplicationContext(), FetchQuestion.class);
                    intent.putExtra("uid", StaticVariable.index.get(StaticVariable.quesnum));
                    startActivity(intent);
                    finish();
                }

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (StaticVariable.quesnum == StaticVariable.size) {
                    Toast.makeText(getApplicationContext(),
                            "This is the last quesiton!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    StaticVariable.quesnum++;
                    String uid = StaticVariable.index.get(StaticVariable.quesnum);
                    Intent intent = new Intent(getApplicationContext(), FetchQuestion.class);
                    intent.putExtra("uid", uid);
                    startActivity(intent);
                    finish();
                }

            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                StaticVariable.index.clear();
                StaticVariable.tempQuestion.clear();
                StaticVariable.quesnum = 1;
                StaticVariable.size = 0;
                StaticVariable.setnum = String.valueOf("Quiz1");
                Intent i = new Intent(getApplicationContext(),
                        ChooseSetAdmin.class);
                startActivity(i);
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        EditQuiz_T.class);
                i.putExtra("question",question0);
                i.putExtra("optiona",optiona0);
                i.putExtra("optionb",optionb0);
                i.putExtra("uid",uid);
                startActivity(i);
                finish();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SelectNewQuesType.class);
                startActivity(i);
                finish();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (!question0.isEmpty()) {
                    deleteQuiz(question0);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Try again, Trust me once more", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    private void deleteQuiz(final String question) {
        // Tag used to cancel the request
        String tag_string_req = "req_delete_question";

        pDialog.setMessage("Deleting Question ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETEQUESTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "DeleteQuestion Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Question successfully deleted!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ShowTrueFalse.this, MainActivity.class);
                        ShowTrueFalse.this.startActivity(intent);
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
                Log.e(TAG, "DeleteQuestion Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("question", question);
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
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(ShowTrueFalse.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
