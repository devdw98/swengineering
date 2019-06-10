package com.example.swengineering_3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static com.example.swengineering_3.MainActivity.database;

public class ResultActivity extends AppCompatActivity {
    private int testSize;
    private int correctNum;
    private String chap;
    private ArrayList<HashMap<String,String>> wrongVoca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView resultTxt = (TextView)findViewById(R.id.resultTxt);
        testSize = getIntent().getIntExtra("testSize",0);
        correctNum = getIntent().getIntExtra("correctNum",0);

        resultTxt.setText(correctNum+"/"+testSize);

        Button mainButton = (Button)findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
