package com.shaoxiaoqiang.volley0328;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Created by mengyingwang on 4/15/16.
 */
public class FetchQuestion extends Activity {

    private static final String TAG = FetchQuestion.class.getSimpleName();
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tag_string_req = "req_FetchQuestion";

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        pDialog.setMessage("Loading questions...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SHOWQUESTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "FetchQuestion Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        StaticVariable.tempQuestion.put("qid", jObj.getString("qid"));
                        StaticVariable.tempQuestion.put("id", jObj.getString("id"));
                        JSONObject ques = jObj.getJSONObject("ques");
                        String type = ques.getString("type");
                        StaticVariable.tempQuestion.put("type", type);
                        StaticVariable.tempQuestion.put("question", ques.getString("question"));
                        StaticVariable.tempQuestion.put("ans", ques.getString("ans"));
                        switch (type) {
                            case "True or False":
                                StaticVariable.tempQuestion.put("optiona", ques.getString("optiona"));
                                StaticVariable.tempQuestion.put("optionb", ques.getString("optionb"));
                                break;
                            case "Multiple Choice":
                                StaticVariable.tempQuestion.put("optiona", ques.getString("optiona"));
                                StaticVariable.tempQuestion.put("optionb", ques.getString("optionb"));
                                StaticVariable.tempQuestion.put("optionc", ques.getString("optionc"));
                                StaticVariable.tempQuestion.put("optiond", ques.getString("optiond"));
                                break;
                            case "Fill in Blanks":
                                StaticVariable.tempQuestion.put("optiona", ques.getString("optiona"));
                                break;
                            default: break;
                        }

                        StaticVariable.tempQuestion.put("created_at", ques.getString("created_at"));
                        hideDialog();
                        if(type != null) {
                            Intent intent;
                            switch (type) {
                                case "True or False":
                                    if (StaticVariable.identity.equals("1")) {
                                        intent = new Intent(FetchQuestion.this,
                                                ShowTrueFalse.class);
                                    } else {
                                        intent = new Intent(FetchQuestion.this,
                                                ShowTrueFalse_S.class);
                                    }
                                    //intent.putExtra();
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "Multiple Choice":
                                    if (StaticVariable.identity.equals("1")) {
                                        intent = new Intent(FetchQuestion.this,
                                                ShowMultipleChoice.class);
                                    } else {
                                        intent = new Intent(FetchQuestion.this,
                                                ShowMultipleChoice_S.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                    break;
                                case "Fill in Blanks":
                                    if (StaticVariable.identity.equals("1")) {
                                        intent = new Intent(FetchQuestion.this,
                                                ShowFillinBlank.class);
                                    } else {
                                        intent = new Intent(FetchQuestion.this,
                                                ShowFillinBlank_S.class);
                                    }
                                    startActivity(intent);
                                    finish();
                                    break;
                                default: break;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "No questions in the quiz set.", Toast.LENGTH_LONG)
                                    .show();
                            //if student, stay here; if teacher, jump to create question
                        }

                        // Launch main activity

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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                String uid = getIntent().getExtras().getString("uid");

                params.put("uid", uid);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        //return tempQuestion.get("type");


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
