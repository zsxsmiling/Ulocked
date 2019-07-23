package com.kybss.ulocked.wrapper.adapter;



import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.wrapper.holder.MeetingActivitesItemViewHolder;
import com.kybss.ulocked.wrapper.holder.MeetingItemViewHolder;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class MeetingActivityListAdapter extends BaseAdapter<Meeting> {

    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_activity_item;
    }

    @Override
    public MeetingActivitesItemViewHolder createViewHolder(View view, int viewType) {
        return new MeetingActivitesItemViewHolder(view);
    }


    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Meeting meeting, int position) {
        if (holder instanceof MeetingActivitesItemViewHolder) {
            ((MeetingActivitesItemViewHolder) holder).bind(meeting);
        }
    }


}
