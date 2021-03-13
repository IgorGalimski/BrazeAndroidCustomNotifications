package com.igorgalimski.appboycustomandroidnotifications;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.appboy.Appboy;
import com.appboy.Constants;
import com.appboy.IAppboyNotificationFactory;
import com.appboy.configuration.AppboyConfigurationProvider;
import com.appboy.enums.AppboyViewBounds;
import com.appboy.models.push.BrazeNotificationPayload;
import com.appboy.push.AppboyNotificationActionUtils;
import com.appboy.push.AppboyNotificationStyleFactory;
import com.appboy.push.AppboyNotificationUtils;
import com.appboy.support.AppboyLogger;

public class CustomAppboyNotificationFactory implements IAppboyNotificationFactory
{
    private static final String TAG = AppboyLogger.getAppboyLogTag(CustomAppboyNotificationFactory.class);
    private static final String BACKGROUND_ID = "ab_sbg";
    private static final byte DOWNLOADING_ATTEMPT = 3;

    @Override
    public Notification createNotification(AppboyConfigurationProvider appboyConfigurationProvider, Context context, Bundle notificationExtras, Bundle appboyExtras)
    {
        return createNotification(new BrazeNotificationPayload(context, appboyConfigurationProvider, notificationExtras, appboyExtras));
    }

    @Override
    public Notification createNotification(BrazeNotificationPayload brazeNotificationPayload)
    {
        NotificationCompat.Builder builder = populateNotificationBuilder(brazeNotificationPayload);
        CreateCustomNotification(builder, brazeNotificationPayload.getContext(), brazeNotificationPayload);

        return CreateNotification(builder);
    }

    private Notification CreateNotification(NotificationCompat.Builder builder)
    {
        if (builder != null)
        {
            return builder.build();
        }
        else
        {
            AppboyLogger.i(TAG, "Notification could not be built. Returning null as created notification");
            return null;
        }
    }

    private static void CreateCustomNotification(NotificationCompat.Builder builder, Context context, BrazeNotificationPayload brazeNotificationPayload)
    {
        try
        {
            CreateCustomNotification(
                builder,
                context,
                brazeNotificationPayload,
                GetValueFromExtras(brazeNotificationPayload.getAppboyExtras(), BACKGROUND_ID),
                GetValueFromExtras(brazeNotificationPayload.getAppboyExtras(), Constants.APPBOY_PUSH_BIG_IMAGE_URL_KEY),
                GetValueFromExtras(brazeNotificationPayload.getNotificationExtras(), Constants.APPBOY_PUSH_TITLE_KEY),
                GetValueFromExtras(brazeNotificationPayload.getNotificationExtras(), Constants.APPBOY_PUSH_CONTENT_KEY));
        }
        catch (Exception e)
        {
            AppboyLogger.w(TAG, e);
        }
    }

    private static void CreateCustomNotification(
        NotificationCompat.Builder notificationBuilder,
        Context context,
        BrazeNotificationPayload payload,
        String contentImageUri,
        String bigContentImageUri,
        String title,
        String message) {

        if (contentImageUri == null)
        {
            AppboyNotificationStyleFactory.setStyleIfSupported(notificationBuilder, payload);
            
            return;
        }

        NotificationResourceIds resourceIds = new NotificationResourceIds(context);
        NotificationViewIds viewIds = new NotificationViewIds(context);
        Bitmap contentImage = DownloadImage(context, payload.getAppboyExtras(), contentImageUri);
        boolean contentImageLoaded = contentImage != null;

        RemoteViews contentView = CreateContentView(
            context,
            resourceIds,
            viewIds,
            contentImageLoaded
                ? contentImage
                : BitmapFactory.decodeResource(context.getResources(), resourceIds.DefaultContentImageId));

        notificationBuilder
            .setContentTitle(title)
            .setContentText(message)
            .setCustomContentView(contentView);

        if (bigContentImageUri == null || !contentImageLoaded)
        {
            return;
        }

        Bitmap bigContentImage = DownloadImage(context, payload.getAppboyExtras(), bigContentImageUri);
        if (bigContentImage == null)
        {
            return;
        }

        RemoteViews bigContentView = CreateContentView(context, resourceIds, viewIds, bigContentImage);

        notificationBuilder.setCustomBigContentView(bigContentView);
    }

    private static RemoteViews CreateContentView(
        Context context,
        NotificationResourceIds resourceIds,
        NotificationViewIds viewIds,
        Bitmap contentImage)
    {
        RemoteViews contentView = new RemoteViews(context.getPackageName(), resourceIds.LayoutId);

        contentView.setImageViewBitmap(viewIds.BackgroundViewId, contentImage);

        return contentView;
    }

    private static NotificationCompat.Builder populateNotificationBuilder(BrazeNotificationPayload payload) 
    {
        AppboyLogger.v(TAG, "Using BrazeNotificationPayload: " + payload);
        Context context = payload.getContext();
        if (context == null) 
        {
            AppboyLogger.d(TAG, "BrazeNotificationPayload has null context. Not creating notification");
            return null;
        } 
        else 
        {
            AppboyConfigurationProvider appboyConfigurationProvider = payload.getAppboyConfigurationProvider();
            if (appboyConfigurationProvider == null) 
            {
                AppboyLogger.d(TAG, "BrazeNotificationPayload has null app configuration provider. Not creating notification");
                return null;
            } 
            else 
             {
                Bundle notificationExtras = payload.getNotificationExtras();
                AppboyNotificationUtils.prefetchBitmapsIfNewlyReceivedStoryPush(context, notificationExtras, payload.getAppboyExtras());
                String notificationChannelId = AppboyNotificationUtils.getOrCreateNotificationChannelId(payload);
                NotificationCompat.Builder notificationBuilder = (new NotificationCompat.Builder(context, notificationChannelId)).setAutoCancel(true);
                AppboyNotificationUtils.setTitleIfPresent(notificationBuilder, payload);
                AppboyNotificationUtils.setContentIfPresent(notificationBuilder, payload);
                AppboyNotificationUtils.setTickerIfPresent(notificationBuilder, payload);
                AppboyNotificationUtils.setSetShowWhen(notificationBuilder, payload);
                AppboyNotificationUtils.setContentIntentIfPresent(context, notificationBuilder, notificationExtras);
                AppboyNotificationUtils.setDeleteIntent(context, notificationBuilder, notificationExtras);
                AppboyNotificationUtils.setSmallIcon(appboyConfigurationProvider, notificationBuilder);
                AppboyNotificationUtils.setLargeIconIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setSoundIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setSummaryTextIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setPriorityIfPresentAndSupported(notificationBuilder, notificationExtras);
                
                AppboyNotificationActionUtils.addNotificationActions(context, notificationBuilder, notificationExtras);
                AppboyNotificationUtils.setAccentColorIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setCategoryIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setVisibilityIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setPublicVersionIfPresentAndSupported(notificationBuilder, payload);
                AppboyNotificationUtils.setNotificationBadgeNumberIfPresent(notificationBuilder, payload);
                
                return notificationBuilder;
            }
        }
    }
    
    private static Bitmap DownloadImage(Context context, Bundle extras, String imageURI)
    {
        for (int i = 0; i < DOWNLOADING_ATTEMPT; i++)
        {
            Bitmap bitmap = Appboy.getInstance(context).getAppboyImageLoader().getPushBitmapFromUrl(context, extras, imageURI, AppboyViewBounds.NO_BOUNDS);
            if(bitmap != null)
            {
                return bitmap;
            }
        }
        
        return null;
    }

    private static String GetValueFromExtras(Bundle bundle, String key)
    {
        return bundle.getString(key);
    }
}