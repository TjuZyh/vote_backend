package com.tju.bclab.vote_backend.common;

import java.util.Calendar;
import java.util.Date;

public class myUtils {
    //判断两个日期是否是同一年的同一天。true为同一天。
    static public Boolean sameDay(Date currentTime, Date databaseTime) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(currentTime);
        cal2.setTime(databaseTime);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;
    }
}
