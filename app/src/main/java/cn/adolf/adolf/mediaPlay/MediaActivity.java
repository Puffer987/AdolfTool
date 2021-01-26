package cn.adolf.adolf.mediaPlay;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
    @BindView(R.id.voice_seek)
    SeekBar mVoiceSeek;
    @BindView(R.id.voice_close)
    ImageView mVoiceClose;
    @BindView(R.id.voice_play)
    ImageView mVoicePlay;
    @BindView(R.id.voice_pause)
    ImageView mVoicePause;
    @BindView(R.id.voice_time)
    TextView mVoiceTime;

    private String path1 = "http://contres.readboy.com/resources/lexicon/chinese_dictionary/spelling/16/16f127fae7e42dd0947f730b29cc169b.mp3";
    private String path2 = "https://rbebag-zy-test.strongwind.cn/periodic/homework/2302037020073834.mp3";
    private Runnable mRunnable;
    private int mDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        ButterKnife.bind(this);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                int curPosition = MediaPlayHelper.getInstance().getCurPosition();
                Message message = Message.obtain(mHandler, 0x02, mDuration, curPosition);
                message.sendToTarget();
                mHandler.postDelayed(this, 1000);
            }
        };

        MediaPlayHelper.getInstance().setChangeListener(new MediaPlayHelper.OnStatusChangeListener() {
            @Override
            public void prepared(int duration) {
                mDuration = duration;
                MediaPlayHelper.getInstance().start();
                mHandler.postDelayed(mRunnable, 1000);
                switchPlayStatusIcon(0);
            }

            @Override
            public void completed() {
                mHandler.removeCallbacks(mRunnable);
                switchPlayStatusIcon(1);
                mVoiceSeek.setProgress(100);
            }
        });

        mVoiceSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                MediaPlayHelper.getInstance().setPosition(progress * mDuration / 100);
                MediaPlayHelper.getInstance().start();
            }
        });

    }

    private MyHandler mHandler = new MyHandler(MediaActivity.this);

    @OnClick({R.id.voice_close, R.id.voice_play, R.id.voice_pause})
    public void onOperaClicked(View view) {
        switch (view.getId()) {
            case R.id.voice_close:
                break;
            case R.id.voice_play:
                break;
            case R.id.voice_pause:
                break;
        }
    }

    public class MyHandler extends Handler {
        // 定义 弱引用实例
        private WeakReference<Activity> reference;

        // 在构造方法中传入需持有的Activity实例
        public MyHandler(Activity activity) {
            reference = new WeakReference<>(activity); // 使用WeakReference弱引用持有Activity实例
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:

                    break;
                case 0x02:
                    Log.d(TAG, ": arg2: " + msg.arg2 + " - arg1: " + msg.arg1);
                    mVoiceTime.setText(MediaPlayHelper.formatTime(msg.arg2));
                    mVoiceSeek.setProgress(msg.arg2 * 100 / msg.arg1);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayHelper.getInstance().stop();
    }

    @OnClick({R.id.media_play, R.id.media_play2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.media_play:
                MediaPlayHelper.getInstance().setPath(path1);
                break;
            case R.id.media_play2:
                MediaPlayHelper.getInstance().setPath(path2);
                break;
        }
    }

    public void switchPlayStatusIcon(int i) {
        mVoicePause.setVisibility(View.GONE);
        mVoicePlay.setVisibility(View.GONE);
        if (i == 0) {
            mVoicePause.setVisibility(View.VISIBLE);
        } else if (i == 1) {
            mVoicePlay.setVisibility(View.VISIBLE);
        }
    }
}