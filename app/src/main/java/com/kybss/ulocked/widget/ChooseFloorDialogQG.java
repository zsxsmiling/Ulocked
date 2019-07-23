package com.kybss.ulocked.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.kybss.ulocked.R;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class ChooseFloorDialogQG extends Dialog implements View.OnClickListener {

    private List<Meeting> floors;
    private OnConfirmListener listener;
    private TextView mCancelTxt,mSubmitTxt;
    private TextView mMaxfloor;
    private Spinner mSpinner;
    private EditText mFloorNum;



    private String curr_building;
    private String curr_floor;
    private int maxfloor;
    public ChooseFloorDialogQG(Context context){
        super(context);
    }

    public ChooseFloorDialogQG(Context context, int themeResId, String content){
        super(context, themeResId);
    }

    public ChooseFloorDialogQG(Context context, OnConfirmListener confirmListener){
        super(context);
        this.listener = confirmListener;
    }

    public ChooseFloorDialogQG setDate(List<Meeting> floors){
        this.floors = floors;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_floor_choose);
        mCancelTxt = findViewById(R.id.cancel);
        mSubmitTxt = findViewById(R.id.submit);
        mSubmitTxt.setOnClickListener(this);
        mCancelTxt.setOnClickListener(this);
        mMaxfloor = findViewById(R.id.max_floor);
        mSpinner = findViewById(R.id.spinner);
        mFloorNum = findViewById(R.id.floor);
        initSpinner();

    }

    private void initSpinner() {
        List<String> floor_list = new ArrayList<>();
        floor_list.add("前工院");
/*        for(int i=0;i<floors.size();i++){
            floor_list.add(floors.get(i).getDescription());
        }*/

         ArrayAdapter<String> arr_adapter=
                new ArrayAdapter<String>(getContext(), R.layout.spinner_item, floor_list);
        arr_adapter.setDropDownViewResource(R.layout.spinner_item);
        mSpinner.setAdapter(arr_adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                 //   Floor floor = floors.get(i-1);
                    curr_building = "GQY";
                    maxfloor = 5;
                    mMaxfloor.setText("（可选1-"+maxfloor+"）");
                }else{
                    curr_building = "QGY";
                    maxfloor = 5;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancel:
                this.dismiss();
                break;
            case R.id.submit:
                if(EmptyFloor()){
                    return;
                }
                if(listener != null){
                    listener.onClick(getBuilding(),getFloor());
                    this.dismiss();
                }
                break;
        }

    }


    private boolean EmptyFloor(){
        String _floor = mFloorNum.getText().toString().trim();
        Log.e("fl",_floor);
        if(TextUtils.isEmpty(curr_building)){
            ToastUtil.showToast("请选择教学楼");
            return true;
        }
        if(TextUtils.isEmpty(_floor)){
            ToastUtil.showToast("请填写楼层");
            return true;
        }else if(Integer.parseInt(_floor)> maxfloor || Integer.parseInt(_floor)<=0){
            ToastUtil.showToast("楼层信息错误");
            return true;
        } else{
            curr_floor = _floor;
            return false;
        }
    }

    public String getFloor(){
        return this.curr_floor;
    }
    public String getBuilding(){
        return this.curr_building;
    }

    public interface OnConfirmListener{
        void onClick(String curr_building, String curr_floor);
    }
}
