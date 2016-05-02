package com.shaoxiaoqiang.volley0328;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class CreateQuiz_MC extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnSubmitquestion;
    private Button btnBacktomain;
    private EditText inputQuestion;
    private EditText inputOptionA;
    private EditText inputOptionB;
    private EditText inputOptionC;
    private EditText inputOptionD;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView txtQuizNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz__mc);

        inputQuestion = (EditText) findViewById(R.id.question);
        inputOptionA = (EditText) findViewById(R.id.optiona);
        inputOptionB = (EditText) findViewById(R.id.optionb);
        inputOptionC = (EditText) findViewById(R.id.optionc);
        inputOptionD = (EditText) findViewById(R.id.optiond);
        btnSubmitquestion = (Button) findViewById(R.id.btnSubmitquestion);
        btnBacktomain = (Button) findViewById(R.id.btnBacktomain);
        txtQuizNum = (TextView) findViewById(R.id.quiznum);
        txtQuizNum.setText(StaticVariable.setnum);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        btnSubmitquestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String question = inputQuestion.getText().toString().trim();
                String optiona = inputOptionA.getText().toString().trim();
                String optionb = inputOptionB.getText().toString().trim();
                String optionc = inputOptionC.getText().toString().trim();
                String optiond = inputOptionD.getText().toString().trim();

                if (!question.isEmpty() && !optiona.isEmpty() && !optionb.isEmpty() && !optionc.isEmpty() && !optiond.isEmpty()) {
                    registerQuiz(question, optiona, optionb, optionc, optiond);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnBacktomain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(CreateQuiz_MC.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    private void registerQuiz(final String question, final String optiona,
                              final String optionb, final String optionc, final String optiond) {
        // Tag used to cancel the request
        String tag_string_req = "req_create_question";

        pDialog.setMessage("Creating Question ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CREATEQUESTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "CreateQuestion Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // Question successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject quizquestion = jObj.getJSONObject("question");
                        String question = quizquestion.getString("question");
                        String optiona = quizquestion.getString("optiona");
                        String optionb = quizquestion.getString("optionb");
                        String optionc = quizquestion.getString("optionc");
                        String optiond = quizquestion.getString("optiond");
                        String created_at = quizquestion.getString("created_at");

                        // Inserting row in users table
                        db.addQuestion(question, optiona, optionb, optionc, optiond, uid, created_at);

                        Toast.makeText(getApplicationContext(), "Question successfully added! Check them out!", Toast.LENGTH_LONG).show();

                        // Go back to ChooseSetAdmin activity
                        Intent intent = new Intent(CreateQuiz_MC.this, MainActivity.class);
                        CreateQuiz_MC.this.startActivity(intent);
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
                params.put("setnum", StaticVariable.setnum);
                params.put("type", String.valueOf("Multiple Choice"));
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
