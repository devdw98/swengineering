package com.example.swengineering_3;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import static com.example.swengineering_3.MainActivity.database;


public class PrintVocaActivity extends AppCompatActivity {
    private Chapter chapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_voca);


        //이전 액티비티에서 chapName 받아옴
        chapter = new Chapter(getIntent().getStringExtra("chapterName"));
        list = (ListView)findViewById(R.id.vocaListView);
        showVocaList(chapter.TABLE_NAME);

        //deleteButton누르면 chapName Table 삭제
        Button deleteButton = (Button)findViewById(R.id.deleteButton);
        goDelete(deleteButton);
    }

    private void goDelete(Button deleteButton){
        final MyVocaActivity MA = (MyVocaActivity) MyVocaActivity.MyVocaActivity;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File delFile = new File(getFilesDir(),chapter.TABLE_NAME+".txt");
                if(delFile != null){
                    delFile.delete();
                }
                chapter.deleteChapter(chapter.TABLE_NAME);
                database.close();
                MA.finish();
                startActivity(new Intent(PrintVocaActivity.this, MyVocaActivity.class));
             //   finish(); //MyVoca 화면으로 이동
            }
        });
    }

    private void showVocaList(String chapterName){
        ListAdapter adapter1;
        Voca v = new Voca();
        ArrayList<HashMap<String,String>> vocaArray = new ArrayList<HashMap<String, String>>();
        final String ENG = "eng";
        final String KOR = "kor";
        try{
            Cursor cursor = v.selectVoca(chapterName);  //cursor에 챕터 테이블 안의 단어들을 모두 가져온다
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
            String[] columns = {ENG, KOR};
            /*어느 layout의 아이템에 표시해줄 건지 명시. 다른 곳에서 단어들 불러올 때 이 함수 복사해서 그 액티비티에 넣고 새로운 함수명으로 변경한담에 여기만 고치면 돼! */
            int[] to = {R.id.itemEnglish,R.id.itemKorean};  //
            adapter1 = new SimpleAdapter(this,vocaArray,R.layout.item_voca, columns,to);
            list.setAdapter(adapter1);
        }catch(SQLException se){
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}
