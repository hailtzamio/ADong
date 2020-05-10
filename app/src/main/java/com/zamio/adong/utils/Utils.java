package com.zamio.adong.utils;

import java.text.SimpleDateFormat;

public class Utils {

    public static String convertDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
        String newFormat = formatter.format(date);

        return newFormat;
    }
}
