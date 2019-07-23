package com.kybss.ulocked.wrapper.adapter;



import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.wrapper.holder.ManagerItemViewHolder;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class ManagerListAdapter extends BaseAdapter<Meeting> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_manager_item;
    }

    @Override
    public ManagerItemViewHolder createViewHolder(View view, int viewType) {
        return new ManagerItemViewHolder(view);
    }


    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Meeting meeting, int position) {
        if (holder instanceof ManagerItemViewHolder) {
            ((ManagerItemViewHolder) holder).bind(meeting);
        }
    }


}
