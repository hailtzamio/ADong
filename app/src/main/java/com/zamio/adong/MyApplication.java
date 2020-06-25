package com.zamio.adong;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.downloader.PRDownloader;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.zamio.adong.network.ConstantsApp;
import com.zamio.adong.ui.activity.LoginActivity;
import com.zamio.adong.ui.notification.ReceiveNotificationActivity;
import com.zamio.adong.ui.project.tab.ui.main.information.BasicInformation2Activity;
import com.zamio.adong.ui.project.tab.ui.main.information.BasicInformationActivity;
import com.zamio.adong.ui.trip.DetailTripActivity;
import com.zamio.adong.utils.ConnectivityReceiver;
import com.zamio.adong.utils.FontsOverride;

import org.json.JSONObject;

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

        PRDownloader.initialize(getApplicationContext());
        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
//
//        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
//
//        OneSignal.startInit(this)
//                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
//                .autoPromptLocation(true)
//                .init();

//        OneSignal.startInit(this)
//                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
//                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(false)
//                .init();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
            String title = notification.payload.title;
            String body = notification.payload.body;
            String smallIcon = notification.payload.smallIcon;
            String largeIcon = notification.payload.largeIcon;
            String bigPicture = notification.payload.bigPicture;
            String smallIconAccentColor = notification.payload.smallIconAccentColor;
            String sound = notification.payload.sound;
            String ledColor = notification.payload.ledColor;
            int lockScreenVisibility = notification.payload.lockScreenVisibility;
            String groupKey = notification.payload.groupKey;
            String groupMessage = notification.payload.groupMessage;
            String fromProjectNumber = notification.payload.fromProjectNumber;
            String rawPayload = notification.payload.rawPayload;

            String customKey;

            Log.i("OneSignalExample", "NotificationID received: " + notificationID);

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            String launchUrl = result.notification.payload.launchURL; // update docs launchUrl

            String customKey;
            Integer id;
            String openURL = null;
            Object activityToLaunch = MainActivity.class;

//            Intent intent1 = new Intent(getApplicationContext(), ReceiveNotificationActivity.class);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent1);

            if (data != null) {
                customKey = data.optString("type", "");
                id = data.optInt("objectId", 0);

                switch (customKey) {

                    case "REG_APPROVED": {
                        Intent intent = new Intent(getApplicationContext(), BasicInformation2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(ConstantsApp.KEY_VALUES_REG_APPROVED, id);
                        startActivity(intent) ;
                    }
                     break;

                    case "NEW_PROJECT": {
                        Intent intent = new Intent(getApplicationContext(), BasicInformation2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id);
                        intent.putExtra(ConstantsApp.KEY_VALUES_NEW_PROJECT, id);
                        startActivity(intent) ;
                    }
                    break;

                    case "NEW_TRIP" : {
                        Intent intent = new Intent(getApplicationContext(), DetailTripActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra(ConstantsApp.KEY_VALUES_ID, id);
                        intent.putExtra(ConstantsApp.KEY_VALUES_HIDE, id);
                        startActivity(intent) ;
                    }
                    break;

                }
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID);

                if (result.action.actionID.equals("id1")) {
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
                    activityToLaunch = LoginActivity.class;
                } else
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID);
            }

            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
//            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
//            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra("openURL", openURL);
//            Log.i("OneSignalExample", "openURL = " + openURL);
//            // startActivity(intent);
//            startActivity(intent);

            // Add the following to your AndroidManifest.xml to prevent the launching of your main Activity
            //   if you are calling startActivity above.
        /*
           <application ...>
             <meta-data android:name="com.onesignal.NotificationOpened.DEFAULT" android:value="DISABLE" />
           </application>
        */
        }
    }


}

