package com.kybss.ulocked.ui.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseActivity;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.UserController;
import com.kybss.ulocked.model.bean.DoorCheck;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.widget.MultiStateView;
import com.kybss.ulocked.wrapper.adapter.LockRecordListAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import java.util.List;
import butterknife.BindView;

/**
 * Created by Administrator on 2018\4\11 0011.
 */
@ContentView(R.layout.activity_lock_record)
public class LockRecordActivity extends BaseActivity<UserController.UserUiCallbacks>
        implements UserController.UserRecordUi {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    private LockRecordListAdapter mAdapter;
    @Override
    protected BaseController getController() {
        return AppContext.getContext().getMainController().getUserController();
    }
    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getCallbacks().refresh();
            }
        });

        mAdapter = new LockRecordListAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMultiStateView.setState(MultiStateView.STATE_LOADING);
    }
    @Override
    public void onStartRequest(int page) {
        if (mMultiStateView.getState() != MultiStateView.STATE_CONTENT) {
            mMultiStateView.setState(MultiStateView.STATE_LOADING);
        } else {
            mRefreshLayout.autoRefresh(0);
        }
    }

    @Override
    public void onFinishRequest(List<DoorCheck> items, int page, boolean haveNextPage) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }

        if (items != null && !items.isEmpty()) {
                mAdapter.setItems(items);
                mMultiStateView.setState(MultiStateView.STATE_CONTENT);

        } else {
                mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                        .setIcon(getEmptyIcon())
                        .setTitle(getEmptyTitle())
                        .setButton(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMultiStateView.setState(MultiStateView.STATE_LOADING);
                                getCallbacks().refresh();
                            }
                        });

        }

    }
    @Override
    public void onResponseError(ResponseError error) {
        cancelLoading();

        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }

        if (mMultiStateView.getState() != MultiStateView.STATE_CONTENT) {
            mMultiStateView.setState(MultiStateView.STATE_ERROR)
                    .setTitle(error.getMessage())
                    .setButton(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMultiStateView.setState(MultiStateView.STATE_LOADING);
                            getCallbacks().refresh();
                        }
                    });
        } else {
            ToastUtil.showToast(error.getMessage());
        }
    }
    @DrawableRes
    protected int getEmptyIcon() {
        return R.drawable.ic_empty;
    }
    protected String getEmptyTitle() {
        return getString(R.string.label_empty_data);
    }

}
