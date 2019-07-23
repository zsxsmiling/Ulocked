package com.kybss.ulocked.controller;




import android.util.Log;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.kybss.ulocked.R;
import com.kybss.ulocked.base.BaseController;
import com.kybss.ulocked.context.AppCookie;
import com.kybss.ulocked.model.bean.Help;
import com.kybss.ulocked.model.bean.HelpRecords;
import com.kybss.ulocked.model.bean.Meeting;
import com.kybss.ulocked.model.bean.ResponseError;
import com.kybss.ulocked.model.bean.Review;
import com.kybss.ulocked.network.RequestCallback;
import com.kybss.ulocked.network.RestApiClient;
import com.kybss.ulocked.ui.Display;
import com.kybss.ulocked.util.StringFetcher;
import com.kybss.ulocked.util.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.kybss.ulocked.util.Constants.HttpCode.HTTP_UNAUTHORIZED;

/**
 * Created by Administrator on 2018\4\26 0026.
 */

public class ManagerController extends BaseController<ManagerController.ManagerUi,
        ManagerController.ManagerUiCallbacks> {

    private final RestApiClient mRestApiClient;

    @Inject
    public ManagerController(RestApiClient restApiClient) {
        super();
        mRestApiClient = Preconditions.checkNotNull(restApiClient, "restApiClient cannot be null");
    }

    @Override
    protected void populateUi(ManagerController.ManagerUi ui) {
        if (ui instanceof AdminTabUi) {
            populateAdminTabUi((AdminTabUi) ui);
        }else if (ui instanceof ManagerListUi) {
            populateManagerListUi((ManagerListUi) ui);
        }else if(ui instanceof HelpListUi){
            populateHelpListUi((HelpListUi) ui);
        }
    }

    private void populateHelpListUi(HelpListUi ui) {
        doFetchHelper(getId(ui));
    }

    private void populateAdminTabUi(AdminTabUi ui) {
        ui.setTabs(ManagerTab.NORMAL,ManagerTab.HELPER);
    }

    private void populateManagerListUi(ManagerListUi ui) {
        doFetchManager(getId(ui));
    }


    private void doFetchHelper(final int callingId) {
        if (!AppCookie.isLoggin()) {
            ManagerUi ui = findUi(callingId);
            if (ui instanceof HelpListUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        } else {
            mRestApiClient.helperService()
                    .helpRecords()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof HelpListUi) {
                                ((HelpListUi) ui).onStartRequest(1);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<List<HelpRecords>>() {
                        @Override
                        public void onFailure(ResponseError error) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof HelpListUi) {
                                ui.onResponseError(error);
                            }
                        }

                        @Override
                        public void onResponse(List<HelpRecords> response) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof HelpListUi) {
                                ((HelpListUi) ui).onFinishRequest(response, 1, false);
                            }
                        }
                    });
        }
    }

    /**
     * 刷新时获取待处理的请求
     * @param callingId
     */
    private void doFetchManager(final int callingId) {
        if (!AppCookie.isLoggin()) {
            ManagerUi ui = findUi(callingId);
            if (ui instanceof ManagerListUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        }else{
            mRestApiClient.managerService()
                    .get_manager_rooms()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(@NonNull Disposable disposable) throws Exception {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerListUi) {
                                ((ManagerListUi) ui).onStartRequest(1);
                            }
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<List<Meeting>>() {
                        @Override
                        public void onResponse(List<Meeting> meetings) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerListUi) {
                                ((ManagerListUi) ui).onFinishRequest(meetings, 1, false);
                            }
                        }
                        @Override
                        public void onFailure(ResponseError error) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerListUi) {
                                ui.onResponseError(error);
                            }
                        }
                    });

        }
    }

    private void doReviewHelp(final int callingId,String id, int approve){
        if (!AppCookie.isLoggin()) {
            ManagerUi ui = findUi(callingId);
            if (ui instanceof ManagerDetailUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        }else{
            Gson gson=new Gson();
            Map<String, Integer> params = new HashMap<>();
            params.put("orderId", Integer.valueOf(id));
            params.put("message", approve);
            String strEntity=gson.toJson(params);
            Log.e("发送格式", strEntity);
            RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
            Log.e("发送格式", String.valueOf(okhttp3.MediaType.parse("application/json")));
            mRestApiClient.helperService()
                    .managerHelp(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<Help>() {
                        @Override
                        public void onResponse(Help response) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerDetailUi) {
                                ((ManagerDetailUi) ui).onFinishReview();
                            }
                        }

                        @Override
                        public void onFailure(ResponseError error) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerDetailUi) {
                                ui.onResponseError(error);
                            }
                        }
                    });

        }
    }


    private void doReview(final int callingId, Meeting meeting, int approve) {
        if (!AppCookie.isLoggin()) {
            ManagerUi ui = findUi(callingId);
            if (ui instanceof ManagerDetailUi) {
                ResponseError error = new ResponseError(HTTP_UNAUTHORIZED,
                        StringFetcher.getString(R.string.toast_error_not_login));
                ui.onResponseError(error);
            }
        }else{
            Gson gson=new Gson();
            Map<String, Integer> params = new HashMap<>();
            params.put("message", approve);
            params.put("orderId", meeting.getId());

            String strEntity=gson.toJson(params);
            RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),strEntity);
            mRestApiClient.managerService()
                    .review(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new RequestCallback<Review>() {
                        @Override
                        public void onResponse(Review response) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerDetailUi) {
                                ((ManagerDetailUi) ui).onFinishReview();
                            }
                        }

                        @Override
                        public void onFailure(ResponseError error) {
                            ManagerUi ui = findUi(callingId);
                            if (ui instanceof ManagerDetailUi) {
                                ui.onResponseError(error);
                            }
                        }
                    });

        }
    }



    @Override
    protected ManagerUiCallbacks createUiCallbacks(final ManagerController.ManagerUi ui) {
        return new ManagerUiCallbacks() {

            @Override
            public void showLogin() {
                Display display = getDisplay();
                if (display != null) {
                    display.showLogin();
                }
            }

            @Override
            public void refreshHelpers() {
                if (ui instanceof HelpListUi) {
                    doFetchHelper(getId(ui));
                }
            }

            @Override
            public void reviewHelp(String id, int i){
                if(ui instanceof ManagerDetailUi){
                    doReviewHelp(getId(ui),id,i);
                }
            }

            @Override
            public void refreshManagers() {
                if (ui instanceof ManagerListUi) {
                    doFetchManager(getId(ui));
                }
            }
            @Override
            public void showManagerDetail(Meeting meeting){
                Display display = getDisplay();
                if (display != null) {
                    if (AppCookie.isLoggin()) {
                        display.showManagerDetail(meeting);
                    } else {
                        display.showLogin();
                    }
                }
            }
            @Override
            public void review(Meeting meeting, int approve){
                if (ui instanceof ManagerDetailUi) {
                    doReview(getId(ui),meeting,approve);
                }
            }

            @Override
            public void finish(){
                Display display = getDisplay();
                if (display != null) {
                    display.finishActivity();
                }
            }

            @Override
            public void showHelperDetail(HelpRecords recode){
                Display display = getDisplay();
                if (display != null) {
                    display.showHelpManager(recode);
                }
            }

        };
    }



    public interface ManagerUiCallbacks {

        void showLogin();

        void refreshManagers();

        void showManagerDetail(Meeting meeting);

        void review(Meeting meeting, int i);

        void finish();

        void refreshHelpers();

        void showHelperDetail(HelpRecords recode);

        void reviewHelp(String id, int i);
    }


    public enum ManagerTab {
        NORMAL,
        HELPER
    }

    /**
     * UI界面必须实现的接口,留给控制器调用
     */
    public interface ManagerUi extends BaseController.Ui<ManagerUiCallbacks> {}

    public interface ManagerListUi extends ManagerController.ManagerUi,
            BaseController.ListUi<Meeting, ManagerUiCallbacks> {}

    public interface ManagerDetailUi extends ManagerUi{
        void onFinishReview();
    }

    public interface AdminTabUi extends ManagerUi {
        void setTabs(ManagerTab... tabs);
    }

    public interface HelpListUi extends ManagerController.ManagerUi,
    BaseController.ListUi<HelpRecords, ManagerUiCallbacks> {}
}