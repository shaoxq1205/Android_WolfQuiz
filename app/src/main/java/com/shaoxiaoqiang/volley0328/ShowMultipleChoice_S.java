package com.shaoxiaoqiang.volley0328;

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

import java.util.HashMap;
import java.util.Map;

import app.AppConfig;

public class ShowMultipleChoice_S extends AppCompatActivity {

    private static final String TAG = ShowMultipleChoice_S.class.getSimpleName();
    private TextView ViewQuestion;
    private Button btnOptiona;
    private Button btnOptionb;
    private Button btnOptionc;
    private Button btnOptiond;
    private Button btnConfirm;
    private Button btnPrevious;
    private Button btnNext;
    private Button btnGoBack;
    private String btext = null;
    private boolean flagConfirm = false;
    private TextView txtQuizNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_multiple_choice__s);
        txtQuizNum = (TextView) findViewById(R.id.quiznum);
        txtQuizNum.setText(StaticVariable.setnum);
        ViewQuestion = (TextView) findViewById(R.id.question);
        btnOptiona = (Button) findViewById(R.id.optiona);
        btnOptionb = (Button) findViewById(R.id.optionb);
        btnOptionc = (Button) findViewById(R.id.optionc);
        btnOptiond = (Button) findViewById(R.id.optiond);
        btnConfirm = (Button) findViewById(R.id.confirmans);
        btnPrevious = (Button) findViewById(R.id.btnPreviousQuestion);
        btnNext = (Button) findViewById(R.id.btnNextQuestion);
        btnGoBack = (Button) findViewById(R.id.btnBackToChooseSet);

        String question = "Q" + StaticVariable.quesnum + ": " + StaticVariable.tempQuestion.get("question");
        String optiona = "A. " + StaticVariable.tempQuestion.get("optiona");
        String optionb = "B. " + StaticVariable.tempQuestion.get("optionb");
        String optionc = "C. " + StaticVariable.tempQuestion.get("optionc");
        String optiond = "D. " + StaticVariable.tempQuestion.get("optiond");
        ViewQuestion.setText(question);
        btnOptiona.setText(optiona);
        btnOptionb.setText(optionb);
        btnOptionc.setText(optionc);
        btnOptiond.setText(optiond);

        btnOptiona.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!flagConfirm) {
                    btnOptiona.setBackgroundColor(getResources().getColor(R.color.input_register_hint));
                    btnOptionb.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptionc.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptiond.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btext = StaticVariable.tempQuestion.get("optiona");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You can't change your answer any more!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        btnOptionb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!flagConfirm) {
                    btnOptionb.setBackgroundColor(getResources().getColor(R.color.input_register_hint));
                    btnOptiona.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptionc.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptiond.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btext = StaticVariable.tempQuestion.get("optionb");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You can't change your answer any more!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        btnOptionc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!flagConfirm) {
                    btnOptionc.setBackgroundColor(getResources().getColor(R.color.input_register_hint));
                    btnOptionb.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptiona.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptiond.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btext = StaticVariable.tempQuestion.get("optionc");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You can't change your answer any more!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        btnOptiond.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!flagConfirm) {
                    btnOptiond.setBackgroundColor(getResources().getColor(R.color.input_register_hint));
                    btnOptionb.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptionc.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btnOptiona.setBackgroundColor(getResources().getColor(R.color.input_register_bg));
                    btext = StaticVariable.tempQuestion.get("optiond");
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You can't change your answer any more!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (btext != null) {
                    if(!flagConfirm) {
                        flagConfirm = true;
                        if (btext.equals(StaticVariable.tempQuestion.get("ans"))) {
                            StaticVariable.score += 1;
                            Toast.makeText(getApplicationContext(),
                                    "Correct! Good job!", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Wrong! The answer should be: " +
                                            StaticVariable.tempQuestion.get("ans"), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }


                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please input your answer!", Toast.LENGTH_SHORT)
                            .show();
                }

            }
        });
        /*
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
        */
        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (StaticVariable.quesnum == StaticVariable.size) {
                    Toast.makeText(getApplicationContext(),
                            "This is the last quesiton!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    StaticVariable.quesnum++;
                    Intent intent = new Intent(getApplicationContext(), FetchQuestion.class);
                    intent.putExtra("uid", StaticVariable.index.get(StaticVariable.quesnum));
                    startActivity(intent);
                    finish();
                }

            }
        });
        btnGoBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                int score = StaticVariable.score * 100 / StaticVariable.size;
                StaticVariable.index.clear();
                StaticVariable.tempQuestion.clear();
                StaticVariable.quesnum = 1;
                StaticVariable.size = 0;
                //StaticVariable.setnum = String.valueOf("Quiz1");
                Intent i = new Intent(getApplicationContext(),
                        UpdateGradebook.class);
                i.putExtra("score", Integer.toString(score));
                i.putExtra("setname", StaticVariable.setnum);
                StaticVariable.score = 0;
                startActivity(i);
                finish();
            }
        });

    }

}
