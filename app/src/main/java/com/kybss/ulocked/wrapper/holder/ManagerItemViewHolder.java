package com.kybss.ulocked.wrapper.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ACTIVITY_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_MANAGER_CLICKED;

/**
 * Created by Administrator on 2018\4\25 0025.
 */

public class ManagerItemViewHolder extends BaseViewHolder<Meeting> {

    //会议室房间号
    @BindView(R.id.item_manager_meeting_number)
    TextView mManagerMeetingNumberTxt;

    //状态
    @BindView(R.id.item_manager_door_state)
    TextView mManagerDoorStateTxt;

    //主题
    @BindView(R.id.item_manager_meeting_topic)
    TextView mManagerTopicTxt;

    //申请人
    @BindView(R.id.item_manager_username)
    TextView mUserNameTxt;

    @BindView(R.id.manager_item)
    LinearLayout item;


    public ManagerItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Meeting meeting) {

        /*mManagerMeetingNumberTxt.setText(StringFetcher.getString(R.string.door_number,
                meeting.getR_id()));*/
/*        for(int i=0;i<meeting.getResults().size();i++){
            mManagerMeetingNumberTxt.setText(StringUtil.descriptionToshow(meeting.getResults().get(i).getDescription()));
        }
        if(meeting.getResults().size()==0) mManagerMeetingNumberTxt.setText("前工院403");*/
        mManagerMeetingNumberTxt.setText(meeting.getDescription());
        mManagerTopicTxt.setText(StringFetcher.getString(R.string.meeting_topic,meeting.getTheme()));
        mUserNameTxt.setText(StringFetcher.getString(R.string.apply_user,
                meeting.getUser_id()));
        mManagerDoorStateTxt.setText(StringUtil.decodeStatus(meeting.getStatus()));
    }

    @OnClick(R.id.manager_item)
    public void onClick(View view) {
        notifyItemAction(CLICK_TYPE_ROOM_MANAGER_CLICKED);
    }
}
