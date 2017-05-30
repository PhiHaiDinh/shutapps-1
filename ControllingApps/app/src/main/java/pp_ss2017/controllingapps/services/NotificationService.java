package pp_ss2017.controllingapps.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotificationService extends NotificationListenerService {

    public static final String TAG = "NotificationService";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private NotificationListenerServiceReceiver notificationListenerServiceReceiver;

    private Set blockedNotifications = new HashSet();
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

        for(Object pkg : blockedNotifications) {
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

                String dbKey = "blacklist" + intent.getStringExtra("id");

                myRef.child(dbKey).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        blockedNotifications.add(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        blockedNotifications.remove(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
