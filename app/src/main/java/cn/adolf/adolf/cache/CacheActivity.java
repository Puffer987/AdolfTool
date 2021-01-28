package cn.adolf.adolf.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class CacheActivity extends AppCompatActivity {
    private static final String TAG = "CacheActivity";
    private static ImageLoader mImageLoader;
    @BindView(R.id.cache_img)
    ImageView mCacheImg;

    private static String[] urls = new String[]{
            "https://i.loli.net/2021/01/28/xoQ5rpvig4tzaNP.png",
            "https://i.loli.net/2021/01/28/uQ2q79Xvnlojf3s.png",
            "https://i.loli.net/2021/01/28/Ih9vRYXUsuafbrl.jpg"};
    private DiskLruHelper mDiskLru;
    private MemoryLruUtils mMemoryLru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        ButterKnife.bind(this);

        mMemoryLru = new MemoryLruUtils(this, mCacheImg, this);

        mDiskLru = new DiskLruHelper(this, "bitmap");
        mDiskLru.put(urls[1]);
        InputStream inputStream = mDiskLru.get(urls[1]);
        if (inputStream != null) {
            mCacheImg.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        }
    }

    @OnClick({R.id.cache_btn1, R.id.cache_btn2, R.id.cache_btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cache_btn1:
                mMemoryLru.loadImg(urls[0]);
                break;
            case R.id.cache_btn2:
                mMemoryLru.loadImg(urls[1]);
                break;
            case R.id.cache_btn3:
                mMemoryLru.loadImg(urls[2]);
                break;
        }
    }
}