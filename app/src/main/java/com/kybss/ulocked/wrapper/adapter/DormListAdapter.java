package com.kybss.ulocked.wrapper.adapter;



import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseAdapter;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.wrapper.holder.DormItemViewHolder;
import com.kybss.ulocked.wrapper.holder.MeetingItemViewHolder;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class DormListAdapter extends BaseAdapter<Meeting> {


    @Override
    public int getViewLayoutId(int viewType) {
        return R.layout.adapter_building_item;
    }

    @Override
    public DormItemViewHolder createViewHolder(View view, int viewType) {
        return new DormItemViewHolder(view);
    }


    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, Meeting meeting, int position) {
        if (holder instanceof DormItemViewHolder) {
            ((DormItemViewHolder) holder).bind(meeting);

        }
    }
/*    public interface OnItemClickListener{
         void onClick( int position);
         void onLongClick( int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
         this.mOnItemClickListener=onItemClickListener;
    }*/
public interface IItem {
    void setOnItem(int position);//接口中的方法
}

}
