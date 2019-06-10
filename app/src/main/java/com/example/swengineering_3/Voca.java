package com.example.swengineering_3;

import android.database.Cursor;
import android.database.SQLException;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.swengineering_3.MainActivity.database;

public class Voca implements BaseColumns {
    private final String ENGLISH = "english";
    private final String KOREAN ="korean" ;

    public String getENGLISH() {
        return ENGLISH;
    }

    public String getKOREAN() {
        return KOREAN;
    }

    public Cursor selectVoca(String chapterName){
        Voca v = new Voca();
        String sql = "SELECT "+v.ENGLISH+", "+v.KOREAN+" FROM "+chapterName;
        Cursor cursor = database.rawQuery(sql,null);
        return cursor;
    }


/*    public void getChapter(String chapterName){
        final String ENG = "eng";
        final String KOR = "kor";
        ArrayList<HashMap<String,String>> vocaArray=new ArrayList<HashMap<String, String>>();
        try{
            Cursor cursor = selectVoca(chapterName); //cursor에 챕터 테이블 안의 단어들을 모두 가져온다
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

        }
    }*/
}

