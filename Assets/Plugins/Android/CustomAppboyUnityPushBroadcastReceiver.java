package com.igorgalimski.appboycustomandroidnotifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.appboy.unity.AppboyUnityPushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class CustomAppboyUnityPushBroadcastReceiver extends AppboyUnityPushBroadcastReceiver
{
    private static final String APPBOY_PUSH_TITLE_KEY = "t";
    private static final String APPBOY_PUSH_CONTENT_KEY = "a";
    private static final String JSON_TITLE_KEY = "title";
    private static final String JSON_CONTENT_KEY = "content";
    private static final String JSON_DATA_KEY = "date";
    private static final String APPBOY_PUSH_EXTRAS_KEY = "extra";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            SharedPreferences sharedpreferences = context.getSharedPreferences(CustomAppboyUnityPlayerActivity.PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            SaveAction(intent, editor);
            SaveData(intent, editor);

            editor.apply();
        }
        catch (Exception exception)
        {
            Log.i(CustomAppboyUnityPlayerActivity.LOG_TAG, "OnCreate error: " + exception.getMessage());
        }
    }

    private void SaveAction(Intent intent, SharedPreferences.Editor editor)
    {
        String action = intent.getAction();

        editor.putString(CustomAppboyUnityPlayerActivity.ACTION_KEY, action);
    }

    private void SaveData(Intent intent, SharedPreferences.Editor editor)
    {
        Bundle extras = intent.getExtras();

        String title = extras.getString(APPBOY_PUSH_TITLE_KEY);
        String content = extras.getString(APPBOY_PUSH_CONTENT_KEY);

        Bundle dataExtras = intent.getBundleExtra(APPBOY_PUSH_EXTRAS_KEY);

        JSONObject json = new JSONObject();
        
        try 
        {
            Set<String> keys = dataExtras.keySet();
            for (String key : keys)
            {
                json.put(key, JSONObject.wrap(dataExtras.get(key)));
            }
    
            json.put(JSON_TITLE_KEY, title);
            json.put(JSON_CONTENT_KEY, content);
            json.put(JSON_DATA_KEY, getCurrentTimeStamp());
        }
        catch (JSONException e) 
        {
        }
        
        editor.putString(CustomAppboyUnityPlayerActivity.DATA_KEY, json.toString());
    }
    
    private String getCurrentTimeStamp() 
    {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}