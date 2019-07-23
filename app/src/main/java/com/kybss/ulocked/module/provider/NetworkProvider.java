package com.kybss.ulocked.module.provider;

import android.content.Context;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.module.qualifiers.ApplicationContext;
import com.kybss.ulocked.module.qualifiers.CacheDirectory;
import com.kybss.ulocked.module.qualifiers.ShareDirectory;
import com.kybss.ulocked.network.RestApiClient;
import com.kybss.ulocked.util.Constants;
import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class NetworkProvider {

    @Provides
    @Singleton
    public RestApiClient provideRestApiClient(@CacheDirectory File cacheLocation, @ApplicationContext Context context) {
        RestApiClient restApiClient = new RestApiClient(context, cacheLocation);
        if (AppCookie.isLoggin()) {
            restApiClient.setToken(AppCookie.getAccessToken());
        }
        return restApiClient;
    }

    @Provides
    @Singleton @CacheDirectory
    public File provideHttpCacheLocation(@ApplicationContext Context context) {
        return context.getCacheDir();
    }

    @Provides
    @Singleton @ShareDirectory
    public File provideShareLocation(@ApplicationContext Context context) {
        return new File(context.getFilesDir(), Constants.Persistence.SHARE_FILE);
    }
}
