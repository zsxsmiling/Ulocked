package com.kybss.ulocked.wrapper.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.util.StringFetcher;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_BUILDING_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ORDER_CLICKED;

/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class MeetingItemViewHolder extends BaseViewHolder<Meeting>{

    //会议室编号
    @BindView(R.id.door_number)
    TextView mDoorNuberTxt;

    //图标上
    @BindView(R.id.state_using)
    ImageView mStateUseImg;
    //图标下
    @BindView(R.id.state_order)
    ImageView mStateOrderImg;

    //预约按钮
    @BindView(R.id.bt_order)
    ImageButton mOrderBtn;



    public MeetingItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Meeting meeting) {
        /*mDoorNuberTxt.setText(StringFetcher.getString(R.string.door_number,
                meeting.getDescription()));*/
        mDoorNuberTxt.setText(meeting.getDescription());
        if(meeting.getStatus() == null ){
            mStateOrderImg.setBackgroundResource(R.drawable.ic_door_useing);
            mStateUseImg.setBackgroundResource(R.drawable.ic_order);
        }else {
            mStateOrderImg.setBackgroundResource(meeting.getStatus().equals("1")?
                    R.drawable.ic_door_useing : R.drawable.ic_door_not_use);
            mStateUseImg.setBackgroundResource(meeting.getStatus().equals("2")?
                    R.drawable.ic_order :R.drawable.ic_noorder);
        }

    }

    @OnClick({R.id.bt_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_order:
                notifyItemAction(CLICK_TYPE_ROOM_ORDER_CLICKED);
                break;

        }
    }

}


