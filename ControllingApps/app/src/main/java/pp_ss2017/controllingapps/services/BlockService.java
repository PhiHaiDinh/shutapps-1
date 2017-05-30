package pp_ss2017.controllingapps.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DucGiang on 09.04.2017.
 */

public class BlockService extends Service {

    private static final String TAG = "BlockService";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private Context appContext;

    private Handler handler;
    private Timer timer;

    private BlockServiceReceiver blockServiceReceiver;

    private Set blockedApps = new HashSet();

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        blockServiceReceiver = new BlockServiceReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("pp_ss2017.controllingapps.BLOCK_SERVICE");
        registerReceiver(blockServiceReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        appContext = getBaseContext();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(BlockService.this);
                for(int i=0; i<processes.size(); i++) {
                    AndroidAppProcess process = processes.get(i);
                    String processName = process.name;
                    if (blockedApps.contains(processName)) {
                        //Toast.makeText(BlockService.this.getApplicationContext(), "blocked", Toast.LENGTH_SHORT).show();
                        shortToast(processName);
                        Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                        startHomescreen.addCategory(Intent.CATEGORY_HOME);
                        startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startHomescreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startHomescreen);
                    }
                    Log.d(TAG, processName);
                }
            }
        }, 2000, 2000);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        timer.cancel();
        super.onDestroy();
        unregisterReceiver(blockServiceReceiver);
    }

    public void shortToast(String toast) {
        final String tempString = toast;

        if(appContext!=null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appContext, tempString, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class BlockServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("block")) {
                Log.d(TAG, "Apps werden zur Blacklist hinzugefügt.");

                String dbKey = "blacklist" + intent.getStringExtra("id");

                myRef.child(dbKey).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        blockedApps.add(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        blockedApps.remove(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else if(intent.getStringExtra("command").equals("unblock")){
                Log.d(TAG, "Blacklist wird geleert.");
                blockedApps.clear();
            }
        }
    }
}
