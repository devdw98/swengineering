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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.Result;

import static com.example.swengineering_3.MainActivity.database;

public class TestActivity extends AppCompatActivity {
    private int MILLISINFUTURE = 16*1000;
    private int COUNT_DOWN_INTERVAL = 1000;
    ArrayList<HashMap<String,String>> vocaArray;
    ArrayList<HashMap<String,String>> wrongVoca;
    private HashMap<String,String> voca;
    private Voca vo = new Voca();
    private String eng = "";
    private String kor = "";
    private int time=16;
    private int correctNum=0;
    private int testSize;
    private int test = 1;
    private boolean running = true;
    private int i=0;
    private CountDownTimer timer;
    private String chap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        vocaArray = new ArrayList<HashMap<String, String>>();
        wrongVoca = new ArrayList<HashMap<String, String>>();

        chap = getIntent().getStringExtra("chapName");
        final EditText engInput = (EditText)findViewById(R.id.englishEdit);
        final ImageView correct = findViewById(R.id.correct);
        final ImageView wrong = findViewById(R.id.wrong);

        correct.setVisibility(View.INVISIBLE);
        wrong.setVisibility(View.INVISIBLE);

        Button enterButton = (Button)findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = engInput.getText().toString();
                if(input.equals(eng)){
                    correctNum++;
                    correct.setVisibility(View.VISIBLE);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            correct.setVisibility(View.INVISIBLE);
                            engInput.setText("");
                            test++;
                        }
                    }, 500);
                }
                else{
                    wrongVoca.add(voca);
                    wrong.setVisibility(View.VISIBLE);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wrong.setVisibility(View.INVISIBLE);
                            engInput.setText("");
                            test ++;
                        }
                    }, 500);
                }
                if(i==testSize){timer.onFinish();}
            }
        });

        vocaArray = getChapter(chap);
        test(vocaArray);
        checkLoop(vocaArray);
    }

    private void test(ArrayList<HashMap<String, String>> vocaList){
        testSize = vocaList.size();
        MILLISINFUTURE = 16*1000*vocaList.size();
        testTimer();
    }
    private void checkLoop(final ArrayList<HashMap<String, String>> vocaList){
        new Thread(new Runnable() {
            TextView koreanTxt = (TextView) findViewById(R.id.koreanTxt);
            @Override
            public void run() {
                while (running) {
                    if (test%2 == 1) {
                        if(i==testSize){break;}
                        voca = vocaList.get(i);
                        kor = voca.get(vo.getKOREAN());
                        eng = voca.get(vo.getENGLISH());
                        koreanTxt.setText(kor);
                        time = 16;
                        i++;
                        test++;
                    }
                }
            }
        }).start();
    }
    private void testTimer(){
        final ImageView wrong = findViewById(R.id.wrong);
        final TextView timeTxt = findViewById(R.id.timerTxt);
        timer = new CountDownTimer(MILLISINFUTURE,COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                time--;
                if(time<0){
                    time=15;
                    wrong.setVisibility(View.VISIBLE);
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wrong.setVisibility(View.INVISIBLE);
                            test++;
                        }
                    }, 500);
                    wrongVoca.add(voca);
                }
                timeTxt.setText(String.valueOf(time));
            }
            @Override
            public void onFinish(){
                addWrongVoca(wrongVoca);
                Intent intent = new Intent(TestActivity.this, ResultActivity.class);
                intent.putExtra("correctNum",correctNum);
                intent.putExtra("testSize",testSize);
                startActivity(intent);
            }
        };timer.start();
    }
    private void addWrongVoca(final ArrayList<HashMap<String, String>> wrongVoca){
        SimpleDateFormat df = new SimpleDateFormat("yyyMMdd", Locale.KOREA);
        String str_date = df.format(new Date());

        Chapter c = new Chapter(chap+str_date);
        c.createChapter(c.TABLE_NAME);

        if(database != null){
            String sql = "insert into "+c.TABLE_NAME+"(english, korean) values(?,?)";
            Object[] params = {"",""};
            for(int i=0;i< wrongVoca.size();i++){
                HashMap<String, String> voca = wrongVoca.get(i);
                params[0] = voca.get(vo.getENGLISH());
                params[1] = voca.get(vo.getKOREAN());
                database.execSQL(sql,params);
            }
        }
    }

    private ArrayList<HashMap<String, String>> getChapter(String chap){
        ArrayList<HashMap<String, String>> vocaArray = new ArrayList<HashMap<String, String>>();
        try{
            Cursor cursor = vo.selectVoca(chap);
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
        }catch(SQLException se){
        }
        return vocaArray;
    }
}
