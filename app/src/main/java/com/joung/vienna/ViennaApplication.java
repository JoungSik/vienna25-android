package com.joung.vienna;

import android.app.Application;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class ViennaApplication extends Application {

    private static final String TAG = ViennaApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.vienna_notification_channel_topic))
                .addOnCompleteListener(task ->
                        Log.v(TAG, "subscribe - " + task.isSuccessful()));
    }

}
