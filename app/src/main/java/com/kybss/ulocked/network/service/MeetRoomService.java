package com.kybss.ulocked.network.service;

import com.kybss.ulocked.model.bean.Building;
import com.kybss.ulocked.model.bean.Door;
import com.kybss.ulocked.model.bean.DoorCheck;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.OrderRoom;
import com.kybss.ulocked.model.bean.ResultsPage;
import com.kybss.ulocked.model.bean.Token;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.model.bean.resultpagenew;

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
import retrofit2.http.Path;


/**
 * Created by Administrator on 2018\4\8 0008.
 */

public interface MeetRoomService {

    @GET("room/all")
    Observable<List<Meeting>> rooms();

    /**
     * 用户获取申请会议室记录
     * @param params
     * @return
     */
    @GET("room/user/nodorm")
    Observable<List<Meeting>> get_apply_rooms();
    /**
     * 用户获取申请宿舍记录
     * @param params
     * @return
     */
    @GET("room/user/dorm")
    Observable<List<Meeting>> get_apply_dorms();


    @FormUrlEncoded
    @POST("doorchecks/phone")
    Observable<Boolean> submitQCode(@FieldMap Map<String, String> params);

  /*  @FormUrlEncoded
    @POST("meeting/order")
    Observable<Boolean> orderRoom(@FieldMap Map<String, String> params);*/


    @POST("room/order")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> orderRoom(@Body RequestBody params);

    @GET("doorchecks" )
    Observable<ResultsPage<DoorCheck>> getAllDoorChecks();


    //用户申请开门

    @POST("door/quest")
    @Headers({"Content-Type:application/json"})
    Observable<Lock> questDoor(@Body RequestBody params);

    //管理员重置密码

    @POST("door/reset")
    @Headers({"Content-Type:application/json"})
    Observable<Lock> resetPwd(@Body RequestBody params);

    //用户结束会议室、实验室使用

    @POST("room/finish")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> finishMeeting(@Body RequestBody params);


    //用户获取所有楼层信息
    @GET("room/building" )
    Observable<List<Meeting>> getAllFloors();

    //获取building建筑第floor层房间

    @POST("room/query")
    @Headers({"Content-Type:application/json"})
    Observable<List<Meeting>> queryBuilding(@Body RequestBody params);

     //根据key搜索房间

    @POST("room/search")
    @Headers({"Content-Type:application/json"})
    Observable<List<Meeting>> searchBuilding(@Body RequestBody params);

    //根据building搜索房间

    @GET("room/building/QGY")
    Observable<List<Meeting>> searchBuildQG();
    @GET("room/building/DNY")
    Observable<List<Meeting>> searchBuildDN();
    @GET("room/building/ZDY")
    Observable<List<Meeting>> searchBuildZD();


}
