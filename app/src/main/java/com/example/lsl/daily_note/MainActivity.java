package com.example.lsl.daily_note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

/**
 * Created by lsl on 2020/6/7.
 */

public class MainActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    Intent intent=new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //设置注册图标大小
        //        设置工作图标大小
        final TextView editWork = (TextView) findViewById(R.id.work);
        Drawable work = getResources().getDrawable(R.drawable.work);
        work.setBounds(0, 0, 60, 60);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editWork.setCompoundDrawables(work, null, null, null);//只放左边
        // 设置点击监听
        editWork.setClickable(true);
        editWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("item1","工作");
                intent.setClass(MainActivity.this, ItemDetailActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //        设置学习图标大小
        final TextView editStudy = (TextView) findViewById(R.id.study);
        Drawable study = getResources().getDrawable(R.drawable.study);
        study.setBounds(0, 0, 60, 60);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editStudy.setCompoundDrawables(study, null, null, null);//只放左边
        //设置学习的点击事件
        editStudy.setClickable(true);
        editStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("item1","学习");
                intent.setClass(MainActivity.this, ItemDetailActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //        设置娱乐图标大小
        final TextView editPlay = (TextView) findViewById(R.id.play);
        Drawable play = getResources().getDrawable(R.drawable.play);
        play.setBounds(0, 0, 60, 60);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editPlay.setCompoundDrawables(play, null, null, null);//只放左边
        //设置娱乐的监听事件
        editPlay.setClickable(true);
        editPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("item1","娱乐");
                intent.setClass(MainActivity.this, ItemDetailActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //        设置其他图标大小
        final TextView editOther = (TextView) findViewById(R.id.other);
        Drawable other = getResources().getDrawable(R.drawable.other);
        other.setBounds(0, 0, 60, 60);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editOther.setCompoundDrawables(other, null, null, null);//只放左边
        //设置其他的点击事件
        editOther.setClickable(true);
        editOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("item1","其他");
                intent.setClass(MainActivity.this, ItemDetailActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        tv_main_title.setText("记事本分类");
    }
//    private void toast(String content){
//        Toast.makeText(getApplicationContext(),content,Toast.LENGTH_SHORT).show();
//    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.book)
                .setMessage("确定要退出应用?")
                .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.this.finish();   //关闭本活动页面
                        System.exit(0);
                    }
                })
                .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();
    }
}