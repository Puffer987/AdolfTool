package cn.adolf.adolf;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.cache.CacheActivity;
import cn.adolf.adolf.cropper.CropperActivity;
import cn.adolf.adolf.db.DbOpenTestActivity;
import cn.adolf.adolf.file.FileActivity;
import cn.adolf.adolf.intent.StartIntentActivity;
import cn.adolf.adolf.animRv.RvAdvanceActivity;
import cn.adolf.adolf.mediaPlay.MediaActivity;
import cn.adolf.adolf.notification.NotificationActivity;
import cn.adolf.adolf.pathAnim.PathAnimActivity;
import cn.adolf.adolf.widget.DaysWidgetConfigureActivity;
import cn.adolf.adolf.widget.WidgetActivity;
import cn.adolf.adolf.wsp2.ProgressActivity;
import cn.adolf.adolf.wsp2.VisibleListenerActivity;
import cn.adolf.adolf.db.DbMainActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String[] allPermission = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
    ArrayList<String> noGrantedPerm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPerm();
    }


    @OnClick({R.id.rv_advance, R.id.workspace, R.id.database_provider,
            R.id.start_intent, R.id.start_progress, R.id.start_pathanim,
            R.id.start_file, R.id.start_widget, R.id.start_notification,
            R.id.start_media, R.id.start_cache, R.id.start_open_db, R.id.start_cropper})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_advance:
                startActivity(new Intent(this, RvAdvanceActivity.class));
                break;
            case R.id.workspace:
                startActivity(new Intent(this, VisibleListenerActivity.class));
                break;
            case R.id.database_provider:
                startActivity(new Intent(this, DbMainActivity.class));
                break;
            case R.id.start_intent:
                startActivity(new Intent(this, StartIntentActivity.class));
                break;
            case R.id.start_progress:
                startActivity(new Intent(this, ProgressActivity.class));
                break;
            case R.id.start_pathanim:
                startActivity(new Intent(this, PathAnimActivity.class));
                break;
            case R.id.start_file:
                startActivity(new Intent(this, FileActivity.class));
                break;
            case R.id.start_widget:
                startActivity(new Intent(this, WidgetActivity.class));
                break;
            case R.id.start_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.start_media:
                startActivity(new Intent(this, MediaActivity.class));
                break;
            case R.id.start_cache:
                startActivity(new Intent(this, CacheActivity.class));
                break;
            case R.id.start_open_db:
                fileTest();
                startActivity(new Intent(this, DbOpenTestActivity.class));
                break;

            case R.id.start_cropper:
                fileTest();
                startActivity(new Intent(this, CropperActivity.class));
                break;
        }
    }

    private void fileTest() {
        String s = Environment.getExternalStorageDirectory() + "/A/AA/AAA/a.txt";
        File file = new File(s);
        if (!file.exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            Log.d(TAG, "mkdirs: " + mkdirs);
            try {
                boolean newFile = file.createNewFile();
                Log.d(TAG, "createNewFile: " + newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void checkPerm() {
        for (String perm : allPermission) {
            int i = ActivityCompat.checkSelfPermission(this, perm);
            if (i != PackageManager.PERMISSION_GRANTED) {
                noGrantedPerm.add(perm);
            }
        }
        if (noGrantedPerm.size() > 0) {
            ActivityCompat.requestPermissions(this, noGrantedPerm.toArray(new String[noGrantedPerm.size()]), 0x11);
        } else {
            Toast.makeText(this, "已有所需权限", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11) {
            int count = 0;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    count++;
                }
            }
            if (count == noGrantedPerm.size()) {
                Toast.makeText(this, "所需权限已全部允许", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "没有获得所需权限", Toast.LENGTH_SHORT).show();
            }
        }
    }
}