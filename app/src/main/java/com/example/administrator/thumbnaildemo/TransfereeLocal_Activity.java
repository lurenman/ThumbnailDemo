package com.example.administrator.thumbnaildemo;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.thumbnaildemo.adapter.GridLocalAdapter;
import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.index.CircleIndexIndicator;
import com.hitomi.tilibrary.style.progress.ProgressBarIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 * 用glide加载本地transferee的使用
 */

public class TransfereeLocal_Activity extends BaseActivity {
    //原图片的集合
    private List<String> images;
    private GridLocalAdapter mGridLocalAdapter;
    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_transfereelocal);
        gvImages= (GridView) findViewById(R.id.gv_images);


    }

    @Override
    protected void initEnvent() {
        super.initEnvent();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        } else {
            images = getLatestPhotoPaths(9);
            initTransfereeConfig();
            if (images != null && !images.isEmpty())
            {
                gvImages.setAdapter(new GridLocalAdapter(TransfereeLocal_Activity.this,images,transferee,config,gvImages));
            }
        }
    }
    /**
     * 使用ContentProvider读取SD卡最近图片
     *
     * @param maxCount 读取的最大张数
     * @return
     */
    private List<String> getLatestPhotoPaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;

        ContentResolver mContentResolver = getContentResolver();

        // 只查询jpg和png的图片,按最新修改排序
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);

        List<String> latestImagePaths = null;
        if (cursor != null) {
            //从最新的图片开始读取.
            //当cursor中没有数据时，cursor.moveToLast()将返回false
            if (cursor.moveToLast()) {
                latestImagePaths = new ArrayList<String>();

                while (true) {
                    // 获取图片的路径
                    String path = cursor.getString(0);
                    if (!latestImagePaths.contains(path))
                        latestImagePaths.add(path);

                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }

        return latestImagePaths;
    }
    private void initTransfereeConfig() {
        config = TransferConfig.build()
                .setSourceImageList(images)
                .setMissPlaceHolder(R.mipmap.ic_empty_photo)
                .setErrorPlaceHolder(R.mipmap.ic_empty_photo)
                .setProgressIndicator(new ProgressBarIndicator())
                .setIndexIndicator(new CircleIndexIndicator())
                .setJustLoadHitImage(true)
                .setImageLoader(GlideImageLoader.with(getApplicationContext()))
                .setOnLongClcikListener(new Transferee.OnTransfereeLongClickListener() {
                    @Override
                    public void onLongClick(ImageView imageView, int i) {
                      Toast.makeText(getApplicationContext(),"长按点击了",Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
    }

    @Override
    protected void loadData() {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE) {
            images = getLatestPhotoPaths(9);
            initTransfereeConfig();
            if (images != null && !images.isEmpty())
                gvImages.setAdapter(new GridLocalAdapter(TransfereeLocal_Activity.this,images,transferee,config,gvImages));
        } else {
            Toast.makeText(this, "请允许获取相册图片文件访问权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //这个不销毁就会出现异常，亲测
        transferee.destroy();
    }
}
