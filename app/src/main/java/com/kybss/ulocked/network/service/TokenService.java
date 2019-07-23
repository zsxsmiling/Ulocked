package com.kybss.ulocked.network.service;



import com.kybss.ulocked.model.bean.Token;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;


public interface TokenService {
    @POST("sso/login")
    @Headers({"Content-Type:application/json"})
    Observable<Token> accessToken(@Body RequestBody params);



}