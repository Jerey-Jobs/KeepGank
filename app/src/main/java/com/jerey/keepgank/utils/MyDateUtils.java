package com.jerey.keepgank.utils;

import android.text.TextUtils;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Date;


public class MyDateUtils {
    public static DateTime formatDateFromStr(final String dateStr) {
        DateTime dateTime = new DateTime();
        if(!TextUtils.isEmpty(dateStr)) {
            try {
                dateTime = DateTime.parse(dateStr, ISODateTimeFormat.dateTimeParser());
            }catch (Exception e){
                Log.i("Exception:", e.getMessage());
            }
        }
        return dateTime;

    }

    public static boolean isSameDay(Date date1, Date date2) {
        return date1.getDay() == date2.getDay() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }
}
