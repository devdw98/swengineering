package com.example.swengineering_3;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.swengineering_3.MainActivity.database;

/*시험 볼 챕터를 선택하는 화면*/
public class TestMainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_INSERT = 1000;
    ListAdapter adapter;
    Cursor cursor;
    ListView list;
    ArrayList<HashMap<String,String>> chapterArray;
    private final String item_chapter = "chap";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        list= (ListView)findViewById(R.id.testChapListView);
        chapterArray = new ArrayList<HashMap<String, String>>();
        showChapterList();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TestMainActivity.this,TestActivity.class);
                cursor.moveToPosition(position);
                String chap = cursor.getString(0);
                intent.putExtra("chapName",chap);
                startActivityForResult(intent,REQUEST_CODE_INSERT);
            }
        });
    }
    private void showChapterList(){
        try{
            cursor = selectChapter();
            if(cursor != null) {
                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToNext();
                    String c = cursor.getString(0);
                    HashMap<String, String> chap = new HashMap<String, String>();
                    chap.put(item_chapter, c);
                    chapterArray.add(chap);
                }
            }
            String[] columns = {item_chapter};
            int[] to = {R.id.itemChapter};
            adapter = new SimpleAdapter(this,chapterArray,R.layout.item_chapter, columns,to);
            list.setAdapter(adapter);
        }catch(SQLException se){
            Toast.makeText(getApplicationContext(),se.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    public Cursor selectChapter(){
        String sql = "SELECT name FROM sqlite_master WHERE type = 'table' and name != 'android_metadata' and name != 'sqlite_sequence'";
        Cursor cursor = database.rawQuery(sql,null);
        return cursor;
    }
}
