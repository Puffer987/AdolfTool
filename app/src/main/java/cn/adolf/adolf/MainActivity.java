package cn.adolf.adolf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.wsp1.RvAdvanceActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_advance)
    Button mRvAdvance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rv_advance)
    public void onViewClicked() {
        startActivity(new Intent(this, RvAdvanceActivity.class));
    }
}