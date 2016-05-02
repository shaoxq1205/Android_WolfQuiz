package com.shaoxiaoqiang.volley0328;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import helper.SQLiteHandler;

/**
 * Created by BOXUAN on 2016/4/20.
 */
public class UpdateGradebook extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SQLiteHandler db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Progress dialog

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        String setname = getIntent().getExtras().getString("setname");
        String grade = getIntent().getExtras().getString("score");

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        updategradebook(email,name,setname,grade);

    }
    private void updategradebook(final String email,final String name,final String setname,final String grade) {
        // Tag used to cancel the request
        String tag_string_req = "req_update_gradebook";

        pDialog.setMessage("Please wait ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATEGRADEBOOK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Showquestion Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        Toast.makeText(getApplicationContext(),
                                "Grades has been loaded", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateGradebook.this, MainActivity.class);
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("name", name);
                params.put("setname", setname);
                params.put("grade", grade);
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

