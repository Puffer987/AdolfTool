package cn.adolf.adolf.widget;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class WidgetActivity extends AppCompatActivity {

    @BindView(R.id.date_tv)
    TextView mDateTv;
    @BindView(R.id.date_btn)
    Button mDateBtn;
    @BindView(R.id.time_tv)
    TextView mTimeTv;
    @BindView(R.id.time_btn)
    Button mTimeBtn;
    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog mTimePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget);
        ButterKnife.bind(this);
        mDatePickerDialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDateTv.setText(year+"-"+month+1+"-"+dayOfMonth);
            }
        },2017,3,22);

        mDatePickerDialog.getDatePicker().setCalendarViewShown(false);
        mDatePickerDialog.getDatePicker().setSpinnersShown(true);

        mTimePickerDialog = new TimePickerDialog(this, TimePickerDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mTimeTv.setText(hourOfDay+":"+minute);
                Toast.makeText(WidgetActivity.this, "hour: " + hourOfDay + ", min: " + minute, Toast.LENGTH_SHORT).show();
            }
        }, 22, 30, true);

    }

    @OnClick({R.id.date_btn, R.id.time_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.date_btn:
                mDatePickerDialog.show();
                break;
            case R.id.time_btn:
                mTimePickerDialog.show();
                break;
        }
    }
}