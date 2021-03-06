package com.shaoxiaoqiang.volley0328;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class EditQuiz_M extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnUpdateQuiz;
    private Button btnAbortEdit;
    private EditText inputQuestion;
    private EditText inputOptionA;
    private EditText inputOptionB;
    private EditText inputOptionC;
    private EditText inputOptionD;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz__m);

        inputQuestion = (EditText) findViewById(R.id.question);
        inputOptionA = (EditText) findViewById(R.id.optiona);
        inputOptionB = (EditText) findViewById(R.id.optionb);
        inputOptionC = (EditText) findViewById(R.id.optionc);
        inputOptionD = (EditText) findViewById(R.id.optiond);
        btnUpdateQuiz = (Button) findViewById(R.id.btnUpdateQuestion);
        btnAbortEdit = (Button) findViewById(R.id.btnAbortEdit);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        inputQuestion.setText(getIntent().getExtras().getString("question"));
        inputOptionA.setText(getIntent().getExtras().getString("optiona"));
        inputOptionB.setText(getIntent().getExtras().getString("optionb"));
        inputOptionC.setText(getIntent().getExtras().getString("optionc"));
        inputOptionD.setText(getIntent().getExtras().getString("optiond"));

        btnUpdateQuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String uid = getIntent().getExtras().getString("uid");
                String question = inputQuestion.getText().toString().trim();
                String optiona = inputOptionA.getText().toString().trim();
                String optionb = inputOptionB.getText().toString().trim();
                String optionc = inputOptionC.getText().toString().trim();
                String optiond = inputOptionD.getText().toString().trim();
                if (!question.isEmpty() && !optiona.isEmpty() && !optionb.isEmpty() && !optionc.isEmpty() && !optiond.isEmpty()&& !uid.isEmpty()) {
                    UpdateQuiz(question, optiona, optionb, optionc, optiond, uid);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnAbortEdit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(EditQuiz_M.this, ShowMultipleChoice.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void UpdateQuiz(final String question, final String optiona,
                            final String optionb, final String optionc, final String optiond, final String id) {
        // Tag used to cancel the request
        String tag_string_req = "req_create_question";

        pDialog.setMessage("Creating Question ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_EDITQUESTION_M, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "CreateQuestion Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(), "Question successfully updated!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), FetchQuestion.class);
                        intent.putExtra("uid", StaticVariable.index.get(StaticVariable.quesnum));
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
                Log.e(TAG, "CreateQuestion Error: " + error.getMessage());
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
                params.put("optiona", optiona);
                params.put("optionb", optionb);
                params.put("optionc", optionc);
                params.put("optiond", optiond);
                params.put("id", id);

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
