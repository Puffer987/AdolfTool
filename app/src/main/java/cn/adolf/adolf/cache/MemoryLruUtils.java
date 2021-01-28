package cn.adolf.adolf.cache;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2021-01-28 15:39
 **/
public class MemoryLruUtils {
    private static final String TAG = "MemoryLruUtils";
    private MyHandler mHandler;
    private Context mContext;
    private ImageLoader mImageLoader;
    private ImageView imageView;

    public MemoryLruUtils(Context context, ImageView imageView, AppCompatActivity activity) {
        mContext = context;
        this.imageView = imageView;
        mHandler = new MyHandler(activity);
        mImageLoader = new ImageLoader();
    }

    public void loadImg(String path) {
        Log.d(TAG, "url: " + path);
        Bitmap bitmap = mImageLoader.getBitmap(hashKeyForCache(path));
        if (bitmap != null) {//有缓存
            Toast.makeText(mContext, "从缓存中取出图片", Toast.LENGTH_SHORT).show();
            imageView.setImageBitmap(bitmap);
        } else {//没有缓存
            Toast.makeText(mContext, "从网络下载图片", Toast.LENGTH_SHORT).show();
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(path)
                    .build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: ", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    byte[] Picture_bt = response.body().bytes();

                    Bitmap bitmap = BitmapFactory.decodeByteArray(Picture_bt, 0, Picture_bt.length);
                    mImageLoader.addBitmap(hashKeyForCache(path), bitmap);

                    Message message = mHandler.obtainMessage();
                    message.obj = Picture_bt;
                    message.what = 0x01;
                    mHandler.sendMessage(message);

                }
            });
        }
    }

    class MyHandler extends Handler {
        //创建一个类继承 Handler
        WeakReference<AppCompatActivity> mWeakReference;

        public MyHandler(AppCompatActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        //在 handleMessage 方法中对网络下载的图片进行处理
        @Override
        public void handleMessage(Message msg) {
            final AppCompatActivity appCompatActivity = mWeakReference.get();
            if (appCompatActivity != null) {
                switch (msg.what) {
                    case 0x01://成功
                        Log.d(TAG, "success: ");
                        byte[] Picture = (byte[]) msg.obj;
                        Bitmap bitmap = BitmapFactory.decodeByteArray(Picture, 0, Picture.length);
                        imageView.setImageBitmap(bitmap);

                        break;
                    case 0x02://失败
                        Log.d(TAG, "failure: ");
                        break;
                }

            }
        }
    }

    public static String hashKeyForCache(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        // return key;
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
