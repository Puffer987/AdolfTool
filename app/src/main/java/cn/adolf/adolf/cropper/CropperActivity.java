package cn.adolf.adolf.cropper;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;
import cn.dream.ebag.ebagcropper.HyperCameraActivity;

public class CropperActivity extends AppCompatActivity {

    @BindView(R.id.iv_display)
    ImageView mIvDisplay;
    private File outFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        ButterKnife.bind(this);

        outFile = new File(getExternalCacheDir(), "/" + System.currentTimeMillis() + ".jpg");
    }

    @OnClick(R.id.btn_camera)
    public void onViewClicked() {
        Intent intent = new Intent(this, HyperCameraActivity.class);
        intent.putExtra(HyperCameraActivity.EXTRA_OUTPUT_PATH, outFile.getAbsoluteFile());
        startActivityForResult(intent, 0x01);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case HyperCameraActivity.RESULT_CODE_OK: {
                mIvDisplay.setImageBitmap(BitmapFactory.decodeFile(outFile.getAbsolutePath()));
            }
            case HyperCameraActivity.RESULT_CODE_CANCEL: {
                mIvDisplay.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            }
            default: {
                mIvDisplay.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));
            }
        }
    }
}