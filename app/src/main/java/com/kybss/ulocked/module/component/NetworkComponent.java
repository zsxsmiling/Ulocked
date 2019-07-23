package com.kybss.ulocked.module.component;

import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.module.provider.NetworkProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2018\4\5 0005.
 */
@Component(modules = NetworkProvider.class,dependencies = ContextComponent.class)
@Singleton
public interface NetworkComponent {
    void inject(AppContext appContext);
}
