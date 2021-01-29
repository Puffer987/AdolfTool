package cn.adolf.adolf.cache;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2021-01-28 15:18
 **/
public class DiskLruHelper {

    private Context mContext;
    private DiskLruCache mDiskLruCache;

    public DiskLruHelper(Context context, String dirName) {
        mContext = context;
        mDiskLruCache = openCache(mContext, dirName);
    }

    /**
     * 使用DiskLruCache.Editor添加一个缓存
     * 添加一个key：edit(key)
     * 添加一个value：newOutputStream(0)绑定一个outputStream
     * commit()：确认提交
     * abort()：放弃
     */
    public void put(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String key = hashKey(url);
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (downloadUrlToStream(url, outputStream)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取一个缓存
     * get()获得一个DiskLruCache.Snapshot
     * DiskLruCache.Snapshot#getInputStream获取到一个InputStream
     * 再将InputStream转为相应格式的文件
     */
    public InputStream get(String url) {
        InputStream is = null;
        try {
            String key = hashKey(url);
            DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
            if (snapShot != null) {
                is = snapShot.getInputStream(0);
                // Bitmap bitmap = BitmapFactory.decodeStream(is);
                // mImage.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    public boolean remove(String url) {
        boolean success = false;
        try {
            String key = hashKey(url);
            success = mDiskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 获取缓存数据的大小
     * journal文件中CLEAN标记后的数值相加
     */
    public long size() {
        return mDiskLruCache.size();
    }

    /**
     * 将内存中的操作记录同步到journal文件中，一般在onPause中调用
     */
    public void flush() {
        try {
            mDiskLruCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭，一般在onDestroy调用
     */
    public void close() {
        try {
            mDiskLruCache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将所有缓存数据清除
     */
    public void delete() {
        try {
            mDiskLruCache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DiskLruCache.open(缓存路径, app版本号, 一个key可以对应的文件数, 最多可缓存字节数据);
     * 版本号发生改变，缓存数据会被清除
     */
    private DiskLruCache openCache(Context context, String dirName) {
        DiskLruCache mDiskLruCache = null;
        try {
            File cacheDir = getDiskCacheDir(context, dirName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(context), 1, 10 * 1024 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mDiskLruCache;
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {// sd卡有安装或没有被移除
            cachePath = context.getExternalCacheDir().getPath();///sdcard/Android/data/<application package>/cache
        } else {
            cachePath = context.getCacheDir().getPath();///data/data/<application package>/cache
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * HttpURLConnection下载数据
     */
    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static String hashKey(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
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
