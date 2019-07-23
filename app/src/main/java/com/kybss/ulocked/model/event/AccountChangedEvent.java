package com.kybss.ulocked.model.event;


import com.kybss.ulocked.model.bean.User;

/**
 * author：cheikh.wang on 16/7/18 14:21
 * email：wanghonghi@126.com
 */
public class AccountChangedEvent {

    private User user;

    public AccountChangedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}