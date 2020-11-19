package cn.adolf.adolf.wsp3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-11-18 15:20
 **/
public class AdolfDbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "AdolfDatabase";
    private static final int VERSION = 1;
    private static final String CREATE_USER = "create table user(" +
            "id integer primary key autoincrement," +
            "username varchar(16)," +
            "sex integer," +
            "motto text)";

    public AdolfDbOpenHelper(Context context, String name) {
        this(context, name, null, VERSION);
    }

    public AdolfDbOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + "user");
        db.execSQL(CREATE_USER);
    }
}
