package com.kybss.ulocked.util;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Created by Administrator on 2018\4\24 0024.
 */

public abstract class PickedListener implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private String mouth,day;
    private String hour,minute;
    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

        if(monthOfYear<=9){
            mouth="0"+(monthOfYear+1);
        }else{
            mouth=String.valueOf(monthOfYear+1);
        }
        if(dayOfMonth<=9){
            day= "0"+dayOfMonth;
        }else{
            day=String.valueOf(dayOfMonth);
        }
        onPickedFinish(String.valueOf(year)+"-"+mouth+"-"+day);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
        if(hourOfDay<=9){
            hour = "0"+hourOfDay;
        }else{
            hour = String.valueOf(hourOfDay);
        }
        if(minuteOfHour<=9){
            minute = "0"+minuteOfHour;
        }else{
            minute = String.valueOf(minuteOfHour);
        }
       onPickedFinish(hour+":"+minute);

    }

    public void onPickedFinish(String  data){}




}
