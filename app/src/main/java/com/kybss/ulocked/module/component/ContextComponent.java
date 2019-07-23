package com.kybss.ulocked.module.component;



import android.content.Context;

import com.kybss.ulocked.module.provider.ContextProvider;
import com.kybss.ulocked.module.qualifiers.ApplicationContext;


import dagger.Component;

/**
 * Created by Administrator on 2018\4\5 0005.
 */
@Component(modules = ContextProvider.class)

public interface ContextComponent {
    @ApplicationContext
    Context proContext();
}
