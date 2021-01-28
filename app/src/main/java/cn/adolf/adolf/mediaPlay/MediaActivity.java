package cn.adolf.adolf.mediaPlay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class MediaActivity extends AppCompatActivity {
    private static final String TAG = "MediaActivity";

    private String path1 = "http://contres.readboy.com/resources/lexicon/chinese_dictionary/spelling/16/16f127fae7e42dd0947f730b29cc169b.mp3";
    private String path2 = "https://rbebag-zy-test.strongwind.cn/periodic/homework/2302037020073834.mp3";
    private MusicBarFragment mMusicBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.media_play, R.id.media_play2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.media_play:
                mMusicBarFragment = MusicBarFragment.newInstance(path2);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.music_bar, mMusicBarFragment)
                        .commit();
                break;
            case R.id.media_play2:
                mMusicBarFragment = MusicBarFragment.newInstance(path1);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.music_bar, mMusicBarFragment)
                        .commit();
                break;
        }
    }

}