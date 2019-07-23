package com.kybss.ulocked.base;


import com.kybss.ulocked.util.ViewEventListener;

public interface IViewHolder<T> {

    void setViewEventListener(ViewEventListener<T> viewEventListener);

    void setItem(T item);

    void setPosition(int position);
}
