package com.zamio.adong.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.zamio.adong.R;


public class ProgressDialogUtils {
    public static ProgressDialog progressDialog;
    public static final String TAG = ProgressDialogUtils.class.getName();

    public static void showProgressDialog(Context context, int titleID,
                                          int messageID) {


        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        if (context == null) return;

        try {
            if (progressDialog != null) {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } else {
                progressDialog = new ProgressDialog(context, R.style.MyTheme);
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog = null;
            showProgressDialog(context, titleID, messageID);
            return;
        }
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
    }

    public static void dismissProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
