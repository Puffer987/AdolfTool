package cn.adolf.adolf.mediaPlay;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import cn.adolf.adolf.App;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2021-01-22 11:03
 **/
public class MediaPlayHelper {
    private static final String TAG = "MediaPlayHelper";
    private static MediaPlayHelper instance;
    private MediaPlayer mMediaPlayer;

    private MediaPlayHelper() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());

    }

    public static MediaPlayHelper getInstance() {
        synchronized (MediaPlayHelper.class) {
            if (instance == null) {
                instance = new MediaPlayHelper();
            }
        }
        return instance;
    }

    public int getCurPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void setPath(String path) {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(App.appContext, Uri.parse(path));
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mChangeListener!=null) {
                        mChangeListener.prepared(mMediaPlayer.getDuration());
                    }
                }
            });

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mChangeListener!=null) {
                        mChangeListener.completed();
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        if (mMediaPlayer.isPlaying()) {
            return;
        }
        mMediaPlayer.start();

    }

    public void stop() {
        mMediaPlayer.stop();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public interface OnStatusChangeListener{
        void prepared(int duration);
        void completed();
    }

    private OnStatusChangeListener mChangeListener;

    public void setChangeListener(OnStatusChangeListener changeListener) {
        mChangeListener = changeListener;
    }
}
