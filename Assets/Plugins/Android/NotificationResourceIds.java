package com.igorgalimski.appboycustomandroidnotifications;

import android.content.Context;
import android.content.res.Resources;

public class NotificationResourceIds 
{
    public final int LayoutId;
    public final int ApplicationIconId;
    public final int DefaultContentImageId;

    public NotificationResourceIds(Context context)
    {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        LayoutId = resources.getIdentifier("push_notification", "layout", packageName);
        ApplicationIconId = context.getApplicationInfo().icon;
        DefaultContentImageId = resources.getIdentifier("notification_background", "drawable", packageName);
    }
}
