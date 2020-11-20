package cn.adolf.adolf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.intent.StartIntentActivity;
import cn.adolf.adolf.wsp1.RvAdvanceActivity;
import cn.adolf.adolf.wsp2.WorkspaceActivity;
import cn.adolf.adolf.wsp3.DbMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rv_advance, R.id.workspace, R.id.database_provider, R.id.start_intent})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_advance:
                startActivity(new Intent(this, RvAdvanceActivity.class));
                break;
            case R.id.workspace:
                startActivity(new Intent(this, WorkspaceActivity.class));
                break;
            case R.id.database_provider:
                startActivity(new Intent(this, DbMainActivity.class));
                break;
            case R.id.start_intent:
                startActivity(new Intent(this, StartIntentActivity.class));
                break;

        }
    }
}