package com.example.administrator.thumbnaildemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.thumbnaildemo.R;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class GridLocalAdapter extends BaseAdapter {
    private List<String> mDatas;
    private Context mContext;
    private Transferee transferee;
    private TransferConfig config;
    private GridView gvImages;

    public GridLocalAdapter(Context context,List<String> images,Transferee transferee,TransferConfig config,GridView gvImages)
    {
        mDatas=images;
        mContext=context;
        this.transferee=transferee;
        this.config=config;
        this.gvImages=gvImages;
    }

    @Override
    public int getCount() {
        return mDatas==null?0:mDatas.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_image, parent, false);
            holder.image_view= (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null!=mDatas&&!mDatas.isEmpty())
        {
            Glide.with(mContext)
                    .load(mDatas.get(position))
                    .centerCrop()
                    .placeholder(R.mipmap.ic_empty_photo)
                    .into(holder.image_view);

            holder.image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    config.setNowThumbnailIndex(position);
                    config.setOriginImageList(wrapOriginImageViewList(mDatas.size()));

                    transferee.apply(config).show(new Transferee.OnTransfereeStateChangeListener() {
                        @Override
                        public void onShow() {
                            Glide.with(mContext).pauseRequests();
                        }

                        @Override
                        public void onDismiss() {
                            Glide.with(mContext).resumeRequests();
                        }
                    });
                }
            });
        }

        return convertView;
    }
    /**
     * 包装缩略图 ImageView 集合
     * <p>
     * 注意：此方法只是为了收集 Activity 列表中所有可见 ImageView 好传递给 transferee。
     * 如果你添加了一些图片路径，扩展了列表图片个数，让列表超出屏幕，导致一些 ImageViwe 不
     * 可见，那么有可能这个方法会报错。这种情况，可以自己根据实际情况，来设置 transferee 的
     * originImageList 属性值
     *
     * @return
     */
    @NonNull
    public  List<ImageView> wrapOriginImageViewList(int size) {
        List<ImageView> originImgList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ImageView thumImg = (ImageView) ((LinearLayout) gvImages.getChildAt(i)).getChildAt(0);
            originImgList.add(thumImg);
        }
        return originImgList;

    }
    class ViewHolder
    {
        private ImageView image_view;
    }
}
