//package com.tommannson.bodystats.infrastructure.notifications.notificationorganizer;
//
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.content.ContextWrapper;
//import android.os.Build;
//
//import androidx.annotation.RequiresApi;
//
//import static com.tommannson.apps.paimentorganizer.utils.notifications.AlertPointerUpdater.ALERT_CRON_CHANNEL_ID;
//import static com.tommannson.apps.paimentorganizer.utils.notifications.AlertPointerUpdater.PAYMENTS_CHANNEL_ID;
//
//
//public class ChannelMaker{
//
//    NotificationChannelMaker api;
//
//    public ChannelMaker(Context context) {
//        this.api = new NotificationChannelMaker(context);
//    }
//
//    public void createChannels(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            api.createNotificationChannel();
//            api.createPaymentNotificationChannel();
//        }
//    }
//}
//
//class NotificationChannelMaker extends ContextWrapper {
//
//    public NotificationChannelMaker(Context base) {
//        super(base);
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    void createNotificationChannel() {
//            CharSequence name = "Other";
//            String description = "Notifications for system restrictions";
//            int importance = NotificationManager.IMPORTANCE_NONE;
//            NotificationChannel channel = new NotificationChannel(ALERT_CRON_CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    void createPaymentNotificationChannel() {
//            CharSequence name = "Payments Channel";
//            String description = "Notifications related to your payments";
//            int importance = NotificationManager.IMPORTANCE_HIGH;
//            NotificationChannel channel = new NotificationChannel(PAYMENTS_CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//    }
//}
