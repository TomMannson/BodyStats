package com.tommannson.bodystats.infrastructure.notifications.notificationorganizer

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.tommannson.bodystats.R
import com.tommannson.bodystats.feature.MainActivity
import com.tommannson.bodystats.infrastructure.notifications.AlertPointerUpdater
import com.tommannson.bodystats.model.reminding.ReminderType
import com.tommannson.bodystats.model.reminding.notificationId
import com.tommannson.bodystats.model.reminding.text

class NotificationInvoker {
    fun showPaymentNotification(ctx: Context, types: List<ReminderType>) {

        for (type in types) {


            ctx.createGroupName(NOTIFICATION_GROUP, "Measurements")

            val channelId =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.createImportantNotificationChannel(
                        AlertPointerUpdater.MEASUREMENTS_CHANNEL_ID,
                        "BodyStats Notification"
                    )
                } else {
                    // If earlier version channel ID is not used
                    // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                    AlertPointerUpdater.MEASUREMENTS_CHANNEL_ID
                }
            val mBuilder = NotificationCompat.Builder(
                ctx.applicationContext,
                channelId
            )
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle(ctx.getString(R.string.app_name))
                .setContentText(type.text)
                .setGroup(NOTIFICATION_GROUP)

            val appIntent = Intent(ctx, MainActivity::class.java)
            val pi = PendingIntent.getActivity(
                ctx,
                0,
                appIntent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
            mBuilder.setContentIntent(pi)
            val mNotifyMgr =
                ctx.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotifyMgr.notify(type.notificationId, mBuilder.build())
        }
    }

    private fun Context.createGroupName(s: String, s1: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val groupId = s
// The user-visible name of the group.
            val groupName = s1
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))
        }
    }

    fun buildForegroundNotification(ctx: Context): Notification {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ctx.createNotificationChannel(
                    AlertPointerUpdater.ALERT_STATS_CHANNEL_ID,
                    "BodyStats Background Service"
                )
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                AlertPointerUpdater.ALERT_STATS_CHANNEL_ID
            }
        val b = NotificationCompat.Builder(ctx, channelId)
        b.setOngoing(true)
            .setContentTitle("Praca w tle")
            .setContentText("BodyStats is managing your measurements")
            .setSmallIcon(R.drawable.ic_baseline_add_circle_24)
            .setTicker("Praca w tle")
        return b.build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Context.createImportantNotificationChannel(
        channelId: String,
        channelName: String
    ): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Context.createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    companion object {
        const val WEIGHT_NOTIFICATION_ID = 1
        const val MEASUREMENTS_NOTIFICATION_ID = 2
        const val COMPOSITION_NOTIFICATION_ID = 3
        private const val NOTIFICATION_GROUP = "STATS_GROUP"
    }
}