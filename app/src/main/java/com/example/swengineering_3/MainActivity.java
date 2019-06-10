package com.example.swengineering_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    static SQLiteDatabase database;
    static String dbname = "myvoca.db";
    String chapterName = "BasicVoca";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터베이스 열기
        openDatabase();
        insertDefaultChapter();
        Button studyButton = (Button)findViewById(R.id.studyButton);
        goStudy(studyButton);

        //Test 액티비티로 전환
        Button testButton = (Button)findViewById(R.id.testButton);
        goTest(testButton);

        //MyVoca 액티비티로 전환
        Button myvocaButton = (Button)findViewById(R.id.myvocaButton);
        goMyVoca(myvocaButton);
        //앱 종료
        Button finishButton = (Button)findViewById(R.id.finishButton);
        goFinish(finishButton);
    }

    private void goStudy(Button studyButton){
        studyButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent( MainActivity.this, SelectChapterActivity.class));
            }
        });
    }

    private void goMyVoca(Button myvocaButton){
        myvocaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyVocaActivity.class));
            }
        });
    }
    private void goTest(Button testButton){
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestMainActivity.class));
            }
        });
    }

    private void goFinish(Button finishButton){
        finishButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Chapter c = new Chapter();
                c.deleteChapter(chapterName);
                finish();
            }
        });
    }

    public void openDatabase(){
        database = openOrCreateDatabase("myvoca.db", MODE_PRIVATE, null);
        //  if(database != null)
        //    Toast.makeText(getApplicationContext(),"openDB",Toast.LENGTH_SHORT).show();
        //     DatabaseHelper databaseHelper = new DatabaseHelper(this,dbname,null,1);
        //     database = databaseHelper.getWritableDatabase();

    }
    public void insertDefaultChapter(){
        Voca v = new Voca();
        String data = null;
        StringTokenizer tokenizer = null;
        String chapterName = "BasicVoca";
        Object[] params = {"",""};
        String sql = "insert into "+chapterName+" (english, korean) values(?,?)";
        InputStream inputStream = getResources().openRawResource(getResources().getIdentifier("chap1","raw",getPackageName()));
        try { //인코딩 체크
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"MS949");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            if (database != null) {
                String sql2 = "CREATE TABLE IF NOT EXISTS " + chapterName + " (" + v._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + v.getENGLISH() + " TEXT, " + v.getKOREAN() + " TEXT)";
                database.execSQL(sql2);
                try {
                    while ((data = bufferedReader.readLine()) != null) {
                        tokenizer = new StringTokenizer(data, ":");
                        for (int j = 0; tokenizer.hasMoreTokens(); j++) {
                            params[j] = tokenizer.nextToken();
                        }
                        database.execSQL(sql, params);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
    //안씀
    /*    public class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            ChapterList.Chapter c = new ChapterList.Chapter();
            String sql =  "CREATE TABLE IF NOT EXISTS "+ c.TABLE_NAME+" ("+c._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+c.CHAPTER_NAME+" TEXT, "+c.FILE+" TEXT)";
            database.execSQL(sql);
            Toast.makeText(getApplicationContext(),"createTable",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //테이블 있는 경우
            if(newVersion > 1){
                database.execSQL("drop table if exists "+ChapterList.Chapter.TABLE_NAME);
                try{
                    String sql =  String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT) ",
                            ChapterList.Chapter.TABLE_NAME,ChapterList.Chapter._ID, ChapterList.Chapter.CHAPTER_NAME, ChapterList.Chapter.FILE);
                    database.execSQL(sql);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }*/

}
