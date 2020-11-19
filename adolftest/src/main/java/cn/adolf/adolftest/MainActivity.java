package cn.adolf.adolftest;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AdolfTestMain";
    @BindView(R.id.display_db_rv)
    ListView mDisplayDbRv;
    @BindView(R.id.del_id)
    EditText mDelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_add, R.id.btn_del, R.id.btn_update, R.id.btn_query, R.id.btn_del_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add: {
                UserBean userBean = new UserBean("孤狼", "来自外部的狼" + System.currentTimeMillis(), 1);
                Uri newUri = DbResolverManager.getInstance(this).addUser(userBean);
                Log.d(TAG, "--add--: newUri=" + newUri.toString() + ", newId=" + newUri.getPathSegments().get(1));
                showDb();
                break;
            }
            case R.id.btn_del: {
                int delete = DbResolverManager.getInstance(this).deleteUser("id>?", new String[]{"5"});
                Log.d(TAG, "delete count: " + delete);
                showDb();
                break;
            }
            case R.id.btn_update: {
                UserBean userBean = new UserBean(1, "" + System.currentTimeMillis(), "" + System.currentTimeMillis(), 1);
                int update = DbResolverManager.getInstance(this).updateUserById(userBean);
                Log.d(TAG, "update success: " + (update > 0));
                showDb();
                break;
            }
            case R.id.btn_query: {
                showDb();
                break;
            }
            case R.id.btn_del_id: {
                int id = Integer.parseInt(mDelId.getText().toString());
                if (id > 0) {
                    int del = DbResolverManager.getInstance(this).deleteUserById(id);
                    Log.d(TAG, "del id=" + id + " success :" + (del > 0));
                    showDb();
                }
            }
        }
    }

    private void showDb() {
        Cursor cursor = DbResolverManager.getInstance(this).findAllUser();
        List<Map<String, Object>> lists = new ArrayList<>();
        if (cursor != null) {
            Log.d(TAG, "query result count: " + cursor.getCount());
            while (cursor.moveToNext()) {
                String username = cursor.getString(cursor.getColumnIndex("username"));
                String motto = cursor.getString(cursor.getColumnIndex("motto"));
                int sex = cursor.getInt(cursor.getColumnIndex("sex"));
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                Map<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("username", username);
                map.put("sex", sex);
                map.put("motto", motto);
                lists.add(map);
            }
        }
        SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, lists, R.layout.item_rv_user
                , new String[]{"id", "username", "sex", "motto"}
                , new int[]{R.id.item_id, R.id.item_username, R.id.item_sex, R.id.item_motto});
        mDisplayDbRv.setAdapter(adapter);
    }

}
