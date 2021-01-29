package cn.adolf.adolf.cache;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class CacheActivity extends AppCompatActivity {
    private static final String TAG = "CacheActivity";
    @BindView(R.id.cache_img)
    ImageView mCacheImg;

    private static String[] urls = new String[]{
            "https://i.loli.net/2021/01/28/xoQ5rpvig4tzaNP.png",
            "https://i.loli.net/2021/01/28/uQ2q79Xvnlojf3s.png",
            "https://i.loli.net/2021/01/28/Ih9vRYXUsuafbrl.jpg"};
    private DiskLruHelper mDiskLru;
    private MemoryLruHelper mMemoryLru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache);
        ButterKnife.bind(this);

        mMemoryLru = new MemoryLruHelper(this, mCacheImg, this);

        mDiskLru = new DiskLruHelper(this, "bitmap");
        mDiskLru.put(urls[1]);
        InputStream inputStream = mDiskLru.get(urls[1]);
        if (inputStream != null) {
            mCacheImg.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        }

        displayCircle(this,mCacheImg,"https://i.loli.net/2021/01/28/Ih9vRYXUsuafbrl.jpg");
    }

    // public static void displayRoundrect(Context context, ImageView imageView, String url, int corner)  throws IllegalArgumentException{
    //     //加载圆角矩形 corner 圆角度数
    //     if (imageView == null) {
    //         throw new IllegalArgumentException("argument error");
    //     }
    //     //如果加载其他形状的可以设置不同的 GlideRoundedCorners.CornerType
    //     //如 GlideRoundedCorners.CornerType.TOP,GlideRoundedCorners.CornerType.TOP_LEFT_BOTTOM_RIGHT 等等
    //     GlideRoundedCorners c = new GlideRoundedCorners(corner, GlideRoundedCorners.CornerType.ALL);
    //     RequestOptions options = new RequestOptions().optionalTransform(c);
    //     Glide.with(context)
    //             .load(url)
    //             .apply(options)
    //             .into(imageView);
    //
    // }

    public void displayCircle(Context context, ImageView imageView, String url)  throws IllegalArgumentException{
        //加载圆形图片
        if (imageView == null) {
            throw new IllegalArgumentException("argument error");
        }
        // RequestOptions options = new RequestOptions()
        //         .circleCrop()//圆形
        //         .placeholder(R.drawable.ic_launcher_background)//占位图
        //         .error(R.drawable.ic_launcher_background);//加载失败图
        // Glide.with(context)
        //         .load(url)
        //         .apply(options)
        //         .into(imageView);

        Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
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