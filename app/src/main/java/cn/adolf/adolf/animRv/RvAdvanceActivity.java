package cn.adolf.adolf.animRv;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.adolf.adolf.R;

public class RvAdvanceActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.rv_anim)
    RecyclerView mRvAnim;

    private AdolfRvAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    int i = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_advance);
        ButterKnife.bind(this);

        setRvData();
    }

    @OnClick(R.id.tv)
    public void onViewClicked() {
    }

    private void setRvData() {
        ArrayList<String> titleList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            titleList.add("No." + i);
        }

        mRvAnim.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AdolfRvAdapter(this, titleList);
        mRvAnim.setAdapter(mAdapter);

        mAdapter.setDragListener(new AdolfRvAdapter.OnStartDragListener() {
            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                mItemTouchHelper.startDrag(viewHolder);
            }
        });

        // 以下两行防止调用notifyItemRangeChanged时闪一下更新的item
        // ((SimpleItemAnimator) mRvAnim.getItemAnimator()).setSupportsChangeAnimations(false);
        // mRvAnim.getItemAnimator().setChangeDuration(0);

        final AdolfRvItemAnim adolfRvItemAnim = new AdolfRvItemAnim();
        // rvItemAnim.setAddDuration(500);
        // rvItemAnim.setRemoveDuration(500);
        // defaultItemAnimator.setChangeDuration(0); // 会导致item更新时底部item没有向上移动的动画
        adolfRvItemAnim.setSupportsChangeAnimations(false); // 防止更新时item闪
        mRvAnim.setItemAnimator(adolfRvItemAnim);


        // 实现长按拖曳排序和滑动删除
        mItemTouchHelper = new ItemTouchHelper(new AdolfItemTouchHelper(mAdapter));
        mItemTouchHelper.attachToRecyclerView(mRvAnim);

        mTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.addItem(0, "new" + i++);
            }
        });
    }
}