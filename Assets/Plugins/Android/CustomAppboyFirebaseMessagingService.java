package com.igorgalimski.appboycustomandroidnotifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.appboy.Appboy;
import com.appboy.AppboyFirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class CustomAppboyFirebaseMessagingService extends AppboyFirebaseMessagingService 
{
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) 
    {
        try
        {
            Appboy.setCustomAppboyNotificationFactory(new CustomAppboyNotificationFactory());
        }
        catch (Exception exception)
        {
            Log.i(CustomAppboyUnityPlayerActivity.LOG_TAG, "Set appboy custom factory error: " + exception.getMessage());
        }
        
        super.onMessageReceived(remoteMessage);
    }
}
