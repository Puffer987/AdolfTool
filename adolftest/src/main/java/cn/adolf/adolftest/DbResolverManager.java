package cn.adolf.adolftest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * @program: Adolf
 * @description: 好像mybatis中的mapper
 * @author: yjq
 * @create: 2020-11-19 08:45
 **/
public class DbResolverManager {

    private static final String ADOLF_URI = "content://cn.adolf.db.provider/";
    private static final String TAG = "DbResolverManager";
    private static DbResolverManager instance; // 懒汉，在需要使用时才实例化
    // private static DatabaseManager instance= new DatabaseManager(context); // 饿汉，初始化时就实例化
    private Context mContext;
    private ContentResolver mResolver;

    private DbResolverManager(Context context) {
        this.mContext = context;
        this.mResolver = context.getContentResolver();
    }

    public static synchronized DbResolverManager getInstance(Context context) {
        if (instance == null) {
            instance = new DbResolverManager(context);
        }
        return instance;
    }

    public Uri addUser(UserBean userBean) {
        Uri uri = Uri.parse(ADOLF_URI + "user");
        ContentValues values = new ContentValues();
        values.put("username", userBean.getUsername());
        values.put("sex", userBean.getSex());
        values.put("motto", userBean.getMotto() + System.currentTimeMillis());
        Uri newUri = mResolver.insert(uri, values);
        return newUri;
    }

    public int deleteUserById(int id) {
        if (findUserById(id) == null)
            return 0;
        Uri uri = Uri.parse(ADOLF_URI + "user/" + id);
        int delete = mResolver.delete(uri, null, null);
        return delete;
    }

    public int deleteUser(String where, String[] selectionArg) {
        Uri uri = Uri.parse(ADOLF_URI + "user");
        int delete = mResolver.delete(uri, where, selectionArg);
        return delete;
    }

    public int updateUserById(UserBean userBean) {
        if (userBean.getId() < 0) {
            Toast.makeText(mContext, "userId不正确", Toast.LENGTH_SHORT).show();
            return 0;
        }
        if (findUserById(userBean.getId()) == null) {
            Toast.makeText(mContext, "没有id为" + userBean.getId() + "的user", Toast.LENGTH_SHORT).show();
            return 0;
        }

        Uri uri = Uri.parse(ADOLF_URI + "user/" + userBean.getId());
        ContentValues values = new ContentValues();
        if (userBean.getSex() == 0 || userBean.getSex() == 1) {
            values.put("sex", userBean.getSex());
        }
        if (!TextUtils.isEmpty(userBean.getUsername())) {
            values.put("username", userBean.getUsername());
        }
        if (!TextUtils.isEmpty(userBean.getMotto())) {
            values.put("motto", userBean.getMotto());
        }
        int update = mResolver.update(uri, values, null, null);
        return update;
    }

    public Cursor findAllUser() {
        Uri uri = Uri.parse(ADOLF_URI + "user");
        Cursor cursor = mResolver.query(uri, null, null, null, null);
        return cursor;
    }

    public Cursor findUserById(int id) {
        Uri uri = Uri.parse(ADOLF_URI + "user/" + id);
        Cursor cursor = mResolver.query(uri, null, null, null, null);
        return cursor;
    }

}
