package cn.adolf.adolf;

import android.app.Application;
import android.content.Context;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2021-01-22 11:18
 **/
public class App extends Application {

    public static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }
}
