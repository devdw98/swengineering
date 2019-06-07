package com.example.swengineering_3;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.Result;

import static com.example.swengineering_3.MainActivity.database;

public class TestActivity extends AppCompatActivity {
    private int MILLISINFUTURE = 16*1000;
    private int COUNT_DOWN_INTERVAL = 1000;
    ArrayList<HashMap<String,String>> vocaArray;
    ArrayList<HashMap<String,String>> wrongVoca;
    private Timer timer;
    private final String ENG = "eng";
    private final String KOR = "kor";
    private String input = "";
    private int time=16;
    private int correctNum=0;
    private TimerTask TT;
    private int testSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        vocaArray = new ArrayList<HashMap<String, String>>();
        wrongVoca = new ArrayList<HashMap<String, String>>();


        final String chap = getIntent().getStringExtra("chapName");
        final EditText engInput = (EditText)findViewById(R.id.englishEdit);

        Button enterButton = (Button)findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input = engInput.getText().toString();
                Log.i("Input",input);
            }
        });

        getChapter(chap);
        test(vocaArray);
    }
    private void test(ArrayList<HashMap<String, String>> vocaList){
        TextView koreanTxt = (TextView) findViewById(R.id.koreanTxt);
        ImageView correct = findViewById(R.id.correct);
        ImageView wrong = findViewById(R.id.wrong);
        TextView timeTxt = findViewById(R.id.timerTxt);
        MILLISINFUTURE = 16*1000*vocaList.size();
        int i = 0;
        testSize = vocaList.size();

        testTimer();

        if(time==15) {
            HashMap<String, String> voca = vocaList.get(i);
            String kor = voca.get(KOR);
            String eng = voca.get(ENG);
            koreanTxt.setText(kor);
            if (input == eng) {
                correctNum++;
                correct.setVisibility(View.VISIBLE);
                time = 15;
            } else if (input != eng) {
                wrongVoca.add(voca);
                wrong.setVisibility(View.VISIBLE);
                time=15;
            } else if (time < 0) {
                wrongVoca.add(voca);
                wrong.setVisibility(View.VISIBLE);
                time=15;
            }
        }
        correct.setVisibility(View.INVISIBLE);
        wrong.setVisibility(View.INVISIBLE);
    }

    private void testTimer(){
        final TextView timeTxt = findViewById(R.id.timerTxt);
        CountDownTimer timer = new CountDownTimer(MILLISINFUTURE,COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                time--;
                if(time<0){
                    time=15;
                }
                timeTxt.setText(String.valueOf(time));
            }
            @Override
            public void onFinish(){
                Intent intent = new Intent(TestActivity.this, ResultActivity.class);
                intent.putExtra("correctNum",correctNum);
                intent.putExtra("testSize",testSize);
                startActivity(intent);
            }
        };timer.start();
    }
    private void getChapter(String chap){
        try{
            Cursor cursor = selectVoca(chap);
            if(cursor != null) {
                for(int i=0;i<cursor.getCount();i++) {
                    cursor.moveToNext();
                    String eng = cursor.getString(0);
                    String kor= cursor.getString(1);
                    HashMap<String, String> voca = new HashMap<String, String>();
                    voca.put(ENG, eng);
                    voca.put(KOR, kor);
                    vocaArray.add(voca);
                }
            }
        }catch(SQLException se){
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public static Cursor selectVoca(String chap){
        Voca v = new Voca();
        String sql = "SELECT "+v.ENGLISH+", "+v.KOREAN+" FROM "+chap;
        Cursor cursor = database.rawQuery(sql,null);
        return cursor;
    }
}
