package com.example.swengineering_3;

import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;


public class StudyActivity extends AppCompatActivity implements View.OnClickListener {

    private Cursor cursor;
    private ArrayList<HashMap<String,String>> vocaArray;
    private Voca vo = new Voca();
    private int i = 0;
    TextView num;
    TextView vocaEnglish;
    TextView vocaKorean;
    Button nextButton;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        nextButton = (Button)findViewById(R.id.nextButton);
        total = findViewById(R.id.total);
        num = findViewById(R.id.num);
        final String chap = getIntent().getStringExtra("chapName"); //이전 화면에서 chap 받아옴
        vocaArray = getChapter(chap);
        study(vocaArray);
    }
    @Override
    public void onClick(View v){
        if(i<vocaArray.size()) {
            vocaEnglish = (TextView) findViewById(R.id.vocaEnglish);
            vocaKorean = (TextView) findViewById(R.id.vocaKorean);

            HashMap<String, String> voca = vocaArray.get(i);
            final String eng = voca.get(vo.getENGLISH());
            final String kor = voca.get(vo.getKOREAN());
            vocaEnglish.setText(eng);
            vocaKorean.setText(kor);
            num.setText(Integer.toString(i+1));
            i++;
        }
        else if(i == vocaArray.size()){
            Toast.makeText(this, "학습 완료", Toast.LENGTH_LONG).show();
            nextButton.setText("finish");
            i++;
        }
        else{
         //   database.close();
            finish();
        }
    }

    private void study(ArrayList<HashMap<String, String>> vocaArray){
        num.setText(Integer.toString(i));
        total.setText(Integer.toString(vocaArray.size()));
        nextButton.setOnClickListener(this);
    }

    private ArrayList<HashMap<String, String>> getChapter(String chap){
        ArrayList<HashMap<String, String>> vocaArray = new ArrayList<HashMap<String, String>>();
        try{
            cursor = vo.selectVoca(chap);
            if(cursor != null) {
                for(int i=0;i<cursor.getCount();i++) {
                    cursor.moveToNext();
                    String eng = cursor.getString(0);
                    String kor = cursor.getString(1);
                    HashMap<String, String> voca = new HashMap<String, String>();
                    voca.put(vo.getENGLISH(), eng);
                    voca.put(vo.getKOREAN(), kor);
                    vocaArray.add(voca);
                }
            }
        //    Toast.makeText(getApplicationContext(),chap,Toast.LENGTH_LONG).show();
        }catch(SQLException se){
        //    Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_LONG).show();
        }
        return vocaArray;
    }

}