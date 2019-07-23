package com.kybss.ulocked.wrapper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.wrapper.holder.BuildingListItemViewHolder;
import com.kybss.ulocked.wrapper.holder.MeetingActivitesItemViewHolder;

import java.util.List;

public class BuildingListAdapter extends BaseAdapter<Meeting> {

@Override
public int getViewLayoutId(int viewType) {
        return R.layout.adapter_building_item;
        }

@Override
public BuildingListItemViewHolder createViewHolder(View view, int viewType) {
        return new BuildingListItemViewHolder(view);
        }


@Override
public void bindViewHolder(RecyclerView.ViewHolder holder, Meeting floors, int position) {
        if (holder instanceof BuildingListItemViewHolder) {
        ((BuildingListItemViewHolder) holder).bind(floors);
        }
        }


}
