package com.kybss.ulocked.util;

import java.util.Random;

/**
 * Created by Administrator on 2018\4\11 0011.
 */

public class RandomUtil {

    private static String randomNum;
    private static String checkTime;



    public static void Refresh(){
            getRandomString();
            getCurrentSeconds();
    }
    public static void getRandomString() {
        long t1 = 0x7fffff & System.currentTimeMillis();
        long a = t1 << 6 | Math.abs(new Random().nextInt());
        randomNum = a+"";
    }
    public static void getCurrentSeconds(){
        checkTime = System.currentTimeMillis()/1000+"";
    }


    public static String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public static String getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }
}
