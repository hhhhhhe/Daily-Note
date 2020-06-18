package com.example.lsl.daily_note;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by lsl on 2020/6/10.
 */

//用于管理数据库的类

public class Note implements NoteDataBaseHelper.TableCreateInterface {
    // 定义表名
    public static String tableName = "Note";
    // 定义各字段名
    public static String _id = "_id"; // _id是SQLite中自动生成的主键，用语标识唯一的记录，为了方便使用，此处定义对应字段名
//    public static String id = "id";//来判断是否修改的id号
    public static String title = "title"; // 标题
    public static String content = "content"; // 内容
    public static String time = "date"; // 时间
    public static String pre = "pre";//设置的优先级
//    public static String picture = "picture";//图片
//    public static String path = "path"; // 图片路径


    //私有化构造方法
    private Note() {
    }

    //初始化实例
    private static Note note = new Note();

    //只提供一个实例
    public static Note getInstance() {
        return note;
    }

    //实现表的创建
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "
                + Note.tableName
                + " (  "
                + "_id integer primary key autoincrement, "
//                + Note.id + "TEXT,"
                + Note.title + " TEXT, "
                + Note.content + " TEXT, "
                + Note.time + " TEXT, "
                + Note.pre + " TEXT "
//                + Note.picture + " BLOB"
                + ");";
        db.execSQL(sql);
    }

    //实现表的更新
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            String sql = "DROP TABLE IF EXISTS " + Note.tableName;
            db.execSQL(sql);
            this.onCreate(db);
        }
    }


    // 插入
    public static void insertNote(NoteDataBaseHelper dbHelper, ContentValues userValues) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insert(Note.tableName, null, userValues);
        db.close();
    }

    // 删除一条笔记
    public static void deleteNote(NoteDataBaseHelper dbHelper, int _id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Note.tableName, Note._id + "=?", new String[]{_id + ""});
        db.close();

    }

    // 删除所有笔记
    public static void deleteAllNote(NoteDataBaseHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Note.tableName, null, null);
        db.close();
    }

    // 修改
    public static void updateNote( NoteDataBaseHelper dbHelper,  int _id, ContentValues infoValues ) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(Note.tableName, infoValues, Note._id + " =? ", new String[]{ _id + "" });
        db.close();
    }

    //搜索c查询数据

    // 以HashMap<String, Object>键值对的形式获取一条信息
    public static HashMap<String, Object> getNote(NoteDataBaseHelper dbHelper, int _id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        HashMap<String, Object> NoteMap = new HashMap<String, Object>();
        // 此处要求查询Note._id为传入参数_id的对应记录，使游标指向此记录
        Cursor cursor = db.query(Note.tableName, null, Note._id + " =? ", new String[]{_id + ""}, null, null, null);
        cursor.moveToFirst();
//        NoteMap.put(Note.id,cursor.getString(cursor.getColumnIndex(Note.time)));
        NoteMap.put(Note.title, cursor.getLong(cursor.getColumnIndex(Note.title)));
        NoteMap.put(Note.content, cursor.getString(cursor.getColumnIndex(Note.content)));
        NoteMap.put(Note.time, cursor.getString(cursor.getColumnIndex(Note.time)));
        NoteMap.put(Note.pre, cursor.getString(cursor.getColumnIndex(Note.pre)));
//        NoteMap.put(Note.picture, cursor.getBlob(cursor.getColumnIndex(Note.picture)));
//        NoteMap.put(Note.path, cursor.getString(cursor.getColumnIndex(Note.path)));
        return NoteMap;
    }

    // 获得查询指向Note表的游标
    public static Cursor getAllNotes(NoteDataBaseHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Note.tableName, null, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }
    public static Cursor getSearchNotes(NoteDataBaseHelper dbHelper,String searcher) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from Note where title like '%"+searcher+"%'",null);
        cursor.moveToFirst();
        return cursor;
    }
    public static Cursor upNotes(NoteDataBaseHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Note order by cast(pre as '9999')",null);
        cursor.moveToFirst();
        return cursor;
    }
    public static Cursor downNotes(NoteDataBaseHelper dbHelper) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Note order by cast(pre as '9999') desc",null);
        cursor.moveToFirst();
        return cursor;
    }
}
