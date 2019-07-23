package com.kybss.ulocked.wrapper.holder;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.Door;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.event.ItemSelectedEvent;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringToDate;
import com.kybss.ulocked.util.StringUtil;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.context.AppContext.getContext;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ACTIVITY_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_EAST_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_WEST_CLICKED;

/**
 * Created by Administrator on 2018\4\25 0025.
 */

public class MeetingActivitesItemViewHolder extends BaseViewHolder<Meeting> {


    //会议主题
    @BindView(R.id.meeting_topic)
    TextView mMeetingTopic;

    @BindView(R.id.item_activity_time)
    TextView mActivityTimeTxt;

    //地点-房间号
    @BindView(R.id.item_activity_location)
    TextView mActivityLocationTxt;

    @BindView(R.id.show_door_state)
    TextView mDoorStateTxt;

    @BindView(R.id.activity_item)
    RelativeLayout item;

    @BindView(R.id.west_door_btn)
    ImageButton mWestDoorBtn;

    @BindView(R.id.east_door_btn)
    ImageButton mEastDoorBtn;


    @BindView(R.id.spinner)
    Spinner mSpinner;

    public MeetingActivitesItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Meeting meeting) {

        init_spinner(meeting);

        mMeetingTopic.setText(StringFetcher.getString(R.string.meeting_topic,meeting.getTheme()));
        Log.e("SHIJIAN",meeting.getStart_time());
        mActivityTimeTxt.setText(meeting.getStart_time());
        mActivityTimeTxt.setText(DateUtil.getStampDate(meeting.getStart_time())
                +"  "+DateUtil.getStampTime(meeting.getStart_time()));
        for(int i=0;i<meeting.getResults().size();i++){
        mActivityLocationTxt.setText(StringUtil.descriptionToshow(meeting.getResults().get(i).getDescription()));
        }
        if(meeting.getResults().size()==0) mActivityLocationTxt.setText("此门暂未使用");

        mDoorStateTxt.setText(StringUtil.decodeStatus(meeting.getStatus()));
        if(!meeting.getStatus().equals("1")){
            mSpinner.setVisibility(View.INVISIBLE);
        }
    }

    private void init_spinner(final Meeting meeting) {
       List<String> data_list = new ArrayList<String>();
       final List<Door> results = meeting.getResults();
        data_list.add("门禁");
       for(int i=0;i<results.size();i++){
           data_list.add(StringUtil.descriptionToId(results.get(i).getDescription()));
           Log.e("des",results.get(i).getDescription());
       }
        ArrayAdapter<String> arr_adapter=
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSpinner.setAdapter(arr_adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    EventUtil.sendEvent(new ItemSelectedEvent(results.get(i-1).getMAC(),results.get(i-1).getD_id()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
       /* mSpinner.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {
            }

            @Override
            public void onChildViewRemoved(View view, View view1) {
                try {
                    Class<?> clazz = AdapterView.class;
                    Field mOldSelectedPosition = clazz.getDeclaredField("mOldSelectedPosition");
                    Field mSelectedPosition = clazz.getDeclaredField("mSelectedPosition");
                    mOldSelectedPosition.setAccessible(true);
                    mSelectedPosition.setAccessible(true);
                    if (mOldSelectedPosition.getInt(mSpinner) == mSelectedPosition.getInt(mSpinner)) {
                        //响应事件
                        System.out.println("mSelectedPosition" + mSelectedPosition.getInt(mSpinner));
                        int position =  mSelectedPosition.getInt(mSpinner);
                        if(position>0){
                            EventUtil.sendEvent(new ItemSelectedEvent(results.get(position-1).getMAC(),results.get(position-1).getD_id()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/

    }

    @OnClick({
            R.id.activity_item,
            R.id.west_door_btn,
            R.id.east_door_btn
    })
    public void onClick(View view) {
        switch (view.getId()){
            case  R.id.activity_item:
                notifyItemAction(CLICK_TYPE_ROOM_ACTIVITY_CLICKED);
                break;
            case  R.id.west_door_btn:
                notifyItemAction(CLICK_TYPE_ROOM_WEST_CLICKED);
                break;
            case  R.id.east_door_btn:
                notifyItemAction(CLICK_TYPE_ROOM_EAST_CLICKED);
                break;

        }

    }
}
