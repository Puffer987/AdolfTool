package cn.adolf.adolf.intent;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class StartIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_intent);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_action, R.id.btn_cat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_action:
                Intent intent = new Intent();
                intent.setAction("666");

                intent = Intent.createChooser(intent, "每次都让你弹出");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
            case R.id.btn_cat:
                Intent i = new Intent();
                i.setClassName("cn.adolf.adolftest", "cn.adolf.adolftest.intent.Intent1Activity");
                // ComponentName comp = new ComponentName("cn.adolf.adolftest","cn.adolf.adolftest.intent.Intent1Activity");
                // i.setComponent(comp);
                startActivity(i);
                break;
        }
    }
}