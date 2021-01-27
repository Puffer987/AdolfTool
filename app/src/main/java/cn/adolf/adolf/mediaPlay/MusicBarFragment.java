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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.lang.ref.WeakReference;

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
    }

    private void initPlayer() {
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(mPath);
            mPlayer.prepareAsync();
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
                mDuration = mPlayer.getDuration();
                switchBtn(1);
            }
        });

        mPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                mVoiceSeek.setSecondaryProgress(percent);
            }
        });

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mHandler.removeCallbacks(mTimeRunable);
                mVoiceSeek.setProgress(100);
                mVoiceTime.setText(MediaPlayHelper.formatTime(mDuration));
                mPlayer.stop();
            }
        });
    }

    private void switchBtn(int btn) {
        mVoiceLoading.setVisibility(View.GONE);
        mVoicePlay.setVisibility(View.GONE);
        mVoicePause.setVisibility(View.GONE);
        switch (btn) {
            case 0:
                mVoiceLoading.setVisibility(View.VISIBLE);
                break;
            case 1:
                mVoicePlay.setVisibility(View.VISIBLE);
                break;
            case 2:
                mVoicePause.setVisibility(View.VISIBLE);
                break;
        }
    }


    @OnClick({R.id.voice_play, R.id.voice_pause, R.id.voice_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.voice_play:
                mHandler.postDelayed(mTimeRunable, 1000);
                switchBtn(2);
                mPlayer.start();
                break;
            case R.id.voice_pause:
                switchBtn(1);
                mPlayer.pause();
                break;
            case R.id.voice_close:
                switchBtn(1);
                mPlayer.stop();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.reset();
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