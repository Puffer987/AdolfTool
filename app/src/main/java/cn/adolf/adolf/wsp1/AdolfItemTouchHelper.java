package cn.adolf.adolf.wsp1;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @program: LoveWidget
 * @description: 控制item的相关动作，滑动，拖拽
 * @author: Adolf
 * @create: 2020-11-09 17:16
 **/
public class AdolfItemTouchHelper extends ItemTouchHelper.Callback {

    private BaseRvAdapterImpl mAdapter;

    public AdolfItemTouchHelper(BaseRvAdapterImpl adapter) {
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    // @Override
    // public boolean isLongPressDragEnabled() {
    //     return true;
    // }
    //
    // @Override
    // public boolean isItemViewSwipeEnabled() {
    //     return true;
    // }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.delItem(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true; // 默认为true
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder,actionState);
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE){ // 不为空闲态，即拖拽或滑动中
            if (viewHolder instanceof BaseRvAdapterImpl.ViewHolderImpl){
                ((BaseRvAdapterImpl.ViewHolderImpl) viewHolder).onItemSelected();
            }
        }
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder instanceof BaseRvAdapterImpl.ViewHolderImpl){
            ((BaseRvAdapterImpl.ViewHolderImpl) viewHolder).onItemClear();
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }
}
