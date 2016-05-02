package com.shaoxiaoqiang.volley0328;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SelectNewQuesType extends AppCompatActivity {

    Spinner sp;
    Button btnBacktomain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_new_ques_type);

        sp = (Spinner) findViewById(R.id.spinner3);
        btnBacktomain = (Button) findViewById(R.id.btnBacktomain);
        // Creating adapter for spinner
        List<String> spinneritem1 = new ArrayList<>();
        spinneritem1.add("Select One");
        spinneritem1.add("Multiple Choice");
        spinneritem1.add("Fill in Blanks");
        spinneritem1.add("True or False");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinneritem1);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                  if (sp.getSelectedItem().toString().equals("Select One")) {
                    }
                  else {
                      Intent intent;
                      switch (sp.getSelectedItem().toString()) {
                          case "Multiple Choice":
                              intent = new Intent(SelectNewQuesType.this,
                                      CreateQuiz_MC.class);
                              startActivity(intent);
                              break;
                          case "Fill in Blanks":
                              intent= new Intent(SelectNewQuesType.this,
                                  CreateQuiz_FB.class);
                              startActivity(intent);
                              break;
                          case "True or False":
                              intent = new Intent(SelectNewQuesType.this,
                                      CreateQuiz_TF.class);
                              startActivity(intent);
                              break;
                      }
                  }
                }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btnBacktomain.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(SelectNewQuesType.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
