package cn.adolf.adolf.wsp3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class DatabaseProviderActivity extends AppCompatActivity {

    private static final String TAG = "DbActivity";
    @BindView(R.id.user_info)
    RecyclerView mUserInfo;
    @BindView(R.id.add_username)
    EditText mAddUsername;
    @BindView(R.id.add_motto)
    EditText mAddMotto;
    @BindView(R.id.add_radio_boy)
    RadioButton mAddRadioBoy;
    @BindView(R.id.add_radio_girl)
    RadioButton mAddRadioGirl;
    @BindView(R.id.add_radio_group)
    RadioGroup mAddRadioGroup;
    @BindView(R.id.add_btn)
    Button mAddBtn;
    @BindView(R.id.group_add)
    LinearLayout mGroupAdd;
    @BindView(R.id.del_id)
    EditText mDelId;
    @BindView(R.id.del_username)
    EditText mDelUsername;
    @BindView(R.id.del_motto)
    EditText mDelMotto;
    @BindView(R.id.del_radio_boy)
    RadioButton mDelRadioBoy;
    @BindView(R.id.del_radio_girl)
    RadioButton mDelRadioGirl;
    @BindView(R.id.del_radio_group)
    RadioGroup mDelRadioGroup;
    @BindView(R.id.del_btn)
    Button mDelBtn;
    @BindView(R.id.group_del)
    LinearLayout mGroupDel;
    private int mSex;
    private UserDbOpenHelper mDbOpenHelper;
    private SQLiteDatabase mDatabase;
    private UserAdapter mUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_provider);
        ButterKnife.bind(this);
        initDatabase();
        initView();
    }

    private void initDatabase() {
        mDbOpenHelper = new UserDbOpenHelper(this, "AdolfDatabase");
    }

    private void initView() {


        mUserAdapter = new UserAdapter(getUser(), this);
        mUserInfo.setLayoutManager(new LinearLayoutManager(this));
        mUserInfo.setAdapter(mUserAdapter);

        bindListener();

    }

    private void bindListener() {
        mAddRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.add_radio_boy) {
                    mSex = 1;
                } else if (checkedId == R.id.add_radio_girl) {
                    mSex = 0;
                }
            }
        });

        mUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DatabaseProviderActivity.this, "query", Toast.LENGTH_SHORT).show();
                mUserAdapter.modifyUserBeans(getUser());
            }
        });
    }

    private List<UserBean> getUser() {
        List<UserBean> mUserBeans = new ArrayList<>();
        mDatabase = mDbOpenHelper.getReadableDatabase();
        Cursor cursor = mDatabase.query("user", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int sex = cursor.getInt(cursor.getColumnIndex("sex"));
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String motto = cursor.getString(cursor.getColumnIndex("motto"));
                UserBean userBean = new UserBean(username, motto, id, sex);
                mUserBeans.add(userBean);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return mUserBeans;
    }

    @OnClick({R.id.add_btn, R.id.del_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_btn:
                String username = mAddUsername.getText().toString();
                String motto = mAddMotto.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(motto)) {
                    mDatabase = mDbOpenHelper.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username", username);
                    contentValues.put("motto", motto);
                    contentValues.put("sex", mSex);
                    long lines = mDatabase.insert("user", null, contentValues);
                    Log.d(TAG, "add: " + lines);
                    contentValues.clear(); // 如果不想重新申请对象，可以clear后使用
                }
                break;
            case R.id.del_btn:
                break;
        }
    }
}