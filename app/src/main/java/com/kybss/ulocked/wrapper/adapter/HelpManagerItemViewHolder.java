package com.kybss.ulocked.wrapper.adapter;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.StringFetcher;
import butterknife.BindView;
import butterknife.OnClick;
import static com.kybss.ulocked.util.Constants.ClickType.CLICK_TYPE_HELP_MANAGER_CLICKED;


/**
 * Created by lrisfish on 2018/5/28.
 */

public class HelpManagerItemViewHolder extends BaseViewHolder<HelpRecords> {

    @BindView(R.id.item_manager_meeting_number)
    TextView mDoorNumberTxt;

    @BindView(R.id.item_manager_meeting_topic)
    TextView mHelpReasonTxt;

    @BindView(R.id.item_helper_username)
    TextView mHelpUsrNameTxt;

    @BindView(R.id.item_help_data)
    TextView mHelpDataTxt;

    @BindView(R.id.manager_item)
    LinearLayout item;

    public HelpManagerItemViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(HelpRecords help) {
        mDoorNumberTxt.setText(StringFetcher.getString(R.string.help_door_number,
                help.getId()));
        mHelpReasonTxt.setText(help.getReason());
        mHelpUsrNameTxt.setText(help.getHelper());
        mHelpDataTxt.setText(DateUtil.getStampDate(help.getCreated_at()));
    }

    @OnClick(R.id.manager_item)
    public void onClick(View view) {
        notifyItemAction(CLICK_TYPE_HELP_MANAGER_CLICKED);
    }
}
