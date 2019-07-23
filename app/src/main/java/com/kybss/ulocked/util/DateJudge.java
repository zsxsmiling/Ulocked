package com.kybss.ulocked.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateJudge {
	
    public static String getDate(){
    	DateFormat dateTimeformat = new SimpleDateFormat("yyyy年MM月dd日");
    	String strBeginDate = dateTimeformat.format(new Date());
    	return  strBeginDate  ;			
    }
    public static String getTime(){
    	DateFormat dateTimeformat = new SimpleDateFormat("HH:mm");
    	String strBeginDate = dateTimeformat.format(new Date());
    	return  strBeginDate  ;		
    }
    
    public static String getWeekday(){
    	final Calendar c = Calendar.getInstance();
    	c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
    	 if("1".equals(mWay)){  
             mWay ="天";  
         }else if("2".equals(mWay)){  
             mWay ="一";  
         }else if("3".equals(mWay)){  
             mWay ="二";  
         }else if("4".equals(mWay)){  
             mWay ="三";  
         }else if("5".equals(mWay)){  
             mWay ="四";  
         }else if("6".equals(mWay)){  
             mWay ="五";  
         }else if("7".equals(mWay)){  
             mWay ="六";  
         } 
    	 return "星期"+mWay;  
    	 
    }
    
}
