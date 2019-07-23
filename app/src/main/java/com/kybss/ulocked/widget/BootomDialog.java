package com.kybss.ulocked.widget;

import android.view.View;
import android.widget.RelativeLayout;

import com.kybss.ulocked.R;

import me.shaohui.bottomdialog.BaseBottomDialog;

public class BootomDialog extends BaseBottomDialog implements View.OnClickListener{

    private RelativeLayout mScan;
    private RelativeLayout mLoc;
    private OnViewClickListener listener;

    public BootomDialog setListener(OnViewClickListener mlistener){
        listener = mlistener;
        return this;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.bottom_dialog_layout;
    }

    @Override
    public void bindView(View v) {
        mScan = (RelativeLayout) v.findViewById(R.id.mRlScan);
        mLoc =(RelativeLayout) v.findViewById(R.id.mRlLoc);
        mLoc.setOnClickListener(this);
        mScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
        this.dismiss();
    }


    public interface OnViewClickListener{
        void onClick(View view);
    }
}
