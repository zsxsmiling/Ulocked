package com.kybss.ulocked.base;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.kybss.ulocked.R;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.util.ContentView;
import com.kybss.ulocked.util.ToastUtil;
import com.kybss.ulocked.util.ViewEventListener;
import com.kybss.ulocked.widget.MultiStateView;
import com.kybss.ulocked.wrapper.LoadMoreWrapperAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;

import static android.content.Context.INPUT_METHOD_SERVICE;


@ContentView(R.layout.fragment_content_recyclerview)
public abstract class BaseDormFragment<T, UC> extends BaseFragment<UC>
        implements BaseController.ListUi<T, UC> {

    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

/*    @BindView(R.id.search)
    SearchView mSearchView;

    @BindView(R.id.setting)
    ImageView mSetting;

    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;*/

    protected LoadMoreWrapperAdapter<T> mAdapter;


    @Override
    protected void initializeViews(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mAdapter = new LoadMoreWrapperAdapter<>(getAdapter());
        mRefreshLayout.setEnabled(getEnableRefresh());
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshPage();
            }
        });
        mAdapter.setViewEventListener(new ViewEventListener<T>() {
            @Override
            public void onViewEvent(int actionId, T item, int position, View view) {
                onItemClick(actionId, item,position);
            }
        });
        mAdapter.setOnLoadMoreListener(new LoadMoreWrapperAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                nextPage();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        mMultiStateView.setState(getDefaultState());

/*        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
        mLlSearch.setFocusable(true);
        mLlSearch.setFocusableInTouchMode(true);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                search(s);
                hideInput();
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/
    }

    protected void search(String s){}

    protected void showMenu(){}

    protected void refreshPage() {}

    protected void nextPage() {}

    protected void onItemClick(int actionId, T item,int position) {}

    protected boolean getEnableRefresh() {
        return mMultiStateView.getState() == MultiStateView.STATE_CONTENT
                || mMultiStateView.getState() == MultiStateView.STATE_EMPTY;
    }

    @MultiStateView.State
    protected int getDefaultState() {
        return MultiStateView.STATE_LOADING;
    }

    protected abstract BaseAdapter<T> getAdapter();

    @DrawableRes
    protected int getEmptyIcon() {
        return R.drawable.ic_empty;
    }

    protected String getEmptyTitle() {
        return getString(R.string.label_empty_data);
    }

    protected boolean isDisplayError(ResponseError error) {
        return mMultiStateView.getState() != MultiStateView.STATE_CONTENT;
    }

    @DrawableRes
    protected int getErrorIcon(ResponseError error) {
        return R.drawable.ic_exception;
    }

    protected String getErrorTitle(ResponseError error) {
        return getString(R.string.label_error_network_is_bad);
    }

    protected String getErrorButton(ResponseError error) {
        return getString(R.string.label_click_button_to_retry);
    }

    protected void onRetryClick(ResponseError error) {
        mMultiStateView.setState(MultiStateView.STATE_LOADING);
        refreshPage();
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
    public void onFinishRequest(List<T> items, int page, boolean haveNextPage) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
        if (mAdapter.isLoading()) {
            mAdapter.finishLoadMore();
        }

        if (items != null && !items.isEmpty()) {
            if (page == 1) {
                mAdapter.setItems(items);
                mMultiStateView.setState(MultiStateView.STATE_CONTENT);
            } else {
                mAdapter.addItems(items);
            }
        } else {
            if (page == 1) {
                mMultiStateView.setState(MultiStateView.STATE_EMPTY)
                        .setIcon(getEmptyIcon())
                        .setTitle(getEmptyTitle());
            } else {
                ToastUtil.showToast(R.string.toast_error_not_have_more);
            }
        }

        mRefreshLayout.setEnabled(getEnableRefresh());
        mAdapter.setEnableLoadMore(haveNextPage);
    }

    @Override
    public void onResume() {
        super.onResume();
/*        mLlSearch.setFocusable(true);
        mLlSearch.setFocusableInTouchMode(true);*/
    }

    @Override
    public void onResponseError(final ResponseError error) {
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.finishRefresh();
        }
        if (mAdapter.isLoading()) {
            mAdapter.finishLoadMore();
        }

        if (isDisplayError(error)) {
            mMultiStateView.setState(MultiStateView.STATE_ERROR)
                    .setIcon(getErrorIcon(error))
                    .setTitle(getErrorTitle(error))
                    .setButton(getErrorButton(error), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onRetryClick(error);
                        }
                    });
        } else {
            ToastUtil.showToast(error.getMessage());
        }

        mRefreshLayout.setEnabled(getEnableRefresh());
    }


    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        View v = getActivity().getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}

