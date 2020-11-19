package cn.adolf.adolftest;

import android.content.Context;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-11-19 08:45
 **/
public class DatabaseManager {

    private static DatabaseManager instance; // 懒汉，在需要使用时才实例化
    // private static DatabaseManager instance= new DatabaseManager(context); // 饿汉，初始化时就实例化
    private Context mContext;

    private DatabaseManager(Context context) {
        this.mContext = context;
    }

    public static synchronized DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }
}
