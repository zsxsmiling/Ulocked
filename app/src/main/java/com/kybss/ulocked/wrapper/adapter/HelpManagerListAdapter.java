package com.kybss.ulocked.wrapper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.HelpRecords;

/**
 * Created by lrisfish on 2018/5/28.
 */

public class HelpManagerListAdapter extends BaseAdapter<HelpRecords> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_help_manager_item;
    }

    @Override
    public HelpManagerItemViewHolder createViewHolder(View view, int viewType) {
        return new HelpManagerItemViewHolder(view);
    }


    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, HelpRecords help, int position) {
        if (holder instanceof HelpManagerItemViewHolder) {
            ((HelpManagerItemViewHolder) holder).bind(help);
        }
    }
}
