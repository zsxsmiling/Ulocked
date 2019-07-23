package com.kybss.ulocked.network.service;

import com.kybss.ulocked.model.bean.DoorCheck;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResultsPage;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by Administrator on 2018\4\8 0008.
 */

public interface LabService {


    /**
     * 用户获取申请实验室请求记录
     * @param params
     * @return
     */
    @GET("lab/user")
    Observable<List<Meeting>> get_apply_rooms();


    /**
     * 用户预订实验室
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST("lab/order")
    Observable<Boolean> orderRoom(@FieldMap Map<String, String> params);




    //用户申请开门
    @FormUrlEncoded
    @POST("door/quest")
    Observable<Lock> questDoor(@Field("d_id") String d_id);


    //用户结束实验室使用
    @FormUrlEncoded
    @POST("lab/finish")
    Observable<Boolean> finishMeeting(@Field("order_id") String order_id);

}
