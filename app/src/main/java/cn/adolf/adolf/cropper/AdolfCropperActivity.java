package cn.adolf.adolf.cropper;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;
// import cn.dream.ebag.cropper2.HyperCameraActivity;
import cn.dream.ebag.ebagcropper.CropperCamera1Activity;

public class AdolfCropperActivity extends AppCompatActivity {

    private static final int REQUEST_CUSTOM_CAMERA_1 = 0x1002;


    @BindView(R.id.cropper_iv_display)
    ImageView mIvDisplay;
    private File outFile;
    private File mOrgImg;
    private File mCropImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cropper);
        ButterKnife.bind(this);

        outFile = new File(getExternalCacheDir(), "/" + System.currentTimeMillis() + ".jpg");
        mOrgImg = new File(getExternalCacheDir(), "/cropDir/origin.jpg");
        mCropImg = new File(getExternalCacheDir(), "/cropDir/cropped.jpg");
    }

    @OnClick(R.id.btn_ad)
    public void onViewClicked() {
        // Intent intent = new Intent(this, HyperCameraActivity.class);
        // intent.putExtra(HyperCameraActivity.EXTRA_OUTPUT_PATH, outFile.getAbsoluteFile());
        // startActivityForResult(intent, 0x01);

        Intent i = new Intent(this, CropperCamera1Activity.class);
        i.putExtra(CropperCamera1Activity.CROP_PATH, mCropImg.getAbsolutePath());
        i.putExtra(CropperCamera1Activity.ORG_PATH, mOrgImg.getAbsolutePath());
        startActivityForResult(i, REQUEST_CUSTOM_CAMERA_1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // switch (requestCode) {
        //     case HyperCameraActivity.RESULT_CODE_OK: {
        //         mIvDisplay.setImageBitmap(BitmapFactory.decodeFile(outFile.getAbsolutePath()));
        //     }
        //     case HyperCameraActivity.RESULT_CODE_CANCEL: {
        //         mIvDisplay.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //     }
        //     default: {
        //         mIvDisplay.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        //     }
        // }
        Log.d("TAG", "onActivityResult: "+requestCode);
        switch (requestCode) {
            case REQUEST_CUSTOM_CAMERA_1:
                if (resultCode == RESULT_OK) {
                    if (mCropImg.exists()) {
                        mIvDisplay.setImageBitmap(BitmapFactory.decodeFile(mCropImg.getAbsolutePath()));
                    }
                } else {
                    mIvDisplay.setVisibility(View.GONE);
                    Toast.makeText(this, "未成功.", Toast.LENGTH_SHORT).show();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}