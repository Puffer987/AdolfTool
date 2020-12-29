package cn.adolf.adolf.widget;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

/**
 * The configuration screen for the {@link DaysWidget DaysWidget} AppWidget.
 */
public class DaysWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "cn.adolf.adolf.widget.DaysWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetDate;
    EditText mAppWidgetTime;
    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.date_btn)
    Button mDateBtn;
    @BindView(R.id.time_tv)
    TextView mTimeTv;
    @BindView(R.id.time_btn)
    Button mTimeBtn;
    @BindView(R.id.add_button)
    Button mAddButton;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;
    private Calendar mCalendar;


    public DaysWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTimestampPref(Context context, int appWidgetId, long multi) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putLong(PREF_PREFIX_KEY + appWidgetId, multi);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static long loadTimestampPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        long timestamp = prefs.getLong(PREF_PREFIX_KEY + appWidgetId, 0);
        if (timestamp != 0) {
            return timestamp;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2017, 3, 22, 22, 30);
            return calendar.getTimeInMillis();
        }
    }

    static void deleteTimestampPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED); // 按返回键时，会取消放置小部件

        setContentView(R.layout.days_widget_configure);
        ButterKnife.bind(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);


        mDateTv.setText(mCalendar.get(Calendar.YEAR) + "-" + mCalendar.get(Calendar.MONTH) + "-" + mCalendar.get(Calendar.DATE));
        mTimeTv.setText("00:00");


        mDatePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DATE, dayOfMonth);
                mDateTv.setText(String.format("%d-%02d-%02d", year, (month + 1), dayOfMonth));
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DATE));

        mDatePickerDialog.getDatePicker().setCalendarViewShown(false);
        mDatePickerDialog.getDatePicker().setSpinnersShown(true);

        mTimePickerDialog = new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                mCalendar.set(Calendar.MINUTE, minute);
                mTimeTv.setText(String.format("%02d:%02d", hourOfDay, minute));
            }
        }, 0, 0, true);

    }

    @OnClick({R.id.date_btn, R.id.time_btn, R.id.add_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.date_btn:
                mDatePickerDialog.show();
                break;
            case R.id.time_btn:
                mTimePickerDialog.show();
                break;
            case R.id.add_button:
                addOne();
                break;
        }
    }

    private void addOne() {
        final Context context = DaysWidgetConfigureActivity.this;

        Log.d("widgetConfig", "mCalendar: " + mCalendar.getTime().toString());

        saveTimestampPref(context, mAppWidgetId, mCalendar.getTimeInMillis());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        DaysWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

