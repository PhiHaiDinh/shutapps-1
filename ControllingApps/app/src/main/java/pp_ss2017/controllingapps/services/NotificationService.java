package pp_ss2017.controllingapps.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NotificationService extends NotificationListenerService {

    public static final String TAG = "NotificationService";

    private NotificationListenerServiceReceiver notificationListenerServiceReceiver;

    private String packageName1 = "com.google.android.youtube";
    private String packageName2 = "com.facebook.katana";
    private String packageName3 = "com.instagram.android";
    private String packageName4 = "com.whatsapp";
    private String myPackageName = "pp_ss2017.controllingapps";

    private List<String> blockedNotifications = new ArrayList<String>();
    private ArrayList<StatusBarNotification> savedNotifications = new ArrayList<StatusBarNotification>();

    @Override
    public void onCreate() {
        super.onCreate();
        notificationListenerServiceReceiver = new NotificationListenerServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("pp_ss2017.controllingapps.NOTIFICATION_LISTENER");
        registerReceiver(notificationListenerServiceReceiver, intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification){
        String packageName = statusBarNotification.getPackageName();

        for(String pkg : blockedNotifications) {
            if(packageName.equals(pkg)) {
                savedNotifications.add(statusBarNotification);
                cancelNotification(statusBarNotification.getKey());
                //Log.d(TAG, savedNotifications.get(i).getNotification().tickerText.toString());
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification statusBarNotification){

    }

    class NotificationListenerServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("block")) {
                Log.d(TAG, "blocked");
                blockedNotifications.add(packageName1);
                blockedNotifications.add(packageName2);
                blockedNotifications.add(packageName3);
                //blockedNotifications.add(packageName4);
                blockedNotifications.add(myPackageName);
            }
            else if(intent.getStringExtra("command").equals("unblock")) {
                Log.d(TAG, "unblocked");
                blockedNotifications.clear();

                Intent notifyIntent = new Intent("pp_ss2017.controllingapps.NOTIFICATION_LISTENER_SERVICE");
                notifyIntent.putParcelableArrayListExtra("notifyList", savedNotifications);
                sendBroadcast(notifyIntent);

                savedNotifications.clear();
            }
        }
    }
}
