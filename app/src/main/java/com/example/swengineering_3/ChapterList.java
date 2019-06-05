package com.example.swengineering_3;

import android.provider.BaseColumns;



public class ChapterList {
    private ChapterList(){

    }
    public static class Voca implements BaseColumns {
        public static String TABLE_NAME;
        public static String ENGLISH ;
        public static String KOREAN ;
    }

    public static class Chapter implements BaseColumns{
        public static final String TABLE_NAME = "ChapterList";
        public static String CHAPTER_NAME = "chaptername";
        public static String FILE ="file";
        public String chapter;
        public String file;
    }

}
