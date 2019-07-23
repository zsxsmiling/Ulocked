package com.kybss.ulocked.network.service;

import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.Review;
import com.kybss.ulocked.model.bean.User;

import java.util.List;


import io.reactivex.Observable;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface ManagerService {



    /**
     * 管理员获取管理范围内的会议室,实验室，审核信息
     * @param
     * @return
     */
    @GET("room/admin")
    Observable<List<Meeting>> get_manager_rooms();


    /**
     * 管理员提交审核会议室、实验室结果
     * @param
     * @return
     */

    @POST("room/review")
    @Headers({"Content-Type:application/json"})
    Observable<Review> review(@Body RequestBody params);



}