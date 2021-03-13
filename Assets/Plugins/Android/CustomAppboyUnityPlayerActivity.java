package com.igorgalimski.appboycustomandroidnotifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.appboy.unity.AppboyUnityPlayerActivity;

public class CustomAppboyUnityPlayerActivity extends AppboyUnityPlayerActivity 
{
    private static final String APPBOY_OPENED_ACTION = "APPBOY_NOTIFICATION_OPENED";
    
    public static final String PREFERENCES_NAME = "PREFERENCES";
    public static final String ACTION_KEY = "APPBOY_OPENED_ACTION";
    public static final String DATA_KEY = "APPBOY_DATA";
    public static final String LOG_TAG = "CUSTOM NOTIFICATIONS";
    
    private static SharedPreferences _sharedPreferences;
    private static SharedPreferences.Editor _editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        try 
        {
            _sharedPreferences = this.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            _editor = _sharedPreferences.edit();
        }
        catch (Exception exception)
        {
            Log.i(LOG_TAG, "OnCreate error: " + exception.getMessage());
        }
    }

    public static boolean IsOpenedByRemotePush()
    {
        try
        {
            return _sharedPreferences.getString(ACTION_KEY, "").contains(APPBOY_OPENED_ACTION);
        }
        catch (Exception exception)
        {
            Log.i(LOG_TAG, "IsOpenedByRemotePush error: " + exception.getMessage());
        }
        
        return false;
    }
    
    public static String GetLastNotificationData()
    {
        try
        {
            return _sharedPreferences.getString(DATA_KEY, "");
        }
        catch (Exception exception)
        {
            Log.i(LOG_TAG, "GetLastNotificationData error: " + exception.getMessage());
        }
        
        return "";
    }
    
    public static void ClearOpenedState()
    {
        try
        {
            _editor.remove(ACTION_KEY);
            _editor.remove(DATA_KEY);
            _editor.apply();
        }
        catch (Exception exception)
        {
            Log.i(LOG_TAG, "ClearOpenedState error: " + exception.getMessage());
        }
    }
}