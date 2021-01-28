package cn.adolf.adolf.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2021-01-28 10:34
 **/
public class ImageLoader {
    private LruCache<String, Bitmap> mLruCache;

    public ImageLoader() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        int cacheSize = (int) (maxMemory / 2);
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public void addBitmap(String key, Bitmap bitmap) {
        if (getBitmap(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmap(String key) {
        return mLruCache.get(key);
    }

    public void removeBitmap(String key) {
        mLruCache.remove(key);
    }

}
