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
import java.util.StringTokenizer;

import static com.example.swengineering_3.MainActivity.database;

public class MyVocaActivity extends AppCompatActivity {
    private Cursor cursor;
    private ArrayList<HashMap<String,String>> chapterArray;
    private final String item_chapter = "chap";
    private Chapter chapter = new Chapter();
    public static Activity MyVocaActivity;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_voca);
        MyVocaActivity = MyVocaActivity.this;

        /*리스트 구현*/
        list= (ListView)findViewById(R.id.chapterListView);
        chapterArray = new ArrayList<HashMap<String, String>>();

        showChapterList();

        /*아이템 클릭 시 작동*/
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyVocaActivity.this,PrintVocaActivity.class);
                cursor.moveToPosition(position);
                String chap = cursor.getString(0);
                intent.putExtra("chapterName",chap);
                startActivity(intent);
            }
        });
        /*단어 추가 액티비티로 이동*/
        Button addButton = (Button)findViewById(R.id.addButton);
        goAddMyVoca(addButton);
    }

    private void goAddMyVoca(Button addButton){
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyVocaActivity.this, AddChapterActivity.class));
            }
        });
    }
    private void showChapterList() {
        database = openOrCreateDatabase("myvoca.db", MODE_PRIVATE, null);
        /*다른 Activity 에서 쓰고 싶으면 chap.put(..... ,c)이랑 int[] to ={.....} .....부분 고치면 돼!*/
            try {
                cursor = chapter.selectChapter();//테이블 안 데이터 가져오기
                if (cursor != null) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        cursor.moveToNext();
                        String c = cursor.getString(0); //테이블에서 column값 가져옴
                        HashMap<String, String> chap = new HashMap<String, String>();
                        chap.put(item_chapter, c); //뷰에 연결
                        chapterArray.add(chap);
                    }
                }
                String[] columns = {item_chapter};
                /*이 함수 사용할 때도 이 바로 밑에 to 만 고치면 돼!*/
                int[] to = {R.id.itemChapter};
                ListAdapter adapter = new SimpleAdapter(this, chapterArray, R.layout.item_chapter, columns, to); //새로운 adapter 생성하여 데이터 넣기
                list.setAdapter(adapter); //화면에 보여주기 위해 listview에 연결하기

            } catch (SQLException se) {
                Toast.makeText(getApplicationContext(), se.getMessage(), Toast.LENGTH_LONG).show();
            }
        }





    //안씀
    /*    private static class ChapterAdapter extends CursorAdapter{
        public ChapterAdapter(Context context){
            super(context,null,false);
        }

        public ChapterAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_chapter,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView titleText = (TextView)view.findViewById(R.id.itemChapter);
            titleText.setText(cursor.getString(cursor.getColumnIndex(ChapterList.Chapter.CHAPTER_NAME)));
        }
    }*/

}
