package com.miu.photoboothdemo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String getDateByLongTime(long time) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdfDate.format(new Date(time * 1000));
    }
}
