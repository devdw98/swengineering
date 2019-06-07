package com.example.swengineering_3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    static SQLiteDatabase database;
    static String dbname = "myvoca.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //데이터베이스 열기
        openDatabase();
        //MyVoca 액티비티로 전환
        Button myvocaButton = (Button)findViewById(R.id.myvocaButton);
        myvocaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyVocaActivity.class));
            }
        });

        //Test 액티비티로 전환
        Button testButton = (Button)findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TestMainActivity.class));
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
