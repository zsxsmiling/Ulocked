package com.kybss.ulocked.util;


import com.kybss.ulocked.R;
import com.kybss.ulocked.context.AppContext;
import com.kybss.ulocked.controller.ManagerController;
import com.kybss.ulocked.controller.MeetController;

public class StringFetcher {

    public static String getString(int id) {
        return AppContext.getContext().getString(id);
    }

    public static String getString(int id, Object... format) {
        return AppContext.getContext().getString(id, format);
    }
    public static String getString(ManagerController.ManagerTab tab) {
        switch (tab) {
            case NORMAL:
                return getString(R.string.title_normal);
            case HELPER:
                return getString(R.string.title_helper);

        }
        return getString(R.string.app_name);
    }
    public static String getString(MeetController.ActivityTab tab) {
        switch (tab) {
            case ACTIVITY:
                return getString(R.string.title_meet);
            case DORM:
                return getString(R.string.title_dorm);

        }
        return getString(R.string.app_name);
    }

    public static String getMac(String mac_mix,boolean isEastDoor){
        String mac_mix_;
        mac_mix_ = mac_mix.replace("-","");
        if (mac_mix_.length() == 17){
                return mac_mix_;
        }else if(mac_mix_.length()==34){
                if(isEastDoor){
                    return mac_mix_.substring(17);
                }else{
                    return mac_mix_.substring(0,17);
                }
        }else return null;
    }
}