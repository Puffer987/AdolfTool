package cn.adolf.adolf.mediaPlay;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class MusicBarFragment extends Fragment {

    private static final String ARG_PATH = "path";
    private static final String TAG = "MusicBarFragment";
    @BindView(R.id.voice_play)
    ImageView mVoicePlay;
    @BindView(R.id.voice_pause)
    ImageView mVoicePause;
    @BindView(R.id.voice_seek)
    SeekBar mVoiceSeek;
    @BindView(R.id.voice_time)
    TextView mVoiceTime;
    @BindView(R.id.voice_close)
    ImageView mVoiceClose;
    @BindView(R.id.voice_loading)
    ProgressBar mVoiceLoading;

    private String mPath;
    private MediaPlayer mPlayer;
    private Runnable mTimeRunable;
    private MyHandler mHandler;
    private int mDuration;

    public MusicBarFragment() {
        // Required empty public constructor
    }

    public static MusicBarFragment newInstance(String path) {
        MusicBarFragment fragment = new MusicBarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPath = getArguments().getString(ARG_PATH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_bar, container, false);
        ButterKnife.bind(this, view);
        initPlayer();
        init();
        return view;
    }

    private void init() {
        mHandler = new MyHandler(getActivity());

        mTimeRunable = new Runnable() {
            @Override
            public void run() {
                int curPosition = mPlayer.getCurrentPosition();
                Message message = Message.obtain(mHandler, 0x02, mDuration, curPosition);
                // mHandler.sendMessageDelayed(message, 1000);
                message.sendToTarget();
                mHandler.postDelayed(this, 1000);
            }
        };
        mVoiceSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVoiceTime.setText(MediaPlayHelper.formatTime(progress * mDuration / 100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                mPlayer.seekTo(progress * mDuration / 100);
                // mPlayer.start();
            }
        });
    }

    private void initPlayer() {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mPath);
            // mPlayer.prepareAsync();
            switchBtn(0);
            setListener();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setListener() throws IOException {
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mDuration = mediaPlayer.getDuration();
                switchBtn(1);
            }
        });

        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mVoiceSeek.setSecondaryProgress(percent);
                Log.d(TAG, "onBufferingUpdate: " + percent);
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mHandler.removeCallbacks(mTimeRunable);
                mVoiceSeek.setProgress(100);
                mVoiceTime.setText(MediaPlayHelper.formatTime(mDuration));
                switchBtn(1);
                // mPlayer.pause();
            }
        });

        mPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {
                switchBtn(2);
                // if (mp != null && !mp.isPlaying()) {
                //     mp.start();
                // }
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e(TAG, "播放出现问题 what:" + what + ",extra:" + extra);
                Toast.makeText(getContext(), "播放出现问题", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    /**
     * 0：加载
     * 1：播放按钮显示，音乐没有播放
     * 2：暂停按钮显示，音乐播放中
     */
    private void switchBtn(int btn) {
        mVoiceLoading.setVisibility(View.GONE);
        mVoicePlay.setVisibility(View.GONE);
        mVoicePause.setVisibility(View.GONE);
        switch (btn) {
            case 0:
                mPlayer.prepareAsync();
                mVoiceLoading.setVisibility(View.VISIBLE);
                break;
            case 1:
                if (mPlayer.isPlaying()) {
                mHandler.removeCallbacks(mTimeRunable);
                mPlayer.pause();
                }
                mVoicePlay.setVisibility(View.VISIBLE);
                break;
            case 2:
                if (!mPlayer.isPlaying()) {
                mHandler.postDelayed(mTimeRunable, 1000);
                mPlayer.start();
                }
                mVoicePause.setVisibility(View.VISIBLE);
                break;
        }
    }

    @OnClick({R.id.voice_play, R.id.voice_pause, R.id.voice_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.voice_play:
                switchBtn(2);
                break;
            case R.id.voice_pause:
                switchBtn(1);
                break;
            case R.id.voice_close:
                // switchBtn(1);
                // mPlayer.stop();
                mPlayer.seekTo(10000);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
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
}