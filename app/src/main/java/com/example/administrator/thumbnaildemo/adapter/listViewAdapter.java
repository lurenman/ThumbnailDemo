package com.example.administrator.thumbnaildemo.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.administrator.thumbnaildemo.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/7.
 */

public class listViewAdapter extends BaseAdapter {
    private Context mContext;
    private  ArrayList<HashMap<String, String>> allPictures;

    public listViewAdapter(Context context,ArrayList<HashMap<String, String>> list)
    {
        mContext=context;
        allPictures=list;
    }

    @Override
    public int getCount() {
        return  allPictures.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.lv_item, parent, false);
            holder.iv_thumbnail= (ImageView) convertView.findViewById(R.id.iv_thumbnail);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
            if (null!=allPictures&&!allPictures.isEmpty())
            {

            }
        }

        return convertView;
    }
    class ViewHolder
    {
        private ImageView iv_thumbnail;
    }


}
