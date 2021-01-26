package cn.adolf.adolf.mediaPlay;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

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
                    if (mChangeListener != null) {
                        mChangeListener.prepared(mMediaPlayer.getDuration());
                    }
                }
            });

            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mChangeListener != null) {
                        mChangeListener.completed();
                    }
                }
            });

            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    if (mChangeListener != null) {
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

    public void setPosition(int m) {
        Log.d(TAG, "setPosition: "+m);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            mMediaPlayer.seekTo(m, MediaPlayer.SEEK_CLOSEST_SYNC);
        else {
            mMediaPlayer.seekTo(m);
        }
    }


    public static String formatTime(int secTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(0);
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(secTime);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);

        StringBuffer ss = new StringBuffer();
        if (hour != 0) {
            ss.append(hour).append(":").append(String.format("%02d", min)).append(":").append(String.format("%02d", sec));
        } else {
            ss.append(String.format("%02d", min)).append(":").append(String.format("%02d", sec));
        }
        return TextUtils.isEmpty(ss.toString()) ? "00:00" : ss.toString();

    }

    public interface OnStatusChangeListener {
        void prepared(int duration);

        void completed();
    }

    private OnStatusChangeListener mChangeListener;

    public void setChangeListener(OnStatusChangeListener changeListener) {
        mChangeListener = changeListener;
    }
}
