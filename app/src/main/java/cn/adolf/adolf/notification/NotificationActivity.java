package cn.adolf.adolf.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.MainActivity;
import cn.adolf.adolf.R;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.btn_show)
    Button mBtnShow;
    @BindView(R.id.btn_close)
    Button mBtnClose;

    Context mContext;
    private NotificationManager mNManager;
    private Notification notify1;
    Bitmap LargeBitmap = null;
    private static final int NOTIFYID_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        mContext = NotificationActivity.this;
        setNotification();
    }

    private void setNotification() {
        mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        LargeBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_download_big);
    }

    @OnClick({R.id.btn_show, R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                Intent it = new Intent(mContext, MainActivity.class);
                PendingIntent pit = PendingIntent.getActivity(mContext, 0, it, 0);

                //设置图片,通知标题,发送时间,提示方式等属性
                Notification.Builder mBuilder = new Notification.Builder(this);
                mBuilder.setContentTitle("正在更新在线数据...")         //标题
                        .setContentText("0%")   //内容
                        .setSubText("请保持网络连接")                       //内容下面的一小段文字
                        .setTicker("检测到有新数据可以更新")               //收到信息后状态栏显示的文字信息
                        .setWhen(System.currentTimeMillis())                //设置通知时间
                        .setSmallIcon(R.mipmap.ic_download_small)           //设置小图标
                        .setLargeIcon(LargeBitmap)                          //设置大图标
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                        // .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.biaobiao))  //设置自定义的提示音
                        .setAutoCancel(true)                            //设置点击后取消Notification
                        .setProgress(100,0,false );
                        // .setContentIntent(pit);                        //设置PendingIntent


                new Thread(() -> {
                    for (int i = 1; i < 101; i++) {
                        mBuilder.setProgress(100,i,false)
                        .setContentText(i+"%");

                        notify1 = mBuilder.build();
                        mNManager.notify(NOTIFYID_1, notify1);
                        SystemClock.sleep(50);
                    }

                    mBuilder.setContentText("").setContentTitle("更新成功").setProgress(0,0,false);
                    notify1 = mBuilder.build();
                    mNManager.notify(NOTIFYID_1, notify1);
                }).start();

                break;
            case R.id.btn_close:
                mNManager.cancel(NOTIFYID_1);                          // 取消Notification

                break;
        }
    }
}