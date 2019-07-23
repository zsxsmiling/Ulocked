package com.kybss.ulocked.wrapper.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.DoorCheck;
import com.kybss.ulocked.wrapper.holder.LockRecordItemViewHolder;

/**
 * Created by Administrator on 2018\4\11 0011.
 */public class LockRecordListAdapter extends BaseAdapter<DoorCheck>{
    @Override
    public int getViewLayoutId(int viewType) {
        Log.e("ViewType",""+viewType);
        return R.layout.adapter_lock_record_item;
    }

    @Override
    public LockRecordItemViewHolder createViewHolder(View view, int viewType) {
        return new LockRecordItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, DoorCheck doorCheck, int position) {
        if (holder instanceof LockRecordItemViewHolder) {
            ((LockRecordItemViewHolder) holder).bind(doorCheck);
        }
    }
}
