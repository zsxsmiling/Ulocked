package com.kybss.ulocked.network.service;

import com.kybss.ulocked.model.bean.Help;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.model.bean.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018\5\23 0023.
 */

public interface HelperService {
    @FormUrlEncoded
    @POST("door/help")
    Observable<Help> requestHelp(@FieldMap Map<String, String> params);


    @POST("door/helpack")
    @Headers({"Content-Type:application/json"})
    Observable<Help> managerHelp(@Body RequestBody params);

    @GET("door/helps")
    Observable<List<HelpRecords>> helpRecords();


    @FormUrlEncoded
    @POST("door/puthtoken")
    Observable<User> resetToken(@Field("help_token") String help_token);
}
