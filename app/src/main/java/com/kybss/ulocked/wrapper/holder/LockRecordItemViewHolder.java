package com.kybss.ulocked.wrapper.holder;

import android.view.View;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseViewHolder;
import com.kybss.ulocked.model.bean.DoorCheck;

import butterknife.BindView;

/**
 * Created by Administrator on 2018\4\11 0011.
 */

public class LockRecordItemViewHolder extends BaseViewHolder<DoorCheck> {
    @BindView(R.id.check_door_id)
    TextView door_check_id;

    @BindView(R.id.check_door_time)
    TextView door_check_time;

    @BindView(R.id.check_user_id)
    TextView check_user_id;

    @BindView(R.id.check_door_random)
    TextView door_random;

    @BindView(R.id.check_door_num)
    TextView door_num;

    @BindView(R.id.check_door_state)
    TextView door_check_state;


    public LockRecordItemViewHolder(View view) {
        super(view);
    }

    public void bind(DoorCheck doorCheck) {
        door_check_id.setText("序号：" + doorCheck.getId());
        door_check_time.setText("" + doorCheck.getCheck_time());
        check_user_id.setText("用户编号" + doorCheck.getUser_id());
        door_random.setText("" + doorCheck.getRand_num());
        door_num.setText("" + doorCheck.getDoor_num());
        door_check_state.setText(doorCheck.getStatus() == 0 ? "正常" : "异常");

    }
}
