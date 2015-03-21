package com.sample.alexkush.vkphotoviewer.tools;

import java.text.SimpleDateFormat;

public class Converter {

    public enum TimeUnit {
        SECOND,
        MILLISECOND;
    }

    public static String formatDate(long date, TimeUnit unit) {
        if (unit == TimeUnit.SECOND)
            date *= 1000;
        return new SimpleDateFormat("yyyy.MM.dd HH:mm").format(date);
    }
}
