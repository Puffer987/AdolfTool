package cn.adolf.adolf.wsp2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;


/**
 * 1. 使用tint和tintModel来控制颜色叠加，分为src和background
 */
public class VisibleListenerActivity extends AppCompatActivity {

    private static final String TAG = "WorkspaceActivity";
    @BindView(R.id.btn_switch)
    Button mBtnSwitch;
    @BindView(R.id.img_1)
    ImageView mImgTest;
    @BindView(R.id.root_layout)
    RelativeLayout mRootLayout;
    @BindView(R.id.group_img)
    LinearLayout mGroupImg;


    ViewTreeObserver mTreeObserver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visible_listener);
        ButterKnife.bind(this);


        // mImgTest.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
        //     @Override
        //     public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //         if (mImgTest.isShown()) {
        //             Toast.makeText(WorkspaceActivity.this, "可见", Toast.LENGTH_SHORT).show();
        //         } else { // 不显示这句
        //             Toast.makeText(WorkspaceActivity.this, "消失了", Toast.LENGTH_SHORT).show();
        //         }
        //     }
        // });

        mTreeObserver = mGroupImg.getViewTreeObserver();
        mTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mImgTest.isShown()) {
                    Toast.makeText(VisibleListenerActivity.this, "可见", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VisibleListenerActivity.this, "消失了", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @OnClick(R.id.btn_switch)
    public void onViewClicked() {
        Log.d(TAG, "mImgTest isShown: " + mImgTest.isShown());
        Log.d(TAG, "mImgTest visible: " + (mImgTest.getVisibility() == View.VISIBLE));
        mImgTest.setVisibility(mImgTest.isShown() ? View.GONE : View.VISIBLE);
    }
}