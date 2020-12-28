package cn.adolf.adolf.file;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class FileActivity extends AppCompatActivity {

    private static final String TAG = "FileActivity";
    @BindView(R.id.progress1)
    ProgressBar mProgress1;
    @BindView(R.id.tv_progress)
    TextView mTvProgress;
    @BindView(R.id.btn1)
    Button mBtn1;
    @BindView(R.id.btn2)
    Button mBtn2;
    @BindView(R.id.btn3)
    Button mBtn3;
    private String mSrcFileName;
    private String mOutFileName1;
    private String mOutFileName2;
    private String mOutFileName3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        ButterKnife.bind(this);

        mSrcFileName = Environment.getExternalStorageDirectory() + "/Adolf/src.db";
        mOutFileName1 = Environment.getExternalStorageDirectory() + "/Adolf/test1.db";
        mOutFileName2 = Environment.getExternalStorageDirectory() + "/Adolf/test2.db";
        mOutFileName3 = Environment.getExternalStorageDirectory() + "/Adolf/test3.db";
    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                new Thread(() -> {
                    long start = System.currentTimeMillis();
                    FileUtils.copyByBytes(mSrcFileName, mOutFileName1);
                    Log.e(TAG, "copyByBytes 消耗时间: " + (System.currentTimeMillis() - start));
                    mBtn1.post(()->mBtn1.setText((System.currentTimeMillis() - start)+"ms"));
                }).start();
                break;
            case R.id.btn2:
                new Thread(() -> {
                    long start = System.currentTimeMillis();
                    FileUtils.copyByChannel(mSrcFileName, mOutFileName2);
                    Log.e(TAG, "copyByChannel 消耗时间: " + (System.currentTimeMillis() - start));
                    mBtn2.post(()->mBtn2.setText((System.currentTimeMillis() - start)+"ms"));
                }).start();
                break;
            case R.id.btn3:
                mProgress1.setProgress(0);
                new Thread(() -> {
                    long start = System.currentTimeMillis();
                    copyWithProgress(new File(mSrcFileName), new File(mOutFileName3));
                    Log.e(TAG, "copyByChannel 消耗时间: " + (System.currentTimeMillis() - start));
                    mBtn3.post(()->mBtn3.setText((System.currentTimeMillis() - start)+"ms"));
                }).start();
                break;
        }
    }

    public boolean copyWithProgress(File srcFile, File outFile) {
        if (!srcFile.exists()) return false;
        File tempFile = new File(outFile.getAbsolutePath() + ".tmp");
        tempFile.mkdirs();

        InputStream in = null;
        OutputStream out = null;

        boolean isCopySuccess = false;

        try {
            if (tempFile.exists()) {
                tempFile.delete();
            }
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(tempFile);

            byte[] buf = new byte[4096];
            int i;
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
                mProgress1.post(() -> {
                    int curProgress = (int) (tempFile.length() * 100 / srcFile.length());
                    if (mProgress1.getProgress() != curProgress) {
                        mProgress1.setProgress(curProgress);
                        mTvProgress.setText(curProgress + "%");
                        mBtn3.setText(curProgress + "%");
                    }
                });
            }

            if (tempFile.exists()) {
                isCopySuccess = tempFile.length() == srcFile.length();
                tempFile.renameTo(outFile);
            }
        } catch (IOException e) {
            isCopySuccess = false;
            e.printStackTrace();
        } finally {
            FileUtils.close(in);
            FileUtils.close(out);
        }

        return isCopySuccess;
    }
}