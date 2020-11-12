package cn.adolf.adolf.wsp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

import cn.adolf.adolf.R;


/**
 * @program: LoveWidget
 * @description:
 * @author: Adolf
 * @create: 2020-11-06 11:34
 **/
public class AdolfRvAdapter extends RecyclerView.Adapter<AdolfRvAdapter.ViewHolder> implements BaseRvAdapterImpl {
    Context mContext;
    ArrayList<String> titleList = new ArrayList<>();

    OnStartDragListener mDragListener;

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public void setDragListener(OnStartDragListener dragListener) {
        mDragListener = dragListener;
    }

    public AdolfRvAdapter(Context context, ArrayList<String> titleList) {
        this.mContext = context;
        this.titleList = titleList;
    }

    public void addItem(int position, String title) {
        titleList.add(position, title);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, titleList.size() - position);
    }

    @Override
    public void delItem(int position) {
        titleList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, titleList.size() - position);// 刷新从第一个参数开始后的第二个参数个数的item
    }

    @Override
    public void moveItem(int from, int to) {
        Collections.swap(titleList, from, to);
        notifyItemMoved(from, to);
        notifyItemChanged(from);
        notifyItemChanged(to);
    }

    @Override
    public int getItemCount() {
        return titleList == null ? 0 : titleList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_anim, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tv.setText(titleList.get(position));
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, position + "", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    //通知ItemTouchHelper开始拖拽 
                    if (mDragListener != null)
                        mDragListener.onStartDrag(holder); // 需要调用外部ItemTouchHelper的startDrag方法
                }
                return false;
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder implements BaseRvAdapterImpl.ViewHolderImpl {
        TextView tv;
        RelativeLayout parent;
        ImageView btnDrag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.item_tv);
            parent = itemView.findViewById(R.id.item_parent);
            btnDrag = itemView.findViewById(R.id.btn_drag);
        }

        @Override
        public void onItemSelected() {
            parent.setSelected(true);
        }

        @Override
        public void onItemClear() {
            parent.setSelected(false);
        }
    }
}
