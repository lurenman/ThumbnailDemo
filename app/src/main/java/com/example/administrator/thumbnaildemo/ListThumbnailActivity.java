package com.example.administrator.thumbnaildemo;
import com.example.administrator.thumbnaildemo.adapter.listViewAdapter;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ListThumbnailActivity extends Activity implements EasyPermissions.PermissionCallbacks
{
    private ListView lv_list;
    private listViewAdapter mListViewAdapter;
    private  ArrayList<HashMap<String, String>> allPictures;
    private ImageView iv_ceshi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listthunmbnail);
        checkpermission();
        initVriabls();
        initView();
        initEvent();
        getNetData();

    }

    private void checkpermission()
    {
        if (EasyPermissions.hasPermissions(ListThumbnailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(),"已获得权限",Toast.LENGTH_SHORT).show();
            //获取权限调用查询缩略图数据库的方法，但是测试华为6.0的thumbnails数据是空的
            //allPictures=getAllPictures(getApplicationContext());
        } else {
            EasyPermissions.requestPermissions(ListThumbnailActivity.this, "需要访问sd卡权限", 800, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }


    private void initVriabls()
    {
    }

    private void initView()
    {
        iv_ceshi= (ImageView) findViewById(R.id.iv_ceshi);
        lv_list= (ListView) findViewById(R.id.lv_list);
        if (null!=allPictures&&!allPictures.isEmpty())
        mListViewAdapter=new listViewAdapter(getApplicationContext(),allPictures);
        lv_list.setAdapter(mListViewAdapter);

    }
    private void initEvent()
    {

    }
    private void getNetData()
    {
        //String path="/storage/emulated/0/Pictures/监理/IMG_20170807_150627.jpg";
        //生成缩略图的方法
        //Bitmap imageThumbnail = getImageThumbnail(path, 300, 300);
        //iv_ceshi.setImageBitmap(imageThumbnail);
    }
    /**
     * 得到本地图片文件
     * @param context
     * @return
     */
    public static ArrayList<HashMap<String,String>> getAllPictures(Context context) {
        ArrayList<HashMap<String,String>> picturemaps = new ArrayList<>();
        HashMap<String,String> picturemap;
        ContentResolver cr = context.getContentResolver();
        //先得到缩略图的URL和对应的图片id     MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        Cursor cursor = cr.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.IMAGE_ID,
                        MediaStore.Images.Thumbnails.DATA
                },
                null,
                null,
                null);
        if (cursor.moveToFirst()) {
            do {
                picturemap = new HashMap<>();
                picturemap.put("image_id_path",cursor.getInt(0)+"");
                picturemap.put("thumbnail_path",cursor.getString(1));
                picturemaps.add(picturemap);
            } while (cursor.moveToNext());
            cursor.close();
        }
        //再得到正常图片的path
        for (int i = 0;i<picturemaps.size();i++) {
            picturemap = picturemaps.get(i);
            String media_id = picturemap.get("image_id_path");
            cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Images.Media.DATA
                    },
                    MediaStore.Audio.Media._ID+"="+media_id,
                    null,
                    null
            );
            if (cursor.moveToFirst()) {
                do {
                    picturemap.put("image_id",cursor.getString(0));
                    picturemaps.set(i,picturemap);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return picturemaps;
    }
    //生成缩略图的方法
    //     * 根据指定的图像路径和大小来获取缩略图
//     * 此方法有两点好处：
//     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
//     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
//     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
//     *        用这个工具生成的图像不会被拉伸。
//     * @param imagePath 图像的路径
//     * @param width 指定输出图像的宽度
//     * @param height 指定输出图像的高度
//     * @return 生成的缩略图

    private Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == 200) {
            if (perms.contains(Manifest.permission.CAMERA)) {
                Toast.makeText(getApplicationContext(),"获取权限成功",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == 200) {
            if (perms.contains(Manifest.permission.CAMERA)) {
                Toast.makeText(getApplicationContext(),"权限被禁止",Toast.LENGTH_SHORT).show();
            }
        }
    }



}
