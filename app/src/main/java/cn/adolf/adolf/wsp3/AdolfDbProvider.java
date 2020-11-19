package cn.adolf.adolf.wsp3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-11-19 10:12
 **/
public class AdolfDbProvider extends ContentProvider {
    private static final String TAG = "AdolfDbProvider";

    private static UriMatcher uriMatcher;
    private static final String AUTHORITY = "cn.adolf.db.provider";

    private static final int USER_DIR = 1;
    private static final int USER_ITEM = 11;
    private AdolfDbOpenHelper mDbOpenHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "user", USER_DIR);// 对应vnd.android.cursor.dir ; uri到表结束
        uriMatcher.addURI(AUTHORITY, "user/#", USER_ITEM); // 对应vnd.android.cursor.item ; uri到表中id结束
    }

    @Override
    public boolean onCreate() {
        mDbOpenHelper = new AdolfDbOpenHelper(getContext(), AdolfDbOpenHelper.DB_NAME);
        return true; // true: 表示provider初始化成功
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        String[] param = matchParam(uri);
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        if (!TextUtils.isEmpty(param[0]) && !TextUtils.isEmpty(param[1])) {
            cursor = db.query(param[0], projection, "id=?", new String[]{param[1]}, null, null, sortOrder);
        } else if (TextUtils.isEmpty(param[1])) {
            cursor = db.query(param[0], projection, selection, selectionArgs, null, null, sortOrder);
        } else {
            return null;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                return "vnd.android.cursor.dir/vnd.cn.adolf.db.provider.user";
            case USER_ITEM:
                return "vnd.android.cursor.item/vnd.cn.adolf.db.provider.user";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        String[] param = matchParam(uri);
        long newId = db.insert(param[0], null, values);

        return Uri.parse("content://" + AUTHORITY + "/" + param[0] + "/" + newId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        String[] param = matchParam(uri);
        if (!TextUtils.isEmpty(param[0]) && !TextUtils.isEmpty(param[1])) {
            return db.delete(param[0], "id=?", new String[]{param[1]});
        } else if (TextUtils.isEmpty(param[1])) {

            return db.delete(param[0], selection, selectionArgs);
        }

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        String[] param = matchParam(uri);
        if (!TextUtils.isEmpty(param[0]) && !TextUtils.isEmpty(param[1])) {
            return db.update(param[0], values, "id=?", new String[]{param[1]});
        } else if (TextUtils.isEmpty(param[1])) {
            return db.update(param[0], values, selection, selectionArgs);
        }

        return 0;
    }

    private String[] matchParam(Uri uri) {
        String[] param = new String[]{null, null};
        switch (uriMatcher.match(uri)) {
            case USER_DIR:
                param[0] = "user";
                break;
            case USER_ITEM:
                param[0] = "user";
                param[1] = uri.getPathSegments().get(1); // “/”为分隔符，第一个“/”右边元素下标为0；“//”不算
                break;
        }
        return param;
    }
}
