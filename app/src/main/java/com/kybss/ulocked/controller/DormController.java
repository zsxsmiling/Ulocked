package com.kybss.ulocked.controller;

import android.app.Activity;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppConfig;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.model.bean.Building;
import com.kybss.ulocked.model.bean.Floor;
import com.kybss.ulocked.model.bean.Lock;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.OrderRoom;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.model.bean.ResultsPage;
import com.kybss.ulocked.model.bean.User;
import com.kybss.ulocked.model.event.ItemSelectedEvent;
import com.kybss.ulocked.network.RequestCallback;
import com.kybss.ulocked.network.RestApiClient;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.PickedListener;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringUtil;
import com.squareup.otto.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.inject.Inject;


import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_UNAUTHORIZED;
import static com.kybss.ulocked.util.Constants.Key.PARAM_CLIENT_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_CLIENT_SECRET;
import static com.kybss.ulocked.util.Constants.Key.PARAM_END_TIME;
import static com.kybss.ulocked.util.Constants.Key.PARAM_GRANT_TYPE;
import static com.kybss.ulocked.util.Constants.Key.PARAM_PASSWORD;
import static com.kybss.ulocked.util.Constants.Key.PARAM_PUSH_CLIENT_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_ROOM_ID;
import static com.kybss.ulocked.util.Constants.Key.PARAM_START_TIME;
import static com.kybss.ulocked.util.Constants.Key.PARAM_THEME;
import static com.kybss.ulocked.util.Constants.Key.PARAM_USER_NAME;


/**
 * Created by Administrator on 2018\4\6 0006.
 */

public class DormController extends BaseController<DormController.DormUi,
        DormController.DormUiCallbacks> {

    private final RestApiClient mRestApiClient;

    @Inject
    public DormController(RestApiClient restApiClient) {
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

    @Override
    protected void populateUi(DormUi ui) {
        if (ui instanceof DormListUi) {
            populateDormListUi((DormListUi) ui);
        } else if (ui instanceof RoomOrderUi) {
            populateRoomOrderUi((RoomOrderUi) ui);
        } else if (ui instanceof ActivityListUi) {
            populateActivityListUi((ActivityListUi) ui);
        }

    }

    private void populateActivityListUi(ActivityListUi ui) {
        doFetchActivities(getId(ui));
    }

    private void populateRoomOrderUi(RoomOrderUi ui) {
        ui.showUserInfo(AppCookie.getUserInfo());
    }

    private void populateDormListUi(DormListUi ui) {
    //    doFetchMeeting(getId(ui));
        doFetchFloors(getId(ui));
    }

    private void doFetchFloors(final int callingId) {
       mRestApiClient.meetroomService()
                .getAllFloors()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                        //    ((DormListUi) ui).onFetchAllFloorsFinish(floors);
                            ((DormListUi) ui).onFinishRequest(floors,1,false);
                        }
                    }
                });
    }

    private void doSearchZD(final int callingId) {
        mRestApiClient.meetroomService()
                .searchBuildZD()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {

                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onFinishRequest(floors,1,false);

                        }
                    }
                });
    }


    private void doSearchQG(final int callingId) {
        mRestApiClient.meetroomService()
                .searchBuildQG()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        Log.e("err1", String.valueOf(error));
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onFinishRequest(floors,1,false);

                        }
                    }
                });
    }
    private void doSearchDN(final int callingId) {
        mRestApiClient.meetroomService()
                .searchBuildDN()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onFinishRequest(floors,1,false);

                        }
                    }
                });
    }
    //获取当前用户所有请求的房间
    private void doFetchActivities(final int callingId) {
        if (!AppCookie.isLoggin()) {
            DormUi ui = findUi(callingId);
            if (ui instanceof ActivityListUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        } else{
            mRestApiClient.meetroomService()
                    .get_apply_rooms()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            DormUi ui = findUi(callingId);
                            if (ui instanceof ActivityListUi) {
                                ((ActivityListUi) ui).onStartRequest(1);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<List<Meeting>>() {
                        @Override
                        public void onFailure(ResponseError error) {
                            DormUi ui = findUi(callingId);
                            if (ui instanceof ActivityListUi) {
                                ui.onResponseError(error);
                            }
                        }
                        @Override
                        public void onResponse(List<Meeting> meetings) {
                            DormUi ui = findUi(callingId);
                            if (ui instanceof ActivityListUi) {
                                ((ActivityListUi) ui).onFinishRequest(meetings, 1, false);
                            }
                        }
                    });
        }
    }

    //根据楼和楼层获取房间
    private void doFetchMeetingByBuilding(final int callingId, String buliding, String floor){
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("building", buliding);
        params.put("floor", floor);
        String strEntity=gson.toJson(params);
        Log.e("发送格式", strEntity);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        Log.e("发送格式", String.valueOf(okhttp3.MediaType.parse("application/json")));
        mRestApiClient.meetroomService()
                .queryBuilding(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> meetings) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onFinishRequest(meetings,1,false);
                        }
                    }
                });
    }

    //根据房间号搜索房间
    private void doSearchMeeting(final int callingId, String key) {
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("key", key);
        String strEntity=gson.toJson(params);
        Log.e("发送格式", strEntity);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        Log.e("发送格式", String.valueOf(okhttp3.MediaType.parse("application/json")));
        mRestApiClient.meetroomService()
                .searchBuilding(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> meetings) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onFinishRequest(meetings,1,false);
                        }
                    }
                });
    }

    //获取所有可用的房间
    private void doFetchMeeting(final int callingId) {
        mRestApiClient.meetroomService()
                .rooms()
/*                .map(new Function<ResultsPage<Meeting>, List<Meeting>>() {
                    @Override
                    public List<Meeting> apply(@NonNull ResultsPage<Meeting> meetingResultsPage){
                        return meetingResultsPage.results;
                    }
                })*/
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onStartRequest(1);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> meetings) {
                        DormUi ui = findUi(callingId);
                        if(ui instanceof DormListUi){
                            ((DormListUi) ui).onFinishRequest(meetings,1,false);

                        }
                    }
                });
    }


    private void doOrderRoom(final int callingId,Meeting meeting){

        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_ROOM_ID, meeting.getR_id());
        params.put(PARAM_START_TIME, meeting.getStart_time());
        params.put(PARAM_END_TIME, meeting.getEnd_time());
        params.put(PARAM_THEME, meeting.getTheme());
        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.meetroomService()
                .orderRoom(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<OrderRoom>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if (ui instanceof  RoomOrderUi) {
                            ui.onResponseError(error);
                        }
                    }

                    @Override
                    public void onResponse(OrderRoom response) {
                        DormUi ui = findUi(callingId);
                        if (ui instanceof  RoomOrderUi) {
                            ((RoomOrderUi) ui).onOrdeRoomFinish();
                        }
                    }
                });
    }


    private void doFinishMeeting(final int callingId,Meeting meeting){
        Gson gson=new Gson();
        Map<String, Integer> params = new HashMap<>();
        params.put("orderId", meeting.getId());
        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.meetroomService()
                .finishMeeting(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<OrderRoom>() {
                    @Override
                    public void onResponse(OrderRoom response) {
                        DormUi ui = findUi(callingId);
                        if (ui instanceof  ActivityDetailUi) {
                            ((ActivityDetailUi) ui).onFinishMeeting();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if (ui instanceof  ActivityDetailUi) {
                            ui.onResponseError(error);
                        }
                    }
                });
    }

    private void doRequestDoor(final int callingId, String door_id) {
        Gson gson=new Gson();
        Map<String, String> params = new HashMap<>();
        params.put("doorId", door_id);

        String strEntity=gson.toJson(params);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
        mRestApiClient.meetroomService()
                .questDoor(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<Lock>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        DormUi ui = findUi(callingId);
                        if (ui instanceof  ActivityListUi) {
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(Lock mlock) {
                        DormUi ui = findUi(callingId);
                        if (ui instanceof  ActivityListUi) {
                            ((ActivityListUi) ui).onRequestDoorSuccess(mlock);
                        }
                    }
                });


    }


    @Override
    protected  DormUiCallbacks createUiCallbacks(final  DormUi ui) {
        return new  DormUiCallbacks() {
            @Override
            public void refresh() {
                if (ui instanceof DormListUi) {
                    doFetchFloors(getId(ui));
                }
            }
            @Override
            public void nextPage() {

            }

            @Override
            public void requestDoor(String door_id ) {
                if (ui instanceof ActivityListUi){
                    doRequestDoor(getId(ui),door_id);
                }
            }

            @Override
            public void showOrderRoom(Meeting meeting) {
                Display display = getDisplay();
                if (display != null) {
                    if (AppCookie.isLoggin()) {
                        display.showOrder(meeting);
                    } else {
                        display.showLogin();
                    }
                }
            }

            @Override
            public void orderRoom(Meeting meeting) {
                doOrderRoom(getId(ui),meeting);
            }


            @Override
            public void showBluetooth(Lock lock){
                {
                    Display display = getDisplay();
                    if (display != null) {
                        display.showBluetooth(lock);
                    }
                }
            }


            @Override
            public void serachBuilding(String key) {
                doSearchMeeting(getId(ui),key);
            }

            @Override
            public void searchBuilding(String building) {
                if(building.equals("QGY")){
                doSearchQG(getId(ui));}
                if(building.equals("DNY")) {
                    doSearchDN(getId(ui));
                }
                if(building.equals("ZDY")) {
                    doSearchZD(getId(ui));
                }
                }


            @Override
            public void searchFloor(String building, String floor) {
                doFetchMeetingByBuilding(getId(ui), building, floor);
            }

            @Override
            public void close(){
                Display display = getDisplay();
                if (display != null) {
                    display.finishActivity();
                }
            }

            @Override
            public void showScan() {
                Display display = getDisplay();
                if (display != null) {
                    if (AppCookie.isLoggin()) {
                        display.showScan();
                    } else {
                        display.showLogin();
                    }
                }
            }

            @Override
            public void showLogin() {
                Display display = getDisplay();
                if (display != null) {
                    display.showLogin();
                }
            }


            @Override
            public void refreshActivities() {
                if (ui instanceof ActivityListUi) {
                    doFetchActivities(getId(ui));
                }
            }

            @Override
            public void showDatePicker(PickedListener mPickedListener) {
                Display display = getDisplay();
                if (display != null) {
                    display.showDatePickerDialog(mPickedListener);
                }
            }
            @Override
            public void showTiemPicker(PickedListener mPickedListener){
                Display display = getDisplay();
                if (display != null) {
                    display.showTimePickerDialog(mPickedListener);
                }
            }

            @Override
            public  void showActivityDetail(Meeting meeting){
                Display display = getDisplay();
                if (display != null) {
                    if (AppCookie.isLoggin()) {
                        display.showActivityDetail(meeting);
                    } else {
                        display.showLogin();
                    }
                }
            }
            @Override
            public void searchQG() {
                doSearchQG(getId(ui));
            }
            @Override
            public void searchDN() {
                doSearchDN(getId(ui));
            }
            @Override
            public void searchZD() {
                doSearchZD(getId(ui));
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
            public void finishMeeting(Meeting meeting){
                if (ui instanceof ActivityDetailUi) {
                    doFinishMeeting(getId(ui),meeting);
                }
            }

        };

    }

    public interface DormUiCallbacks {
        void refresh();

        void nextPage();

        void requestDoor(String door_id);

        void showOrderRoom(Meeting meeting);


        void orderRoom(Meeting meeting);

        void close();

        void showScan();

        void showDatePicker(PickedListener mPickedListener);

        void showTiemPicker(PickedListener mPickedListener);

        void showLogin();

        void refreshActivities();

        void showActivityDetail(Meeting meeting);

        void finishMeeting(Meeting meeting);

        void showBluetooth(Lock lock);

        void serachBuilding(String key);
        void searchBuilding(String building);

        void searchFloor(String building,String floor);
        void searchQG();
        void searchDN();
        void searchZD();
        void showZDActivity();
        void showDNActivity();
        void showQGActivity();
    }

    public interface DormUi extends BaseController.Ui<DormUiCallbacks> {
        Meeting getRequestParameter();
    }

    public interface ActivityDetailUi extends DormUi{
        void onFinishMeeting();
    }
    //获取所有房间接口
    public interface DormListUi extends DormUi, BaseController.ListUi<Meeting, DormUiCallbacks> {
        void onFetchAllFloorsFinish(List<Meeting> floors);
    }

    //获取所有活动接口
    public interface ActivityListUi extends DormUi, BaseController.ListUi<Meeting, DormUiCallbacks> {
        void onRequestDoorSuccess(Lock lock);
    }


    public interface RoomOrderUi extends DormUi{
        void showUserInfo(User user);
        void onOrdeRoomFinish();
    }

    public interface BluetoothUi extends DormUi{

    }
}
