package com.zamio.adong.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String convertDate(String pdate) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
//        String newFormat = formatter.format(date);

        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        Date date = inputFormat.parse(pdate);
        String outputText = outputFormat.format(date);

        return outputText;
    }

    public static String convertDate2(String pdate) throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd,yyyy hh:mm a");
//        String newFormat = formatter.format(date);

        DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.US);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

        Date date = inputFormat.parse(pdate);
        String outputText = outputFormat.format(date);

        return outputText;
    }
}
