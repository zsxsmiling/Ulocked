package com.kybss.ulocked.network.service;

import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.model.bean.OrderRoom;
import com.kybss.ulocked.model.bean.Register;
import com.kybss.ulocked.model.bean.User;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;



public interface AccountService {


    /**
     * 发送验证码
     * @param mobile
     * @return
     */


    @POST("sso/register/getcode")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> sendCode(@Body RequestBody params);




    /**
     * 检查验证码
     * @param mobile
     * @param code
     * @return
     */




    @POST("sso/register/validcode")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> verifyCode(@Body RequestBody params);


    @POST("sso/resetpwd/getcode")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> sendResetdCode(@Body RequestBody params);
    @POST("sso/resetpwd/validcode")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> verifyResetCode(@Body RequestBody params);
    @POST("sso/resetpwd/doreset")
    @Headers({"Content-Type:application/json"})
    Observable<OrderRoom> DoResetCode(@Body RequestBody params);





    /**
     * 创建帐号
     * @param
     * @return
     */


    @POST("sso/register/doregister")
    @Headers({"Content-Type:application/json"})
    Observable<Register> register(@Body RequestBody params);

    /**
     * 获取用户资料
     * @param id
     * @return
     */


    @GET("sso/user")

    Observable<User> profile();


    /**
     * 修改头像
     * @param userId
     * @param avatarUrl
     * @return
     */
    @FormUrlEncoded
    @PUT("sso/user/{id}")
    Observable<User> updateAvatar(@Path("id") String userId, @Field("avatar_url") String avatarUrl);



    /**
     * 设置姓名
     * @param userId
     * @param username
     * @return
     */
    @FormUrlEncoded
    @PUT("sso/user/{id}")
    Observable<User> setUsername(@Path("id") String userId, @Field("username") String username);

    /**
     * 设置昵称
     * @param userId
     * @param nickname
     * @return
     */
    @FormUrlEncoded
    @PUT("sso/user/{id}")
    Observable<User> setNickname(@Path("id") String userId, @Field("nickname") String nickname);
}