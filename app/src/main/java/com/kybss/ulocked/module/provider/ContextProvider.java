package com.kybss.ulocked.module.provider;


import android.content.Context;

import com.google.common.base.Preconditions;
import com.kybss.ulocked.module.qualifiers.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextProvider {

    private final Context mApplicationContext;
    public ContextProvider(Context context) {
        mApplicationContext = Preconditions.checkNotNull(context, "context cannot be null");
    }

    @Provides
    @ApplicationContext
     Context provideApplicationContext() {
        return mApplicationContext;
    }

}
