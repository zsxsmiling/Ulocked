package com.kybss.ulocked.wrapper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.wrapper.holder.HelpItemViewHolder;

/**
 * Created by lrisfish on 2018/5/21.
 */

public class HelpListAdapter extends BaseAdapter<HelpRecords> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_help_item;
    }

    @Override
    public HelpItemViewHolder createViewHolder(View view, int viewType) {
        return new HelpItemViewHolder(view);
    }


    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, HelpRecords records, int position) {
        if (holder instanceof HelpItemViewHolder) {
            ((HelpItemViewHolder) holder).bind(records);
        }
    }

}
