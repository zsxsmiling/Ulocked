package com.kybss.ulocked.controller;

import android.app.Activity;
import android.provider.ContactsContract;
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
import com.kybss.ulocked.util.DateUtil;
import com.kybss.ulocked.util.EventUtil;
import com.kybss.ulocked.util.PickedListener;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringToDate;
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

public class MeetController extends BaseController<MeetController.MeetUi,
        MeetController.MeetUiCallbacks> {

    private final RestApiClient mRestApiClient;

    @Inject
    public MeetController(RestApiClient restApiClient) {
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
    protected void populateUi(MeetUi ui) {
        if (ui instanceof MeetListUi) {
            populateMeetListUi((MeetListUi) ui);
        } else if (ui instanceof RoomOrderUi) {
            populateRoomOrderUi((RoomOrderUi) ui);
        } else if (ui instanceof ActivityListUi) {
            populateActivityListUi((ActivityListUi) ui);
        } else if (ui instanceof ActivityTabUi) {
            populateActivityTabUi((ActivityTabUi) ui);
        }
        else if (ui instanceof ActivityDormListUi) {
            populateActivityDormListUi((ActivityDormListUi) ui);
        }

    }



    private void populateActivityListUi(ActivityListUi ui) {
        doFetchActivities(getId(ui));
    }
    private void populateActivityDormListUi(ActivityDormListUi ui) {
        doFetchActivitieDorms(getId(ui));
    }
    private void populateActivityTabUi(ActivityTabUi ui) {
        ui.setTabs(ActivityTab.ACTIVITY,ActivityTab.DORM);
    }
    private void populateRoomOrderUi(RoomOrderUi ui) {
        ui.showUserInfo(AppCookie.getUserInfo());
    }

    private void populateMeetListUi(MeetListUi ui) {
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
                        Log.e("err2", String.valueOf(error));
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof MeetListUi) {
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof MeetListUi) {
                            ((MeetListUi) ui).onFetchAllFloorsFinish(floors);
                            //         ((BuildingZDListUi) ui).onFetchAllFloorsFinish(floors);

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

                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ((MeetListUi) ui).onFinishRequest(floors,1,false);

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
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ((MeetListUi) ui).onFinishRequest(floors,1,false);

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
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> floors) {
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ((MeetListUi) ui).onFinishRequest(floors,1,false);

                        }
                    }
                });
    }

    //获取当前用户所有请求的房间
    private void doFetchActivities(final int callingId) {
        if (!AppCookie.isLoggin()) {
            MeetUi ui = findUi(callingId);
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
                            MeetUi ui = findUi(callingId);
                            if (ui instanceof ActivityListUi) {
                                ((ActivityListUi) ui).onStartRequest(1);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<List<Meeting>>() {
                        @Override
                        public void onFailure(ResponseError error) {
                            MeetUi ui = findUi(callingId);
                            if (ui instanceof ActivityListUi) {
                                ui.onResponseError(error);
                            }
                        }
                        @Override
                        public void onResponse(List<Meeting> meetings) {
                            MeetUi ui = findUi(callingId);
                            if (ui instanceof ActivityListUi) {
                                ((ActivityListUi) ui).onFinishRequest(meetings, 1, false);
                            }
                        }
                    });
        }
    }

    //获取当前用户所有请求的宿舍
    private void doFetchActivitieDorms(final int callingId) {
        if (!AppCookie.isLoggin()) {
            MeetUi ui = findUi(callingId);
            if (ui instanceof ActivityDormListUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        } else{
            mRestApiClient.meetroomService()
                    .get_apply_dorms()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            MeetUi ui = findUi(callingId);
                            if (ui instanceof ActivityDormListUi) {
                                ((ActivityDormListUi) ui).onStartRequest(1);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<List<Meeting>>() {
                        @Override
                        public void onFailure(ResponseError error) {
                            MeetUi ui = findUi(callingId);
                            if (ui instanceof ActivityDormListUi) {
                                ui.onResponseError(error);
                            }
                        }
                        @Override
                        public void onResponse(List<Meeting> meetings) {
                            MeetUi ui = findUi(callingId);
                            if (ui instanceof ActivityDormListUi) {
                                ((ActivityDormListUi) ui).onFinishRequest(meetings, 1, false);
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
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> meetings) {
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ((MeetListUi) ui).onFinishRequest(meetings,1,false);
                      //      ((BuildingListUi) ui).onFetchAllFloorsFinish(meetings);
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

        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);

        mRestApiClient.meetroomService()
                .searchBuilding(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof MeetListUi) {
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> meetings) {
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof MeetListUi) {
                            ((MeetListUi) ui).onFinishRequest(meetings, 1, false);
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
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ((MeetListUi) ui).onStartRequest(1);
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RequestCallback<List<Meeting>>() {
                    @Override
                    public void onFailure(ResponseError error) {
                        Log.e("err3", String.valueOf(error));
                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(List<Meeting> meetings) {

                        MeetUi ui = findUi(callingId);
                        if(ui instanceof MeetListUi){
                       //     ((MeetListUi) ui).onFinishRequest(meetings,1,false);

                        }
                    }
                });
    }


    private void doOrderRoom(final int callingId,Meeting meeting){

        String string_start= DateUtil.StampToDate(meeting.getStart_time());






        String string_end=DateUtil.StampToDate(meeting.getEnd_time());


        Gson gson=new Gson();
        Map<String,String> params = new HashMap<>();
        params.put(PARAM_ROOM_ID, meeting.getR_id());
        params.put(PARAM_START_TIME, string_start);
        params.put(PARAM_END_TIME, string_end);
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

                        MeetUi ui = findUi(callingId);
                        if (ui instanceof  RoomOrderUi) {
                            ui.onResponseError(error);
                        }
                    }

                    @Override
                    public void onResponse(OrderRoom response) {

                        MeetUi ui = findUi(callingId);
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
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof  ActivityDetailUi) {
                            ((ActivityDetailUi) ui).onFinishMeeting();
                        }
                    }

                    @Override
                    public void onFailure(ResponseError error) {
                        MeetUi ui = findUi(callingId);
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
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof  ActivityListUi) {
                            ui.onResponseError(error);
                        }
                    }
                    @Override
                    public void onResponse(Lock mlock) {
                        MeetUi ui = findUi(callingId);
                        if (ui instanceof  ActivityListUi) {
                            ((ActivityListUi) ui).onRequestDoorSuccess(mlock);
                        }
                    }
                });


    }


    @Override
    protected MeetUiCallbacks createUiCallbacks(final MeetUi  ui) {
        return new MeetUiCallbacks() {
            @Override
            public void refresh() {
                if (ui instanceof MeetListUi) {
                    doFetchMeeting(getId(ui));
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
            public void refreshDorms() {
                if (ui instanceof ActivityDormListUi) {
                    doFetchActivitieDorms(getId(ui));
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
            public void finishMeeting(Meeting meeting){
                if (ui instanceof ActivityDetailUi) {
                    doFinishMeeting(getId(ui),meeting);
                }
            }

        };

    }

    public interface MeetUiCallbacks {
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

        void searchFloor(String building,String floor);
        void searchQG();
        void searchDN();
        void searchZD();

        void refreshDorms();
    }

    public enum ActivityTab {
        ACTIVITY,
        DORM
    }

    public interface MeetUi extends BaseController.Ui<MeetUiCallbacks> {
        Meeting getRequestParameter();
    }

    public interface ActivityDetailUi extends MeetUi{
        void onFinishMeeting();
    }
    //获取所有房间接口
    public interface MeetListUi extends MeetUi, BaseController.ListUi<Meeting, MeetUiCallbacks> {
        void onFetchAllFloorsFinish(List<Meeting> floors);
    }

    //获取所有活动接口
    public interface ActivityListUi extends MeetUi, BaseController.ListUi<Meeting, MeetUiCallbacks> {
        void onRequestDoorSuccess(Lock lock);
    }

    //获取所有宿舍接口
    public interface ActivityDormListUi extends MeetUi, BaseController.ListUi<Meeting, MeetUiCallbacks> {
        void onRequestDoorSuccess(Lock lock);
    }



    public interface RoomOrderUi extends MeetUi{
        void showUserInfo(User user);
        void onOrdeRoomFinish();
    }
    public interface ActivityTabUi extends MeetUi {
        void setTabs(ActivityTab... tabs);
    }

    public interface BluetoothUi extends MeetUi{

    }
}
