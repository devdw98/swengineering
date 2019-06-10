package com.example.swengineering_3;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectChapterActivity extends AppCompatActivity {

    private ListAdapter adapter;
    private Cursor cursor;
    private ArrayList<HashMap<String,String>> chapterArray;
    private final String item_chapter = "chap";
    private Chapter chapter = new Chapter();
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chapter);

        /*리스트 구현*/
        list= (ListView)findViewById(R.id.chapterListView);
        chapterArray = new ArrayList<HashMap<String, String>>();
        showChapterList();
        /*아이템 클릭 시 작동*/
        goStudy();
    }
    private void goStudy(){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectChapterActivity.this,StudyActivity.class);
                cursor.moveToPosition(position);
                String chap = cursor.getString(0);
                intent.putExtra("chapName",chap);
                startActivity(intent);
            }
        });
    }
    private void showChapterList(){
        try{
            cursor = chapter.selectChapter();                           //테이블 안 데이터 가져오기
            if(cursor != null) {
                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    String c = cursor.getString(0); //테이블에서 column값 가져옴
                    HashMap<String, String> chap = new HashMap<String, String>();
                    chap.put(item_chapter, c); //뷰에 연결
                    chapterArray.add(chap);
                }
            }
            String[] columns = {item_chapter};
            int[] to = {R.id.itemChapter};
            adapter = new SimpleAdapter(this,chapterArray,R.layout.item_chapter, columns,to); //새로운 adapter 생성하여 데이터 넣기
            list.setAdapter(adapter); //화면에 보여주기 위해 listview에 연결하기
        }catch(SQLException se){
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}