package com.shaoxiaoqiang.volley0328;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class GradeDisplay extends AppCompatActivity {
            private TextView ViewGrade;
            private Button btnBackToMain;
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                //String status = getIntent().getExtras().getString("status");
                //String email = getIntent().getExtras().getString("email");
                setContentView(R.layout.activity_grade_display);
        Bundle b = getIntent().getExtras();
        //String[][] quizzes = (String[][])b.getSerializable("quizzes");
                String[][] quizzes=null;
                Object[] objectArray = (Object[]) getIntent().getExtras().getSerializable("quizzes");
                if(objectArray!=null){
                    quizzes = new String[objectArray.length][];
                    for(int i=0;i<objectArray.length;i++){
                        quizzes[i]=(String[]) objectArray[i];
                    }
                }
                String status = b.getString("status");
                String email = b.getString("email");
        ViewGrade = (TextView) findViewById(R.id.GradesView);
        for(int i = 0; i < quizzes[0].length-2; i++) {// i : quiz index, j : student index
            int Sum = 0;
            int count =0;
            for(int j = 0; j < quizzes.length; j++) {
                if(!quizzes[j][i + 2].equals("Not Available Yet")){
                    Sum = Sum + Integer.parseInt(quizzes[j][i + 2]);
                    count++;
                }
            }
            int[] grades = new int[count];
            int k =0;
            for(int j = 0; j < quizzes.length; j++) {
                if(!quizzes[j][i + 2].equals("Not Available Yet")){
                    grades[k] = Integer.parseInt(quizzes[j][i + 2]);
                    k++;
                }
            }
            Arrays.sort(grades);
            ViewGrade.append("The average grade of Quiz " + (i + 1) + " is " + "\t");
            if(count!=0){
                ViewGrade.append((Sum/(count))+ "/100 \n");
            }
            else
                ViewGrade.append("\n No one has taken it yet \n");

            ViewGrade.append("The median grade of Quiz " + (i + 1) + " is " + "\t");
            if(count!=0) {
                ViewGrade.append((grades[Math.round(count / 2)]) + "/100 \n");
            }
            else
            ViewGrade.append("\n No one has taken it yet \n");
        }
        ViewGrade.append("-------------------- \n");

        if (status.equals("Student")){
            for(int j = 0; j < quizzes.length; j++){
                if(quizzes[j][1].equals(email)){
                    ViewGrade.append("Your information: NAME : " + quizzes[j][0] +"(EMAIL:"+ quizzes[j][1] + ") "+ "\nYour Grades: \n");
                    for (int i = 0; i < quizzes[0].length - 2; i++) {
                        ViewGrade.append("Quiz" + (i + 1) + ": " + "\t");
                        ViewGrade.append(quizzes[j][i + 2]);
                        ViewGrade.append("\n");
                    }
                }
            }
        }else{
            ViewGrade.append("All the students' grades are :"+ "\n\n");
            for(int j = 0; j < quizzes.length; j++){
                ViewGrade.append("NAME : " + quizzes[j][0] +"(EMAIL:"+ quizzes[j][1] + ") "+ "\n");
                for (int i = 0; i < quizzes[0].length - 2; i++) {
                    ViewGrade.append("Quiz" + (i + 1) + ": " + "\t");
                    ViewGrade.append(quizzes[j][i + 2]);
                    ViewGrade.append("\n");
                }
                ViewGrade.append("-------------------- \n");
            }
        }


        btnBackToMain=(Button) findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GradeDisplay.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
