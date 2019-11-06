package com.hy.iom.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cs on 2019/7/30.
 */
public class DateUtils {

    public static boolean withinAMonth(Date date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -31);
        Date t = c.getTime();
        date = sdf.parse(sdf.format(date));
        t = sdf.parse(sdf.format(t));
        if(date.before(t)){
            return false;
        }
        return true;
    }

    public static boolean withinAMonth(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, -31);
        Date t = c.getTime();
        t = sdf.parse(sdf.format(t));
        Date d = sdf.parse(date);
        if(d.before(t)){
            return false;
        }
        return true;
    }

}
