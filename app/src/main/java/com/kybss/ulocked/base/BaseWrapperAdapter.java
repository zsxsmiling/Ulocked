package com.kybss.ulocked.base;

import java.util.List;

public abstract class BaseWrapperAdapter<T> extends BaseAdapter<T> {

    public static final int ITEM_TYPE_LOAD_MORE = 100002;

    protected BaseAdapter<T> mInnerAdapter;

    protected BaseWrapperAdapter(BaseAdapter<T> adapter) {
        mInnerAdapter = adapter;
    }

    @Override
    public void setItems(List<T> items) {
        mInnerAdapter.setItems(items);
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<T> items) {
        mInnerAdapter.addItems(items);
        notifyDataSetChanged();
    }

    @Override
    public void addItem(T item) {
        mInnerAdapter.addItem(item);
        notifyDataSetChanged();
    }

    @Override
    public void delItem(T item) {
        mInnerAdapter.delItem(item);
        notifyDataSetChanged();
    }

    @Override
    public void clearItems() {
        mInnerAdapter.clearItems();
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        if (position < mInnerAdapter.getItemCount()) {
            return mInnerAdapter.getItem(position);
        }
        return null;
    }
}