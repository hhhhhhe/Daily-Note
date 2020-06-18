package com.example.lsl.daily_note;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * Created by lsl on 2020/6/10.
 */

//适配器 用于配置列表的item
class ViewHolder{
    public ImageView itemIcon;
    public TextView itemNoteTitle;
    public TextView itemNoteDate;
    public TextView itemNoteDes;
    public TextView itemNotePer;
    View itemView;

    public ViewHolder(View itemView) {
        if (itemView == null){
            throw new IllegalArgumentException("item View can not be null!");
        }
        this.itemView = itemView;
        itemIcon = (ImageView) itemView.findViewById(R.id.rand_icon);
        itemNoteTitle = (TextView) itemView.findViewById(R.id.item_note_title);
        itemNoteDate = (TextView) itemView.findViewById(R.id.item_note_date);
        itemNoteDes = (TextView) itemView.findViewById(R.id.des);
        itemNotePer = (TextView) itemView.findViewById(R.id.pre);
    }
}
public class ListAdapter extends BaseAdapter {

    private List<NoteInfo> noteList;
    private LayoutInflater layoutInflater;
    private Context context;
    private ViewHolder holder = null;

    public ListAdapter(Context context,List<NoteInfo> noteList) {
        this.noteList = noteList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteList.get(position).getTitle();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(noteList.get(position).getId());
    }

    public void remove(int index){
        noteList.remove(index);
    }

    public void refreshDataSet(){
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.item_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.itemNoteTitle.setText(noteList.get(position).getTitle());
        holder.itemNoteDate.setText(noteList.get(position).getDate());
        holder.itemNoteDes.setText(noteList.get(position).getDes());
        holder.itemNotePer.setText("记事项优先级："+noteList.get(position).getPre());
        return convertView;
    }
}