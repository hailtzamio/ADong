package com.zamio.adong;

import android.app.Application;

import com.zamio.adong.utils.ConnectivityReceiver;
import com.zamio.adong.utils.FontsOverride;

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/RobotoLight.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/RobotoLight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/RobotoLight.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/RobotoLight.ttf");
        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

