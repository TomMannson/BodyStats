//package com.tommannson.bodystats.infrastructure.notifications;
//
//import android.app.IntentService;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//
//import androidx.annotation.Nullable;
//
//import com.tommannson.apps.paimentorganizer.mvvmp.model.db.entity.HistoryPaimentDb;
//import com.tommannson.apps.paimentorganizer.mvvmp.model.db.entity.NotificationDataDb;
//import com.tommannson.apps.paimentorganizer.mvvmp.model.db.repo.NotificationRepo;
//import com.tommannson.apps.paimentorganizer.utils.notifications.alarms.AlarmCreator;
//import com.tommannson.apps.paimentorganizer.utils.notifications.notificationorganizer.NotificationInvoker;
//
//import org.joda.time.DateTime;
//import org.joda.time.Period;
//
//import java.util.Date;
//
//import io.realm.Realm;
//import io.realm.RealmResults;
//
//import static com.tommannson.apps.paimentorganizer.utils.notifications.AlertPointerUpdater.ALERT_CRON_ID;
//
//public class DailyRemainderService extends IntentService {
//
//    public static final String DAILY_INFO = "daily_info";
//    public static final String NEED_INITIALISATION = "daily_enabled";
//    private static int DAILY_SCHEDULER_JOB_ID = 2;
//
//
//    NotificationInvoker invoker = new NotificationInvoker();
//    NotificationRepo notificationRepository = new NotificationRepo();
//
//    public DailyRemainderService() {
//        super("Reminder_Service");
//    }
//
//
//
//
//    public static void startScheduleDaily(Context ctx){
//        startScheduleDaily(ctx, false);
//    }
//
//    public static void startScheduleDaily(Context ctx, boolean restartAfterBoot){
//        if(restartAfterBoot || initialisationNeeded(ctx)) {
//            AlarmCreator alarmCreator = new AlarmCreator();
//            DateTime timeOfAlarm = DateTime.now().withTimeAtStartOfDay().withHourOfDay(18).withMinuteOfHour(30);
//            alarmCreator.cancelAlarm(ctx, 2);
//            alarmCreator.setAlarmPeriodic(ctx, timeOfAlarm, Period.days(1), 2);
//            saveInitialisationFlag(ctx);
//        }
//    }
//
//    public static boolean initialisationNeeded(Context ctx){
//        SharedPreferences preferences = ctx.getSharedPreferences(DAILY_INFO, Context.MODE_PRIVATE);
//        return preferences.getBoolean(NEED_INITIALISATION, true);
//    }
//
//    public static boolean saveInitialisationFlag(Context ctx){
//        SharedPreferences preferences = ctx.getSharedPreferences(DAILY_INFO, Context.MODE_PRIVATE);
//        return preferences.edit().putBoolean(NEED_INITIALISATION, false).commit();
//    }
//
//
//    @Override
//    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        startForeground(ALERT_CRON_ID, invoker.buildForegroundNotification(this));
//        NotificationDataDb data = notificationRepository.getNotificationInfo(Realm.getDefaultInstance());
//        Realm realm = Realm.getDefaultInstance();
//
//        RealmResults<HistoryPaimentDb> result = realm.where(HistoryPaimentDb.class).equalTo("paid", false)
//                .findAllSorted("paimentTo");
//
//        if (result.size() > 0) {
//            if (result.get(0).willTriggerInFuture()) {
//                notificationRepository.clearDailyNotification(realm);
//                return;
//            }
//        }
//
//        if(data == null){
//            stopForeground(true);
//            return;
//        }
//        Date date = data.getTimeOfNotification();
//
//        DateTime lastNotificationTime = new DateTime(date);
//        DateTime nextDay = lastNotificationTime.withTimeAtStartOfDay().plusDays(1);
//
//        if(nextDay.isBeforeNow()){
//            invoker.showPaymentNotification(this);
//        }
//        stopForeground(true);
//    }
//}
