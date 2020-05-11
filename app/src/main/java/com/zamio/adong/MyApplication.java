package com.zamio.adong;

import android.app.Application;

import com.zamio.adong.utils.ConnectivityReceiver;
import com.zamio.adong.utils.FontsOverride;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private String font = "fonts/Roboto-Regular.ttf";
    @Override
    public void onCreate() {
        super.onCreate();
//        FontsOverride.setDefaultFont(this, "DEFAULT", font);
//        FontsOverride.setDefaultFont(this, "MONOSPACE", font);
//        FontsOverride.setDefaultFont(this, "SERIF", font);
//        FontsOverride.setDefaultFont(this, "SANS_SERIF", font);
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

