package com.example.swengineering_3;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Environment;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.StringTokenizer;

import static com.example.swengineering_3.MainActivity.database;

public class Chapter extends Voca{
    public String TABLE_NAME;

    public Chapter(){

    }

    public Chapter(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public void  createChapter(String chapterName){
        Voca v = new Voca();
        TABLE_NAME = chapterName;
        if(database != null){
            String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME+" ("+v._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+v.getENGLISH()+" TEXT, "+v.getKOREAN()+" TEXT)";
            database.execSQL(sql);
            //    Toast.makeText(getApplicationContext(),"create Voca Table",Toast.LENGTH_SHORT).show();
        }
    }
    public void insertChapter(String chapter, String file){ //다시 구현하기!!!
        String data = null;
        StringTokenizer tokenizer = null;
        Object[] params = {"",""};
        String sql = "insert into "+chapter+"(english, korean) values(?,?)";

     /*   try { //인코딩 체크
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
        }*/
    }
    public void deleteChapter(String chapterName){
        String sql = "DROP TABLE IF EXISTS "+chapterName;
        database.execSQL(sql);
        database.close();
    }

    public Cursor selectChapter(){
        String sql = "SELECT name FROM sqlite_master WHERE type = 'table' and name != 'android_metadata' and name != 'sqlite_sequence'";
        Cursor cursor = database.rawQuery(sql,null);
        return cursor;
    }

}
