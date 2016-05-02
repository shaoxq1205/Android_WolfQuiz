package com.shaoxiaoqiang.volley0328;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShowFillinBlank_S extends AppCompatActivity {

    private TextView ViewQuestion;
    private EditText etOptiona;
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
        setContentView(R.layout.activity_show_fillin_blank__s);

        ViewQuestion = (TextView) findViewById(R.id.question);
        etOptiona = (EditText) findViewById(R.id.optiona);
        btnConfirm = (Button) findViewById(R.id.confirmans);
        btnPrevious = (Button) findViewById(R.id.btnPreviousQuestion);
        btnNext = (Button) findViewById(R.id.btnNextQuestion);
        btnGoBack = (Button) findViewById(R.id.btnBackToChooseSet);
        txtQuizNum = (TextView) findViewById(R.id.quiznum);
        txtQuizNum.setText(StaticVariable.setnum);

        String question = "Q" + StaticVariable.quesnum + ": " + StaticVariable.tempQuestion.get("question");
        ViewQuestion.setText(question);


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btext = etOptiona.getText().toString().trim();
                if (btext != null) {
                    if (!flagConfirm) {
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
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "You can't change your answer any more!", Toast.LENGTH_SHORT)
                                .show();
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
