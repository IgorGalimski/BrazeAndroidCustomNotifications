package com.igorgalimski.appboycustomandroidnotifications;

import android.content.Context;
import android.content.res.Resources;

public final class NotificationViewIds
{
    public final int BackgroundViewId;

    public NotificationViewIds(Context context)
    {
        Resources resources = context.getResources();
        String packageName = context.getPackageName();

        BackgroundViewId = resources.getIdentifier("push_background", "id", packageName);
    }
}
