package com.example.swengineering_3;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import static com.example.swengineering_3.MainActivity.database;

public class AddChapterActivity extends AppCompatActivity {
    private Chapter chapter;
    private String file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);
        final MyVocaActivity MA = (MyVocaActivity) MyVocaActivity.MyVocaActivity;

        final EditText chapterText = findViewById(R.id.chapterTextField);
        final EditText fileText = findViewById(R.id.fileTextField);

        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapter = new Chapter( chapterText.getText().toString());
                file = fileText.getText().toString();
                if(database != null){
                    chapter.createChapter(chapter.TABLE_NAME); //VocaTable생성
                    insertVoca(chapter.TABLE_NAME,file);//file.txt 에 있는 단어들 데이터베이스에 넣기
                    database.close();
                    MA.finish();
                    startActivity(new Intent (AddChapterActivity.this, MyVocaActivity.class));
                 //   finish();  //MyVoca 화면으로 이동
                }

            }
        });
    }

    private void insertVoca(String chapter, String file){ //사용자가 직접 추가할 수 있도록 구현다시하기
        String data = null;
        StringTokenizer tokenizer = null;
        Object[] params = {"",""};
        String sql = "insert into "+chapter+"(english, korean) values(?,?)";
    //    InputStream inputStream = getResources().openRawResource(getResources().getIdentifier(file,"raw",getPackageName()));
        FileInputStream inputFile = null;
        FileOutputStream outputFile = null;
        try {
            //입력한 텍스트 .txt파일로 저장
            outputFile = openFileOutput(chapter+".txt", Context.MODE_APPEND);
            PrintWriter writer = new PrintWriter(outputFile);
            writer.println(file);
            writer.close();
            //.txt파일 열어서 데이터베이스에 저장
            inputFile = openFileInput(chapter+".txt");
            InputStreamReader inputStreamReader = new InputStreamReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (database != null) {
                try{
                    while((data = bufferedReader.readLine())!=null){
                        tokenizer = new StringTokenizer(data,":");
                        for(int i=0;tokenizer.hasMoreTokens();i++){
                            params[i] = tokenizer.nextToken();
                        }
                        database.execSQL(sql,params);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
          //  database.close();
    }

    /*
    *  String data = null;
        StringTokenizer tokenizer = null;
        Object[] params = {"",""};
        String sql = "insert into "+chapter+"(english, korean) values(?,?)";
        InputStream inputStream = getResources().openRawResource(getResources().getIdentifier(file,"raw",getPackageName()));
        try { //인코딩 체크
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"MS949");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (database != null) {
                try{
                    while((data = bufferedReader.readLine())!=null){
                        tokenizer = new StringTokenizer(data,":");
                        for(int i=0;tokenizer.hasMoreTokens();i++){
                            params[i] = tokenizer.nextToken();
                        }
                        database.execSQL(sql,params);
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
          //  database.close();
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    * */


}