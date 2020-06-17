package com.example.lsl.daily_note;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsl on 2020/6/8.
 */

public class ItemDetailActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private Button add;//添加按钮
    private Button delete;//删除按钮
    private ListView noteListView;
    private String item2;
    private List<NoteInfo> noteList = new ArrayList<>();
    private ListAdapter mListAdapter;
    private static NoteDataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        dbHelper = new NoteDataBaseHelper(this,"MyNote.db",null,1);

        //从main_title_bar.xml 页面布局中获取对应的UI控件
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        Intent intent=getIntent();
        item2 = intent.getStringExtra("item1");
        tv_main_title.setText(item2);

        //点击添加按钮跳转页面
        add = (Button) findViewById(R.id.btn_add);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intentEditor=new Intent(ItemDetailActivity.this,NoteEditorActivity.class);
//                startActivity(intentEditor);
//                ItemDetailActivity.this.finish();
//            }
//        });
        delete = (Button) findViewById(R.id.btn_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note.deleteAllNote(dbHelper);
                getNoteList();
                mListAdapter.refreshDataSet();
                Toast.makeText(ItemDetailActivity.this,"删除成功！",Toast.LENGTH_LONG).show();

            }
        });
        //先测试添加一条数据
      /*  ContentValues values = new ContentValues();
        values.put(Note.title,"测试笔记");
        values.put(Note.content,"以下为测试内容！！！");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        values.put(Note.time,sdf.format(date));
        Note.insertNote(dbHelper,values);*/

        initView();
        setListener();

        //跳转回主界面 刷新列表
        Intent intent1 = getIntent();
        if (intent1 != null){
            getNoteList();
            mListAdapter.refreshDataSet();
        }
    }

    //初始化视图
    private void initView(){
        noteListView = (ListView) findViewById(R.id.note_list);
        add = (Button) findViewById(R.id.btn_add);
        //获取noteList
        getNoteList();
        mListAdapter = new ListAdapter(ItemDetailActivity.this,noteList);
        noteListView.setAdapter(mListAdapter);
    }

    //设置监听器
private void setListener(){
    add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ItemDetailActivity.this,NoteEditorActivity.class);
            startActivity(intent);
            ItemDetailActivity.this.finish();
        }
    });

    noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NoteInfo noteInfo = noteList.get(position);
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("noteInfo",(Serializable)noteInfo);
            intent.putExtras(bundle);
            intent.setClass(ItemDetailActivity.this, NoteEditorActivity.class);
            startActivity(intent);
            ItemDetailActivity.this.finish();
        }
    });

    noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final NoteInfo noteInfo = noteList.get(position);
            String title = "提示";
            new AlertDialog.Builder(ItemDetailActivity.this)
                    .setIcon(R.drawable.book)
                    .setTitle(title)
                    .setMessage("确定要删除吗?")
                    .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Note.deleteNote(dbHelper,Integer.parseInt(noteInfo.getId()));
                            noteList.remove(position);
                            mListAdapter.refreshDataSet();
                            Toast.makeText(ItemDetailActivity.this,"删除成功！",Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
            return true;
        }
    });
}
    //从数据库中读取所有笔记 封装成List<NoteInfo>
    private void getNoteList(){
        noteList.clear();
        Cursor allNotes = Note.getAllNotes(dbHelper);
        for (allNotes.moveToFirst(); !allNotes.isAfterLast(); allNotes.moveToNext()){
            NoteInfo noteInfo = new NoteInfo();
            noteInfo.setId(allNotes.getString(allNotes.getColumnIndex(Note._id)));
            noteInfo.setTitle(allNotes.getString(allNotes.getColumnIndex(Note.title)));
            noteInfo.setContent(allNotes.getString(allNotes.getColumnIndex(Note.content)));
            noteInfo.setDate(allNotes.getString(allNotes.getColumnIndex(Note.time)));
            noteInfo.setDes(allNotes.getString(allNotes.getColumnIndex(Note.content)));
//            noteInfo.setPhoto(allNotes.getBlob(allNotes.getColumnIndex(Note.picture)));
            noteList.add(noteInfo);
        }
    }
    //重写返回按钮处理事件
    @Override
    public void onBackPressed() {
                        Intent intent=new Intent(ItemDetailActivity.this,MainActivity.class);
                        startActivity(intent);
                        ItemDetailActivity.this.finish();
    }

    //给其他类提供dbHelper
    public static NoteDataBaseHelper getDbHelper() {
        return dbHelper;
    }
}
