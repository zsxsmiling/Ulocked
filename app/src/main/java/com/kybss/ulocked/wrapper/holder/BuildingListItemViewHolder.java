package com.kybss.ulocked.wrapper.holder;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.Door;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.event.ItemSelectedEvent;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.context.AppContext.getContext;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ACTIVITY_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_EAST_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_WEST_CLICKED;

public class BuildingListItemViewHolder extends BaseViewHolder<Meeting> {




    //地点-房间号
    @BindView(R.id.item_activity_location)
    TextView mActivityLocationTxt;

    @BindView(R.id.activity_item)
    RelativeLayout item;

    @BindView(R.id.spinner)
    Spinner mSpinner;

    public BuildingListItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(Meeting  floors) {

        init_spinner(floors);


            mActivityLocationTxt.setText(floors.getDescription());


    }

    private void init_spinner(final Meeting  floors) {
        List<String> floor_list = new ArrayList<String>();
        floor_list.add("层数选择");

            for(int i=0;i<floors.getFloor();i++){
            floor_list.add(String.valueOf(i));

        }
        ArrayAdapter<String> arr_adapter=
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, floor_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        mSpinner.setAdapter(arr_adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
              //      EventUtil.sendEvent(new ItemSelectedEvent(results.get(i-1).getMAC(),results.get(i-1).getD_id()));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


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
