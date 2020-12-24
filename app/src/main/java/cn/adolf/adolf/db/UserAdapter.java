package cn.adolf.adolf.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.adolf.adolf.R;

/**
 * @program: Adolf
 * @description:
 * @author: yjq
 * @create: 2020-11-18 17:00
 **/
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<UserBean> mUserBeans;
    private Context mContext;

    public UserAdapter(List<UserBean> userBeans, Context context) {
        mUserBeans = userBeans;
        mContext = context;
    }

    public void modifyUserBeans(List<UserBean> userBeans){
        mUserBeans.clear();
        mUserBeans.addAll(userBeans);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mItemId.setText(mUserBeans.get(position).getId() + "");
        holder.mItemUsername.setText(mUserBeans.get(position).getUsername());
        holder.mItemSex.setText(mUserBeans.get(position).getSex() + "");
        holder.mItemMotto.setText(mUserBeans.get(position).getMotto());
    }

    @Override
    public int getItemCount() {
        return mUserBeans == null ? 0 : mUserBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_id)
        TextView mItemId;
        @BindView(R.id.item_username)
        TextView mItemUsername;
        @BindView(R.id.item_sex)
        TextView mItemSex;
        @BindView(R.id.item_motto)
        TextView mItemMotto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
