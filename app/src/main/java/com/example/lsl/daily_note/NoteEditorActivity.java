package com.example.lsl.daily_note;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.ContentValues;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Button;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.lsl.daily_note.Note.updateNote;


/**
 * Created by lsl on 2020/6/8.
 */

public class NoteEditorActivity extends AppCompatActivity {
    private TextView tv_main_title;//标题
    private TextView tv_now;//现在的时间
    private EditText et_title;//标题
    private EditText et_content;//内容
    private Button btn_save;//保存
    private Button btn_return;//取消
    private ImageView pic_button;//插入图片按钮
//    public Drawable photodrawable;//插入的图片(有默认的图片)
    private ImageView testpic;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
//    public Bitmap photobitmap ;//图片的Bitmap格式
//    public byte[] photobyte;//图片转为byte类型存入数据库（有默认图片）
    private NoteInfo currentNote;
//    public byte[] showbyte;
//    public Bitmap showbitmap;
    //记录是否是插入状态 （因为也可能是更新（编辑）状态）
    private boolean insertFlag = true;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        photodrawable = pic_button.getDrawable();
//        //把drawable格式转为byte
//        photobyte = dratobyte(photodrawable);
//        photobitmap = BitmapFactory.decodeByteArray(photobyte, 0, photobyte.length);
        setContentView(R.layout.note_editor);
        //设置此界面为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //设置添加事项页面图标大小
        // 设置标题图标大小
        TextView editTitle = (TextView) findViewById(R.id.et_title);
        Drawable title1 = getResources().getDrawable(R.drawable.title);
        title1.setBounds(0, 0, 50, 50);//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        editTitle.setCompoundDrawables(title1, null, null, null);//只放左边
        tv_main_title = (TextView) findViewById(R.id.tv_main_title);
        initView();
        setListener();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        //主界面点击ListView中的一个Item跳转时
        if (bundle != null) {
            currentNote = (NoteInfo) bundle.getSerializable("noteInfo");
            et_title.setText(currentNote.getTitle());
            tv_now.setText(currentNote.getDate());
//            showbyte = currentNote.getPhoto();
//            //把byte格式的图片转为bitmap格式的图片
//            showbitmap = BitmapFactory.decodeByteArray(showbyte, 0, showbyte.length);
//            pic_button.setImageBitmap(showbitmap);
            et_content.setText(currentNote.getContent());
//            //获取drawable格式的图片
//            Drawable drawable1 = pic_button.getDrawable();
//            //把drawable格式转为byte
//            byte[] bytepic = dratobyte(drawable1);
//            //把byte格式的图片转为bitmap格式的图片
//            Bitmap bmpout = BitmapFactory.decodeByteArray(bytepic, 0, bytepic.length);
//            //用Bitmap格式的图片设置tespic的图片
//            testpic.setImageBitmap(bmpout);
//            byte[] in = currentNote.getPhoto();
//            Bitmap pic = BitmapFactory.decodeByteArray(in,0,in.length);
//            pic_button.setBackgroundResource(0);
//            pic_button.setImageBitmap(pic);
//            testpic.setImageBitmap(pic);
//            byte[] pic = currentNote.getPhoto();
//            et_content.setText(pic.toString());
//            Bitmap bitmap = BitmapFactory.decodeByteArray(pic,0,pic.length);

//            while(m.find()){
//                //取出路径前的文字
//                if(m.start() > 0){
//                    et_content.append(context.substring(startIndex, m.start()));
//                }
//                SpannableString ss = new SpannableString(m.group().toString());
//                //取出路径
//                String path = m.group().toString();
////                et_content.setText(path.toString());
//                //取出路径的后缀
//                String type = path.substring(path.length() - 3, path.length());
//                Bitmap bm = null;
//                Bitmap rbm = null;
//                    //取出图片
//                    bm = BitmapFactory.decodeFile(m.group());
//                    //缩放图片
//                    rbm = resize(bm,480);
//                //为图片添加边框效果
//                rbm = getBitmapHuaSeBianKuang(rbm);
//                System.out.println(rbm.getWidth()+"-------"+rbm.getHeight());
//                ImageSpan span = new ImageSpan(this, rbm);
//                ss.setSpan(span,0, m.end() - m.start(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                et_content.append(ss);
//                startIndex = m.end();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        //解决android7调用照相机后直接闪退问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

//    private void saveNote() {
//        NoteDataBaseHelper dbHelper = ItemDetailActivity.getDbHelper();
//        ContentValues values = new ContentValues();
//        values.put(Note.title, et_title.getText().toString());
//        values.put(Note.content, et_content.getText().toString());
//        values.put(Note.time, tv_now.getText().toString());
//        //获取drawable格式的图片
////        photodrawable = pic_button.getDrawable();
//        //把drawable格式转为byte
////        photobyte = dratobyte(photodrawable);
////        values.put(Note.picture,photobyte);
////        //把byte格式的图片转为bitmap格式的图片
////        photobitmap = BitmapFactory.decodeByteArray(photobyte, 0, photobyte.length);
////
////        values.put(Note.picture,photobyte);
////            values.put(Note.picture,photobyte);
////        values.put(Note.path,imageUri.toString());
//        if (insertFlag) {
//            Note.insertNote(dbHelper, values);
//        } else {
//            Note.updateNote(dbHelper, Integer.parseInt(currentNote.getId()), values);
//        }
//    }

    //保存数据
    private void saveNote() {
    NoteDataBaseHelper dbHelper = ItemDetailActivity.getDbHelper();

    ContentValues values = new ContentValues();
    values.put(Note.title, et_title.getText().toString());
    values.put(Note.content, et_content.getText().toString());
    values.put(Note.time, getTime().toString());

//        photodrawable = pic_button.getDrawable();
//        //把drawable格式转为byte
//        photobitmap = BitmapFactory.decodeByteArray(photobyte, 0, photobyte.length);
//        testpic.setImageDrawable(photodrawable);
//        values.put(Note.picture,dratobyte(pic_button.getDrawable()));
//    values.put(Note.id,tv_now.getText().toString());
    if (insertFlag) {
        Note.insertNote(dbHelper, values);
    } else {
        updateNote(dbHelper, Integer.parseInt(currentNote.getId()), values);
    }
}

    //初始化界面
    private void initView() {
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_return = (Button) findViewById(R.id.btn_return);
        tv_now = (TextView) findViewById(R.id.tv_now);
        et_content = (EditText) findViewById(R.id.et_content);
        et_title = (EditText) findViewById(R.id.et_title);
        pic_button = (ImageView) findViewById(R.id.pic);
        testpic = (ImageView) findViewById(R.id.testpic);
//        photodrawable = pic_button.getDrawable();
        tv_now.setText(getTime());
//        //把drawable格式转为byte
//        photobyte = dratobyte(photodrawable);
//        photobitmap = BitmapFactory.decodeByteArray(photobyte, 0, photobyte.length);
//        testpic.setImageDrawable(photodrawable);
//        //用Bitmap格式的图片设置tespic的图片
//       testpic = (ImageView) findViewById(R.id.testpic)

    }

    //时间的显示
    private String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        return str;
    }

    //设置监听器
    private void setListener() {
        pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NoteEditorActivity.this)
                        .setIcon(R.drawable.picture)
                        .setMessage("插入图片")
                        .setPositiveButton(R.string.photograph, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                takePhoto();
                            }
                        }).setNegativeButton(R.string.photo_album, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chooseFromAlbum();//  用户授权了权限申请之后就会调用该方法
                    }
                }).create().show();
            }
        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                NoteEditorActivity.this.finish();
                onBackPressed();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_title.getText().toString().equals("") ||
                        et_content.getText().toString().equals("")) {
                    Toast.makeText(NoteEditorActivity.this, R.string.save_fail, Toast.LENGTH_LONG).show();
                }
                if (currentNote.getDate().toString()==tv_now.getText().toString()){
                    insertFlag = false;
                    currentNote.setTitle(et_title.getText().toString());
                    currentNote.setContent(et_content.getText().toString());
                    currentNote.setDate(getTime().toString());
                    saveNote();
                }
//                if(et_title.getText().toString().equals(currentNote.getTitle()) &&
//                        et_content.getText().toString().equals(currentNote.getContent())&&currentNote.getDate().equals(tv_now.getText().toString())){
//                    Intent intent = new Intent(NoteEditorActivity.this, ItemDetailActivity.class);
//                    startActivity(intent);
//                    NoteEditorActivity.this.finish();
//                if(currentNote.getDate().equals(tv_now.getText().toString())){
//                    updateNote( NoteDataBaseHelper dbHelper,  int _id, ContentValues infoValues )
//                    currentNote.setTitle(et_title.getText().toString());
//                    currentNote.setContent(et_content.getText().toString());
//                    currentNote.setDate(getTime().toString());
//                    Intent intent = new Intent(NoteEditorActivity.this, ItemDetailActivity.class);
//                    startActivity(intent);
//                    NoteEditorActivity.this.finish();
//                }
                else {
//                    insertFlag = true;
//                   saveNote();
                    onBackPressed();
                    Intent intent = new Intent(NoteEditorActivity.this, ItemDetailActivity.class);
                    startActivity(intent);
                    NoteEditorActivity.this.finish();
                    Toast.makeText(NoteEditorActivity.this, R.string.save_succ, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //保存笔记到数据库 判断是新建还是更新
    //将drawable转换成可以用来存储的byte[]类型
//    private byte[] getPicture(Drawable drawable) {
//        if(drawable == null) {
//            return null;
//        }
//        BitmapDrawable bd = (BitmapDrawable) drawable;
//        Bitmap bitmap = bd.getBitmap();
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
//        return os.toByteArray();
//    }

    //重写手机上返回按键处理函数，如果更改了提示保存 否则直接返回主界面
//    @Override
//    public void onBackPressed() {
//        boolean display = false;
//        if (insertFlag) {
//            if (!et_title.getText().toString().equals("") &&
//                    !et_content.getText().toString().equals("")) {
//                display = true;
//            }
//        }
//        else {
//            if (!et_title.getText().toString().equals(currentNote.getTitle()) ||
//                    !et_content.getText().toString().equals(currentNote.getContent()) ) {
//                display = true;
//            }
//        }
//        if (display) {
//            String title = "提示";
//            new AlertDialog.Builder(NoteEditorActivity.this)
//                    .setIcon(R.drawable.book)
//                    .setTitle(title)
//                    .setMessage("是否保存当前内容?")
//                    .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            saveNote();
//                            Toast.makeText(NoteEditorActivity.this, R.string.save_succ, Toast.LENGTH_LONG).show();
//                            //更新当前Note对象的值 防止选择保存后按返回仍显示此警告对话框
//                            currentNote.setTitle(et_title.getText().toString());
//                            currentNote.setContent(et_content.getText().toString());
////                            currentNote.setPhoto(photobyte);
//                            //获取drawable格式的图片
////                            photodrawable =  pic_button.getDrawingCache();
//                            //把drawable格式转为byte
////                            photobyte= dratobyte(photodrawable);
//                            //把byte格式的图片转为bitmap格式的图片
////                            photobitmap = pic_button.getDrawingCache();
//////                            Bitmap bmpout = BitmapFactory.decodeByteArray(photobyte, 0, photobyte.length);
////                            //用Bitmap格式的图片设置tespic的图片
//////            ImageView testpic = (ImageView) findViewById(R.id.testpic);
////                            testpic.setImageBitmap(photobitmap);
////                            values.put(Note.picture,dratobyte(pic_button.getDrawable()));
////                            currentNote.setPhoto(dratobyte(pic_button.getDrawable()));
//                        }
//                    })
//                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
////                            NoteEditorActivity.this.finish();
//                            Intent intent1 = new Intent(NoteEditorActivity.this, ItemDetailActivity.class);
//                            startActivity(intent1);
//                        }
//                    }).create().show();
//        } else {
//            Intent intent = new Intent(NoteEditorActivity.this, ItemDetailActivity.class);
//            startActivity(intent);
////            NoteEditorActivity.this.finish();
//        }
//    }
//重写手机上返回按键处理函数，如果更改了提示保存 否则直接返回主界面

    @Override
    public void onBackPressed() {
        boolean display = false;
        if (insertFlag){
            if( !et_title.getText().toString().equals("") &&
                    !et_content.getText().toString().equals("")){
                display = true;
            }
        } else{//更新状态
            if(!et_title.getText().toString().equals(currentNote.getTitle()) ||
                    !et_content.getText().toString().equals(currentNote.getContent())){
//                    saveNote();
                    currentNote.setTitle(et_title.getText().toString());
                    currentNote.setContent(et_content.getText().toString());
                    display = true;
            }
        }
        if (et_title.getText().toString().equals(currentNote.getTitle()) &&
                et_content.getText().toString().equals(currentNote.getContent())){
            Intent intent = new Intent(NoteEditorActivity.this, ItemDetailActivity.class);
            startActivity(intent);
            NoteEditorActivity.this.finish();
        }
        else if (display){
            String title = "警告";
            new AlertDialog.Builder(NoteEditorActivity.this)
                    .setIcon(R.drawable.book)
                    .setTitle(title)
                    .setMessage("是否保存当前内容?")
                    .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveNote();
                            Toast.makeText(NoteEditorActivity.this,R.string.save_succ,Toast.LENGTH_LONG).show();
                            //更新当前Note对象的值 防止选择保存后按返回仍显示此警告对话框
                            currentNote.setTitle(et_title.getText().toString());
                            currentNote.setContent(et_content.getText().toString());
                            currentNote.setDate(getTime().toString());
                        }
                    })
                    .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(NoteEditorActivity.this,ItemDetailActivity.class);
                            startActivity(intent);
                            NoteEditorActivity.this.finish();
                        }
                    }).create().show();
        }else{
            Intent intent = new Intent(NoteEditorActivity.this,ItemDetailActivity.class);
            startActivity(intent);
            NoteEditorActivity.this.finish();
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NoteEditor Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void takePhoto() {
        //调用系统拍照界面
//        try
//        {
//            if(outputImage.exists()) //  检查与File对象相连接的文件和目录是否存在于磁盘中
//            {
//                outputImage.delete();//  删除与File对象相连接的文件和目录
//            }
//            outputImage.createNewFile();//  如果与File对象相连接的文件不存在，则创建一个空文件
//            Log.d("haha", "创建图片存储目录");
//        } catch (IOException e)
//        {
//            Log.d("haha", "创建目录抛出异常");
//            e.printStackTrace();
//        }
//        imageUri = Uri.fromFile(outputImage);//将文件路径转化为Uri对象,这个Uri对象表示着output_image.jpg 这张图片的本地真实路径
//        et_content.setText(imageUri.toString());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //区分选择相片
        startActivityForResult(intent, 2);
    }
//    public void takePhoto() {
//        String photoName = getTime() + "_image.jpg"; //          获取本地时间，作为图片的名字，防止拍了多张照片时，出现图片覆盖导致之前图片消失的问题
//        Log.d("haha", "photoName is " + photoName);
//        /**
//         *          创建File对象，用于存储拍照后的照片
//         *          第一个参数：  是这张照片存放在手机SD卡的对应关联缓存应用
//         *          第二个参数：  这张图片的命名
//         */
//        File outputImage = new File(Environment.getExternalStorageDirectory()+"/ASimpleCount/", photoName);
//        try
//        {
//            if(outputImage.exists()) //  检查与File对象相连接的文件和目录是否存在于磁盘中
//            {
//                outputImage.delete();//  删除与File对象相连接的文件和目录
//            }
//            outputImage.createNewFile();//  如果与File对象相连接的文件不存在，则创建一个空文件
//            Log.d("haha", "创建图片存储目录");
//        } catch (IOException e)
//        {
//            Log.d("haha", "创建目录抛出异常");
//            e.printStackTrace();
//        }
//        imageUri = Uri.fromFile(outputImage);//将文件路径转化为Uri对象,这个Uri对象表示着output_image.jpg 这张图片的本地真实路径
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //  将Intent的action指定为 拍照到指定目录 —— android.media.action.IMAGE_CAPTURE
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//  指定图片的输出地址
//        intent.putExtra("name", photoName);
//        //  在通过startActivityForResult()，来启动活动，因此拍完照后会有结果返回到 onActivityResult()方法中
//        startActivityForResult(intent,TAKE_PHOTO);//  打开相机，用自定义常量 —— TAKE_PHOTO来作为case处理图片的标识
////        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
////        pic_button.setImageBitmap(bitmap);
////        materialDialog.dismiss();
//    }

    public void chooseFromAlbum() {
//        imageUri = Uri.fromFile(outputImage);//将文件路径转化为Uri对象,这个Uri对象表示着output_image.jpg 这张图片的本地真实路径
        Intent intent;
        //添加图片的主要代码
        intent = new Intent();
        //设定类型为image
        intent.setType("image/*");
        //设置action
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //选中相片后返回本Activity
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        photobitmap = pic_button.getDrawingCache();
        if (resultCode == RESULT_OK) {
            //取得数据
            Uri uri = data.getData();
            ContentResolver cr = NoteEditorActivity.this.getContentResolver();
            Bitmap bitmap = null;
            Bundle extras = null;
            //如果是选择照片
            if (requestCode == 1) {
                try {
                    //将对象存入Bitmap中
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //如果选择的是拍照
            if (requestCode == 2) {
                System.out.println("-----fjwefowefwef");
                try {
                    if (uri != null)
                        //这个方法是根据Uri获取Bitmap图片的静态方法
                        bitmap = MediaStore.Images.Media.getBitmap(cr, uri);
                        //这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                    else
                        extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            int imgWidth = bitmap.getWidth();
            int imgHeight = bitmap.getHeight();
            double partion = imgWidth * 1.0 / imgHeight;
            double sqrtLength = Math.sqrt(partion * partion + 1);
            //新的缩略图大小
            double newImgW = 680 * (partion / sqrtLength);
            double newImgH = 680  * (1 / sqrtLength);
            float scaleW = (float) (newImgW / imgWidth);
            float scaleH = (float) (newImgH / imgHeight);
            Matrix mx = new Matrix();
            //对原图片进行缩放
            mx.postScale(scaleW, scaleH);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
            bitmap = getBitmapHuaSeBianKuang(bitmap);
            pic_button.setImageBitmap(bitmap);
            //获取drawable格式的图片
//            photodrawable = pic_button.getDrawable();
            //把drawable格式转为byte
//            photobyte = dratobyte(photodrawable);
            //把byte格式的图片转为bitmap格式的图片
//            photobitmap = BitmapFactory.decodeByteArray(photobyte, 0, photobyte.length);
            //用Bitmap格式的图片设置tespic的图片
//            ImageView testpic = (ImageView) findViewById(R.id.testpic);
//            testpic.setImageBitmap(photobitmap);
        }
    }

    //把图片转换成字节 bitmap转变为 byte
     public  byte[] imgtobyte(Bitmap bitmap) {
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }

    //把图片转变为字节 drawable转变为byte
    public byte[] dratobyte(Drawable drawable){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
        return baos.toByteArray();
    }
    //等比例缩放图片
    private Bitmap resize(Bitmap bitmap,int S){
        int imgWidth = bitmap.getWidth();
        int imgHeight = bitmap.getHeight();
        double partion = imgWidth*1.0/imgHeight;
        double sqrtLength = Math.sqrt(partion*partion + 1);
        //新的缩略图大小
        double newImgW = S*(partion / sqrtLength);
        double newImgH = S*(1 / sqrtLength);
        float scaleW = (float) (newImgW/imgWidth);
        float scaleH = (float) (newImgH/imgHeight);

        Matrix mx = new Matrix();
        //对原图片进行缩放
        mx.postScale(scaleW, scaleH);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, imgWidth, imgHeight, mx, true);
        return bitmap;
        }
    //给图片加边框，并返回边框后的图片
    public Bitmap getBitmapHuaSeBianKuang(Bitmap bitmap) {
        float frameSize = 0.2f;
        Matrix matrix = new Matrix();

        // 用来做底图
        Bitmap bitmapbg = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 设置底图为画布
        Canvas canvas = new Canvas(bitmapbg);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG));

        float scale_x = (bitmap.getWidth() - 2 * frameSize - 2) * 1f
                / (bitmap.getWidth());
        float scale_y = (bitmap.getHeight() - 2 * frameSize - 2) * 1f
                / (bitmap.getHeight());
        matrix.reset();
        matrix.postScale(scale_x, scale_y);

        // 对相片大小处理(减去边框的大小)
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);

        // 绘制底图边框
        canvas.drawRect(
                new Rect(0, 0, bitmapbg.getWidth(), bitmapbg.getHeight()),
                paint);
        // 绘制灰色边框
        paint.setColor(Color.GRAY);
        canvas.drawRect(
                new Rect((int) (frameSize), (int) (frameSize), bitmapbg
                        .getWidth() - (int) (frameSize), bitmapbg.getHeight()
                        - (int) (frameSize)), paint);
        canvas.drawBitmap(bitmap, frameSize + 2, frameSize + 2, paint);

        return bitmapbg;
    }
}
