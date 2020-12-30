package cn.adolf.adolf.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.AlarmClock;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cn.adolf.adolf.R;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link DaysWidgetConfigureActivity DaysWidgetConfigureActivity}
 */
public class DaysWidget extends AppWidgetProvider {

    private static final String TAG = "jq-loveDays";
    private final static String FORCE_UPDATE = "cn.adolf.widget.action.FORCE_UPDATE";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (Objects.equals(intent.getAction(), FORCE_UPDATE)) {
            Uri data = intent.getData();
            int resId = -1;
            int widgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            if (data != null) {
                String[] split = data.getSchemeSpecificPart().split("-");// 返回":"和"#"之间的字符串
                resId = Integer.parseInt(split[0]);
                widgetId = Integer.parseInt(split[1]);
                Log.e(TAG, "onReceive: " + data.getSchemeSpecificPart());
            }
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.days_widget);
            switch (resId) {
                case R.id.layout_parent:
                    Log.e(TAG, "强制更新: " + System.currentTimeMillis());

                    long timestamp = DaysWidgetConfigureActivity.loadTimestampPref(context, widgetId);

                    String sumDays = SumUtils.getSumDays(timestamp);
                    Bitmap bitmap = SumUtils.buildBitmapWithShadow(context, sumDays, 150);
                    views.setImageViewBitmap(R.id.img_days, bitmap);
                    break;
            }
            //获得appwidget管理实例，用于管理appwidget以便进行更新操作
            AppWidgetManager manger = AppWidgetManager.getInstance(context);

            // updateAppWidget(context, manger, widgetId);
            // 相当于获得所有本程序创建的appwidget
            ComponentName thisName = new ComponentName(context, DaysWidget.class);
            //更新widget
            manger.updateAppWidget(widgetId, views);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.e(TAG, "updateAppWidget: " + appWidgetId);
        long timestamp = DaysWidgetConfigureActivity.loadTimestampPref(context, appWidgetId);

        String sumDays = SumUtils.getSumDays(timestamp);
        Log.d(TAG, "sumDays: " + sumDays);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.days_widget);

        Bitmap bitmap = SumUtils.buildBitmapWithShadow(context, sumDays, 150);
        views.setImageViewBitmap(R.id.img_days, bitmap);
        // 跳转闹钟界面需要声明权限  <uses-permission android:name="com.android.alarm.permission.SET_ALARM" />
        views.setOnClickPendingIntent(R.id.img_days, getActivityIntent(context, AlarmClock.ACTION_SHOW_ALARMS));
        views.setOnClickPendingIntent(R.id.layout_parent, getForceUpdateBroadcastIntent(context, R.id.layout_parent, appWidgetId));
        views.setOnClickPendingIntent(R.id.right_bottom, getWeChatScanIntent(context));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate: " + Arrays.toString(appWidgetIds));
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted: " + Arrays.toString(appWidgetIds));
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            DaysWidgetConfigureActivity.deleteTimestampPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.e(TAG, "onEnabled: ");
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled: ");
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getForceUpdateBroadcastIntent(Context context, int resID, int widgetId) {
        Intent intent = new Intent();
        intent.setClass(context, DaysWidget.class);//此时这句代码去掉
        intent.setAction(FORCE_UPDATE);
        //设置data域的时候，把控件id一起设置进去，
        // 因为在绑定的时候，是将同一个id绑定在一起的，所以哪个控件点击，发送的intent中data中的id就是哪个控件的id
        intent.setData(Uri.parse("id:" + resID + "-" + widgetId));

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    private static PendingIntent getActivityIntent(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);

        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private static PendingIntent getWeChatScanIntent(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.tencent.mm");
        if (intent != null) {
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            return PendingIntent.getActivity(context, 0, intent, 0);
        }
        return null;
    }
}

