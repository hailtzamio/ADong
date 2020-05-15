package com.zamio.adong;

import android.app.Application;

import com.onesignal.OneSignal;
import com.zamio.adong.utils.ConnectivityReceiver;
import com.zamio.adong.utils.FontsOverride;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    private String font = "fonts/NotoSans-Regular.ttf";
    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "DEFAULT", font);
        FontsOverride.setDefaultFont(this, "MONOSPACE", font);
        FontsOverride.setDefaultFont(this, "SERIF", font);
        FontsOverride.setDefaultFont(this, "SANS_SERIF", font);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();



        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}

