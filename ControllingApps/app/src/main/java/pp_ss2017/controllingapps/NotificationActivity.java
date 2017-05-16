package pp_ss2017.controllingapps;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pp_ss2017.controllingapps.adapters.DialogAdapter;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView txtView;
    private NotificationReceiver notificationReceiver;

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private AlertDialog enableNotificationListenerAlertDialog;

    private DialogAdapter dialogAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        initButton();

        txtView = (TextView) findViewById(R.id.textView);
        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("pp_ss2017.controllingapps.NOTIFICATION_LISTENER_SERVICE");
        registerReceiver(notificationReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnCreateNotify:
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder nComp = new NotificationCompat.Builder(this);
                nComp.setContentTitle("New Notification");
                nComp.setContentText("Hallo, World?");
                nComp.setTicker("Hello");
                nComp.setSmallIcon(R.drawable.com_facebook_button_like_background);
                nComp.setAutoCancel(true);
                notificationManager.notify((int) System.currentTimeMillis(), nComp.build());

                break;
            case R.id.btnClearNotify:
                Intent intent = new Intent("pp_ss2017.controllingapps.NOTIFICATION_LISTENER");
                intent.putExtra("command", "block");
                sendBroadcast(intent);
                break;
            case R.id.btnListNotify:
                Intent intent1 = new Intent("pp_ss2017.controllingapps.NOTIFICATION_LISTENER");
                intent1.putExtra("command", "unblock");
                sendBroadcast(intent1);
                break;
        }
    }

    private boolean isNotificationServiceEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(), ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }

    public void initButton() {
        Button create = (Button) findViewById(R.id.btnCreateNotify);
        Button clear = (Button) findViewById(R.id.btnClearNotify);
        Button list = (Button) findViewById(R.id.btnListNotify);

        create.setOnClickListener(this);
        clear.setOnClickListener(this);
        list.setOnClickListener(this);
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null) {
                ArrayList<StatusBarNotification> savedNotifications = intent.getParcelableArrayListExtra("notifyList");
                if(savedNotifications != null) {
                    Log.d("saved", savedNotifications.toString());
                    Log.d("savedTitle", savedNotifications.get(0).getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString());
                    Log.d("savedText", savedNotifications.get(0).getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString());

                    Dialog dialog = new Dialog(NotificationActivity.this);
                    dialog.setContentView(R.layout.dialog_list);
                    dialog.setTitle("Notifications");
                    ListView listView = (ListView) dialog.findViewById(R.id.List);
                    dialogAdapter = new DialogAdapter(NotificationActivity.this, R.layout.dialog_listlayout, savedNotifications);
                    listView.setAdapter(dialogAdapter);
                    dialog.show();
                }
            }
        }
    }
}
