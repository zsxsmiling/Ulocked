package com.kybss.ulocked.wrapper.holder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;

import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_MANAGER_CLICKED;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_ROOM_ORDER_CLICKED;

/**
 * Created by lrisfish on 2018/5/21.
 */

public class HelpItemViewHolder extends BaseViewHolder<HelpRecords> {

    //会议室房间号
    @BindView(R.id.door_number_help)
    TextView mDoorNumberTxt;

    //图标上
    @BindView(R.id.state_using_help)
    ImageView mStateUseImg;
    //图标下
    @BindView(R.id.state_order_help)
    ImageView mStateOrderImg;

    //帮助按钮
    @BindView(R.id.help_order)
    ImageButton mOrderBtn;

    public HelpItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(HelpRecords records) {
        mDoorNumberTxt.setText(records.getHelper());

    }

    @OnClick(R.id.help_order)
    public void onClick(View view) {
       // notifyItemAction(CLICK_TYPE_ROOM_ORDER_CLICKED);
    }
}

