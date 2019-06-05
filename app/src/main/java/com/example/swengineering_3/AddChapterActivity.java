package com.example.swengineering_3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import static com.example.swengineering_3.MainActivity.database;

public class AddChapterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);

        final EditText chapterText = findViewById(R.id.chapterTextField);
        final EditText fileText = findViewById(R.id.fileTextField);
        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chapter = chapterText.getText().toString();
                String file = fileText.getText().toString();
                if(database != null){
                    createVocaTable(chapter); //VocaTable생성
                    insertVoca(chapter,file); //file.txt 에 있는 단어들 데이터베이스에 넣기
                    finish();  //MyVoca 화면으로 이동
                }

            }
        });
    }

    public void createVocaTable(String tableName){
        Voca v = new Voca();
        v.TABLE_NAME = tableName;
        if(database != null){
            String sql = "CREATE TABLE IF NOT EXISTS "+ v.TABLE_NAME+" ("+v._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+v.ENGLISH+" TEXT, "+v.KOREAN+" TEXT)";
            database.execSQL(sql);
            //    Toast.makeText(getApplicationContext(),"create Voca Table",Toast.LENGTH_SHORT).show();
        }
    }
    public void insertVoca(String chapter, String file){
        String data = null;
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
                    //   Toast.makeText(getApplicationContext(),"insert voca",Toast.LENGTH_SHORT).show();
                }catch(IOException e){
                    e.printStackTrace();
                }
                //  database.close();
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
}