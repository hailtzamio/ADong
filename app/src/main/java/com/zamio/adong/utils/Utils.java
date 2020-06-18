package com.zamio.adong.utils;

import android.content.Context;
import android.os.Environment;

import androidx.core.content.ContextCompat;

import java.io.File;
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

    public static String getRootDirPath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = ContextCompat.getExternalFilesDirs(context.getApplicationContext(),
                    null)[0];
            return file.getAbsolutePath();
        } else {
            return context.getApplicationContext().getFilesDir().getAbsolutePath();
        }
    }

    public static String getProgressDisplayLine(long currentBytes, long totalBytes) {
        return getBytesToMBString(currentBytes) + "/" + getBytesToMBString(totalBytes);
    }

    private static String getBytesToMBString(long bytes){
        return String.format(Locale.ENGLISH, "%.2fMb", bytes / (1024.00 * 1024.00));
    }
}
