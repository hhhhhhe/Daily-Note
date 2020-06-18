package com.example.lsl.daily_note;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lsl on 2020/6/10.
 */

public class NoteInfo implements Serializable {
    private String id;
    private String title;
    private String content;
    private String date;
    private String des;
    private String pre;
    private byte[] photo;

    //getter and setter

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getPre(){
        return pre;
    }

    public void setPre(String pre) {
        this.pre = pre;
    }
    public String getDes(){
        //定义正则表达式，用于匹配路径
        String content = getContent();
        Pattern p=Pattern.compile("/([^\\.]*)\\.\\w{3}");
        Matcher m=p.matcher(content);
        StringBuffer strBuff = new StringBuffer();
        String title = "";
        int startIndex = 0;
        while(m.find()){
            //取出路径前的文字
            if(m.start() > 0){
                strBuff.append(content.substring(startIndex, m.start()));
            }
            //取出路径
            String path = m.group().toString();
            //取出路径的后缀
            String type = path.substring(path.length() - 3, path.length());
            //判断附件的类型
            if(type.equals("amr")){
                strBuff.append("[录音]");
            }
            else{
                strBuff.append("[图片]");
            }
            startIndex = m.end();
            //只取出前15个字作为标题
            if(strBuff.length() > 15){
                //统一将回车,等特殊字符换成空格
                title = strBuff.toString().replaceAll("\r|\n|\t", " ");
                return title;
            }
        }
        strBuff.append(content.substring(startIndex, content.length()));
        //统一将回车,等特殊字符换成空格
        title = strBuff.toString().replaceAll("\r|\n|\t", " ");
        return title;
    }

    public byte[] getPhoto(){
        return photo;
    }

    public void setPhoto(byte[] photo){
        this.photo = photo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDes(String des) {
        this.des = getDes();
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }
}



