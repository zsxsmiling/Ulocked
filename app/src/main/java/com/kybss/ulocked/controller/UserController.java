package com.kybss.ulocked.controller;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppConfig;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.model.bean.DoorCheck;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.OrderRoom;
import com.kybss.ulocked.model.bean.Register;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.model.bean.ResponseError_other;
import com.kybss.ulocked.model.bean.ResultsPage;
import com.kybss.ulocked.model.bean.Token;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.model.event.AccountChangedEvent;
import com.kybss.ulocked.network.RestApiClient;
import com.kybss.ulocked.network.RequestCallback;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.ui.activity.RegisterActivity;
import com.kybss.ulocked.util.ActivityStack;
import com.kybss.ulocked.util.Constants;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.RandomUtil;
import com.kybss.ulocked.util.ToastUtil;
import com.squareup.otto.Subscribe;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import butterknife.BindView;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.kybss.ulocked.util.Constants.Key.PARAM_CHECK_TIME;
import static com.kybss.ulocked.util.Constants.Key.PARAM_CLIENT_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_CLIENT_SECRET;
import static com.kybss.ulocked.util.Constants.Key.PARAM_GRANT_TYPE;
import static com.kybss.ulocked.util.Constants.Key.PARAM_PASSWORD;
import static com.kybss.ulocked.util.Constants.Key.PARAM_PUSH_CLIENT_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_RANDOM_NUMBER;
import static com.kybss.ulocked.util.Constants.Key.PARAM_ROOM_TYPE;
import static com.kybss.ulocked.util.Constants.Key.PARAM_USER_NAME;
import static com.kybss.ulocked.util.Constants.Persistence.ACCESS_TOKEN;


/**
 * Created by Administrator on 2018\4\8 0008.
 */

public class UserController extends BaseController<UserController.UserUi,
        UserController.UserUiCallbacks>{

    private final RestApiClient mRestApiClient;

    @Inject
    public UserController(RestApiClient restApiClient) {
        super();
        mRestApiClient = Preconditions.checkNotNull(restApiClient, "restApiClient cannot be null");
    }

    @Override
    protected void onInited() {
        super.onInited();
        EventUtil.register(this);
    }

    @Override
    protected void onSuspended() {
        EventUtil.unregister(this);
        super.onSuspended();
    }

    @Subscribe
    public void onAccountChanged(AccountChangedEvent event) {
        User user = event.getUser();
        if (user != null) {
            if(user.getRank() <= AppConfig.getAppRank())
            {
            AppCookie.saveUserInfo(user);
            AppCookie.saveLastPhone(user.getMobile());
            }
            else {
                AppCookie.saveUserInfo(null);
                AppCookie.saveAccessToken(null);
                AppCookie.saveRefreshToken(null);
                mRestApiClient.setToken(null);
                showWarning(3);
            }
        } else {
            AppCookie.saveUserInfo(null);
            AppCookie.saveAccessToken(null);
            AppCookie.saveRefreshToken(null);
            mRestApiClient.setToken(null);
        }
        populateUis();
    }

    @Override
    protected synchronized void populateUi(UserUi ui) {
        if (ui instanceof UserCenterUi) {
            populateUserCenterUi((UserCenterUi) ui);
        } else if (ui instanceof UserCodeUi) {
            populateUserCodeUi((UserCodeUi) ui);
        }else if (ui instanceof UserRecordUi) {
            populateUserRecordUi((UserRecordUi) ui);
        }
    }

    private void populateUserRecordUi(UserRecordUi ui) {
        doRefreshRecord(getId(ui));
    }



    private void populateUserCodeUi(UserCodeUi ui) {
        doRefreshCode(getId(ui),Constants.RoomType.ROOMTYPE_MEETING);
    }

    private void populateUserCenterUi(UserCenterUi ui) {
        ui.showUserInfo(AppCookie.getUserInfo());
    }



    /**
     * 刷新二维码
     */
    private void doRefreshCode(final int callingId,String room_type) {
        RandomUtil.Refresh();
        StringBuilder qrCode = new StringBuilder();
        qrCode.append("rand_num=");
        qrCode.append(RandomUtil.getRandomNum());
        qrCode.append("&check_time=");
        qrCode.append(RandomUtil.getCheckTime());
        qrCode.append("&class=");
        qrCode.append(room_type);
        qrCode.append("\"");
        //Bitmap mRefreshCode = QRCodeUtil.createQRCodeBitmap(qrCode.toString(),200);
        Bitmap mRefreshCode =  CodeUtils.createImage(qrCode.toString(), 200, 200, null);
        UserUi ui = findUi(callingId);
        if (ui instanceof UserCodeUi) {
            ((UserCodeUi) ui).refreshCodeFinish(mRefreshCode);
        }
    }

    /**
     * 用户退出登录
     * @param callingId
     */
    private void doLogout(final int callingId) {
        UserUi ui = findUi(callingId);
        if (ui instanceof UserCenterUi) {
            // 发送用户账户改变的事件
            EventUtil.sendEvent(new AccountChangedEvent(null));
            ((UserCenterUi) ui).logoutFinish();
        }
    }

    private void doSubmitNewToken(final int callingId, String token){
        mRestApiClient.helperService()
                .resetToken(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<User>() {
                    @Override
                    public void onResponse(User response) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserHelpTokenUi) {
                            ((UserHelpTokenUi) ui).submitHelpTokenFinish();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserHelpTokenUi) {
                            ui.onResponseError(error);
                        }
                    }
                });

    }


    private void doSubmitCode(final int callingId, String room_type) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_RANDOM_NUMBER, RandomUtil.getRandomNum());
        params.put(PARAM_CHECK_TIME, RandomUtil.getCheckTime());
        params.put(PARAM_ROOM_TYPE,Constants.RoomType.ROOMTYPE_MEETING );
        mRestApiClient.meetroomService()
                .submitQCode(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Boolean>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserCodeUi) {
                            ui.onResponseError(error);
                        }
                    }

                    @Override
                    public void onResponse(Boolean response) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserCodeUi) {
                            ((UserCodeUi) ui).submitCodeFinish();
                        }
                    }
                });
    }

    /**
     * 登录，获取AccessToken及用户资料
     * @param callingId
     * @param username
     * @param password
     */
/*    private void doLogin(final int callingId, String username, String password) {
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PUSH_CLIENT_ID,
                PushManager.getInstance().getClientid(AppContext.getContext()));
*//*        params.put(PARAM_CLIENT_ID, AppConfig.APP_KEY);
        params.put(PARAM_CLIENT_SECRET, AppConfig.APP_SECRET);
        params.put(PARAM_GRANT_TYPE, "password");*//*
        params.put(PARAM_USER_NAME, username);
        params.put(PARAM_PASSWORD, password);
        String strEntity=gson.toJson(params);
        Log.e("发送格式", strEntity);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        Log.e("发送格式", String.valueOf(okhttp3.MediaType.parse("application/json")));
        mRestApiClient.tokenService()
                .accessToken(body)
                .flatMap(new Function<Token, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@NonNull Token token){
                        Log.e("token", String.valueOf(token));
                        AppCookie.saveAccessToken(token.getToken());
                        AppCookie.saveRefreshToken(token.getRefreshToken());
                        return mRestApiClient.setToken(token.getToken())
                                .accountService()
                                .profile(token.getUserId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<User>() {
                    @Override
                    public void onResponse(User user) {
                        Log.e("返回信息", String.valueOf(user));
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserLoginUi) {
                            ((UserLoginUi) ui).userLoginFinish();
                            // 发送用户账户改变的事件
                            EventUtil.sendEvent(new AccountChangedEvent(user));
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        Log.e("错误信息", String.valueOf(error));
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserLoginUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }*/

    private void doLogin(final int callingId, String username, String password) {
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_PUSH_CLIENT_ID, PushManager.getInstance().getClientid(AppContext.getContext()));
        params.put(PARAM_USER_NAME, username);
        params.put(PARAM_PASSWORD, password);
        String strEntity=gson.toJson(params);
        Log.e("发送格式", strEntity);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        Log.e("发送格式", String.valueOf(okhttp3.MediaType.parse("application/json")));
        mRestApiClient.tokenService()
                .accessToken(body)
                .flatMap(new Function<Token, ObservableSource<User>>() {
                    @Override
                    public ObservableSource<User> apply(@NonNull Token token){
                        Log.e("token", token.getData().getToken());
                        AppCookie.saveAccessToken(token.getData().getToken());
                        Log.e("ACCESS", ACCESS_TOKEN);
                    //    AppCookie.saveRefreshToken(token.getRefreshToken());
                        return mRestApiClient.setToken(token.getData().getToken())
                                .accountService()
                                .profile();

                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<User>() {
                    @Override
                    public void onResponse(User user) {
                        Log.e("返回信息", String.valueOf(user.getCode()));
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserLoginUi) {

                                ((UserLoginUi) ui).userLoginFinish();
                                // 发送用户账户改变的事件
                                EventUtil.sendEvent(new AccountChangedEvent(user));


                            }
                        }

                    @Override
                    public void onFailure(ResponseError error) {
                        Log.e("错误信息", String.valueOf(error));
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserLoginUi) {
                            ui.onResponseError(error);

                        }
                    }
                });
    }
    /**
     * 创建新用户
     * @param callingId
     * @return
     */
    private void doCreateUser(final int callingId, Map<String, String> params) {
        Gson gson=new Gson();

        String strEntity=gson.toJson(params);
        Log.e("发送格式", strEntity);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        Log.e("发送格式", String.valueOf(okhttp3.MediaType.parse("application/json")));
        mRestApiClient.accountService()
                .register(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Register>() {
                    @Override
                    public void onResponse(Register response) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof UserSubmitInfoUi){
                            ((UserSubmitInfoUi) ui).userRegisterFinish();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        UserUi ui = findUi(callingId);
                        if(ui instanceof UserSubmitInfoUi){
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    private void doRefreshRecord(final int callingId) {
        mRestApiClient.meetroomService()
                .getAllDoorChecks()
                .map(new Function<ResultsPage<DoorCheck>, List<DoorCheck>>() {
                    @Override
                    public List<DoorCheck> apply(@NonNull ResultsPage<DoorCheck> doorCheckResultsPage) throws Exception {
                        return doorCheckResultsPage.results;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        UserController.UserUi ui = findUi(callingId);
                        if(ui instanceof UserController.UserRecordUi){
                            ((UserRecordUi) ui).onStartRequest(0);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<DoorCheck>>() {
                    @Override
                    public void onResponse(List<DoorCheck> response) {
                        UserController.UserUi ui = findUi(callingId);
                        if(ui instanceof UserController.UserRecordUi){
                            ((UserController.UserRecordUi) ui).onFinishRequest(response,0,false);
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        UserController.UserUi ui = findUi(callingId);
                        if(ui instanceof UserController.UserRecordUi){
                            ui.onResponseError(error);
                        }
                    }
                });

    }

    private void showWarning(long time) {
        /**
         * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
         * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
         * 有onTick，onFinsh、cancel和start方法
         */
        CountDownTimer timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔countDownInterval秒会回调一次onTick()方法
                ToastUtil.showToast("权限不足,"+millisUntilFinished/1000+"秒后关闭程序");
            }

            @Override
            public void onFinish() {
                Display display = getDisplay();
                if (display != null) {
                    ActivityStack.create().appExit();//强制结束程序
                }
            }
        };
        timer.start();// 开始计时
        //timer.cancel(); // 取消
    }

    /**
     * 注册的第1个步骤:发送短信验证码
     * @param callingId
     * @param mobile
     */
    private void doSendCode(final int callingId, String mobile) {
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.accountService()
                .sendCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<OrderRoom>() {
                    @Override
                    public void onFailure(ResponseError error) {
                         UserUi ui = findUi(callingId);
                        if (ui instanceof UserVerifyMobileUi) {
                            ui.onResponseError(error);
                        }
                    }

                    @Override
                    public void onResponse(OrderRoom response) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserVerifyMobileUi) {
                            ((UserVerifyMobileUi) ui).sendCodeFinish();
                        }
                    }
                });

    }

    /**
     * 注册的第2个步骤:检查短信验证码
     * @param callingId
     * @param mobile
     * @param code
     */
    private void doCheckCode(final int callingId, String mobile, String code){
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("code", code);
        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.accountService()
                .verifyCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<OrderRoom>() {
                    @Override
                    public void onResponse(OrderRoom response) {
                        Log.e("CHECK", String.valueOf(response.getCode()));
                        Log.e("CHECK1", response.getMessage());

                            UserUi ui = findUi(callingId);
                            if (ui instanceof UserVerifyMobileUi) {
                                if (response.getCode() == 200) {
                                    ((UserVerifyMobileUi) ui).verifyMobileFinish();
                                }
                                else {
                                    ToastUtil.showLongToast("验证码输入错误请重新输入");
                                    ((UserVerifyMobileUi) ui).verifyMobileError();
                                }



                            }
                        }

                    @Override
                    public void onFailure(ResponseError error) {
                        Log.e("CUOWU", String.valueOf(error.getMessage()));
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserVerifyMobileUi) {
                             ui.onResponseError(error);
                        }
                    }
                });


    }


    /**
     * 用户提出开门请求
     * @param
     * @return
     */
    private void doRequestDoor(final int callingId, String door_id) {
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("doorId", door_id);
        Log.e("doorid",door_id);

        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.meetroomService()
                .questDoor(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Lock>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserScanUi) {
                          ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(Lock mlock) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserScanUi) {
                            ((UserScanUi) ui).onRequestDoorSuccess(mlock);
                        }
                    }
                });


    }


    private void doFetchResetPwd(final int callingId, String door_id){
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("doorId", door_id);

        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.meetroomService()
                .resetPwd(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Lock>() {
                    @Override
                    public void onResponse(Lock response) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserScanUi)  {
                            ((UserScanUi) ui).onResetPwdSuccess(response);
                        }
                    }
                    @Override
                    public void onFailure(ResponseError error) {
                        UserUi ui = findUi(callingId);
                        if (ui instanceof UserScanUi)  {
                            ui.onResponseError(error);
                        }
                    }
                });
    }


    @Override
    protected UserUiCallbacks createUiCallbacks(final UserUi ui) {
        return new UserUiCallbacks() {
            @Override
            public void showLogin() {
                Display display = getDisplay();
                if (display != null) {
                    display.showLogin();
                }
            }

            @Override
            public void showRegister() {
                Display display = getDisplay();
                if (display != null) {
                        display.showRegister();
                    }
            }

            @Override
            public void refreshCode() {
                if (ui instanceof UserCodeUi) {
                doRefreshCode(getId(ui),Constants.RoomType.ROOMTYPE_MEETING);
                }
            }

            @Override
            public void showRecord() {
                Display display = getDisplay();
                if (display != null) {
                    display.showRecode();
                }
            }

            @Override
            public void createUser(Map<String, String> params){
                doCreateUser(getId(ui),params);
            }


            @Override
            public void refresh() {
                doRefreshRecord(getId(ui));
            }

            @Override
            public void login(String account, String password) {
                doLogin(getId(ui), account, password);
            }

            @Override
            public void logout() {
                doLogout(getId(ui));
            }

            /**
             * 发送验证码
             */
            @Override
            public void sendCode(String mobile) {
                doSendCode(getId(ui), mobile);
            }
            /**
             * 验证验证码
             */
            @Override
            public void checkCode(String mobile, String code) {
                doCheckCode(getId(ui), mobile, code);
            }

            @Override
            public void showCode() {
                {
                    Display display = getDisplay();
                    if (display != null) {
                        if (AppCookie.isLoggin()) {
                            display.showCode();
                        } else {
                            display.showLogin();
                        }
                    }
                }
            }
            @Override
            public void showToken() {
                {
                    Display display = getDisplay();
                    if (display != null) {
                        if (AppCookie.isLoggin()) {
                            display.showHelpToken();
                        } else {
                            display.showLogin();
                        }
                    }
                }
            }

            @Override
            public void close(){
                Display display = getDisplay();
                if (display != null) {
                    display.finishActivity();
                }
            }

            @Override
            public void submitCode(){
                doSubmitCode(getId(ui),Constants.RoomType.ROOMTYPE_MEETING);
            }

            @Override
            public void requestDoor(String door_id){
                doRequestDoor(getId(ui),door_id);
            }

            @Override
            public void showBluetooth(Lock lock){
                {
                    Display display = getDisplay();
                    if (display != null) {
                        if (AppCookie.isLoggin()) {
                            display.showBluetooth(lock);
                        } else {
                            display.showLogin();
                        }
                    }
                }
            }
            @Override
            public void showQGActivity(){
                Display display = getDisplay();
                if (display != null) {
                    display.showQGActivity();
                }
            }
            @Override
            public void showDNActivity(){
                Display display = getDisplay();
                if (display != null) {
                    display.showDNActivity();
                }
            }
            @Override
            public void showZDActivity(){
                Display display = getDisplay();
                if (display != null) {
                    display.showZDActivity();
                }
            }
            @Override
            public void showDormActivity(){
                Display display = getDisplay();
                if (display != null) {
                    display.showDormActivity();
                }
            }
            @Override
            public void resetPwd(String door_id){
                if (ui instanceof UserScanUi) {
                    doFetchResetPwd(getId(ui),door_id);
                }
            }

            @Override
            public void submitNewToken(String token){
                if (ui instanceof UserHelpTokenUi) {
                    doSubmitNewToken(getId(ui),token);
                }
            }
        };
    }




    public interface UserUiCallbacks {

        void showLogin();

        void showRegister();

        void refreshCode();

        void showRecord();

        void login(String account, String password);

        void logout();

        void sendCode(String mobile);

        void checkCode(String mobile, String code);

        void showCode();

        void close();

        void submitCode();

        void createUser(Map<String, String> params);

        void refresh();

        void requestDoor(String door_id);

        void showBluetooth(Lock lock);

        void resetPwd(String door_id);

        void showToken();

        void submitNewToken(String help_token);
         void showZDActivity();
         void showDNActivity();
         void showQGActivity();
         void showDormActivity();
    }

    public interface UserUi extends BaseController.Ui<UserUiCallbacks> {
    }

    public interface UserLoginUi extends UserUi {
        void userLoginFinish();
        void userLoginError();
    }

    public interface UserVerifyMobileUi extends  UserUi{
        void sendCodeFinish();
        void verifyMobileFinish();
        void verifyMobileError();
    }

    public interface UserSubmitInfoUi extends  UserUi{
        void userRegisterFinish();
    }

    public interface UserRegisterUi extends UserUi {

    }



    public interface UserCenterUi extends UserUi {
        void showUserInfo(User user);
        void uploadAvatarFinish();
        void logoutFinish();
    }


    public interface UserCodeUi extends UserUi{
        void refreshCodeFinish(Bitmap bitmap);

        void submitCodeFinish();
    }

    public interface UserHelpTokenUi extends UserUi{
        void submitHelpTokenFinish();
    }

    public interface UserScanUi extends UserUi{
        void onRequestDoorSuccess(Lock lock);
        void onResetPwdSuccess(Lock response);
    }

    public interface UserRecordUi extends UserUi, ListUi<DoorCheck,UserUiCallbacks>{

    }
}
