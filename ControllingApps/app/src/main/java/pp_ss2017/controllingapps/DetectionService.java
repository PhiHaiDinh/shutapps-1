package pp_ss2017.controllingapps;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DucGiang on 09.04.2017.
 */

public class DetectionService extends Service {

    private String packageName1 = "com.google.android.youtube";
    private String packageName2 = "com.facebook.katana";
    private String packageName3 = "com.instagram.android";
    private String packageName4 = "pp_ss2017.controllingapps";

    private Context appContext;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        appContext = getBaseContext();

        final List<String> blockedApps = new ArrayList<String>();
        blockedApps.add(packageName1);
        blockedApps.add(packageName2);
        blockedApps.add(packageName3);
        Log.d("Test", blockedApps.toString());

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(DetectionService.this);
                for(int i=0; i<blockedApps.size(); i++) {
                    for(int j=0; j<processes.size(); j++) {
                        AndroidAppProcess process = processes.get(j);
                        String processName = process.name;
                        if (blockedApps.get(i).equalsIgnoreCase(processName)) {
                            //Toast.makeText(DetectionService.this.getApplicationContext(), "blocked", Toast.LENGTH_SHORT).show();
                            Log.d("news", "blocked");
                            shortToast(processName);
                            Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                            startHomescreen.addCategory(Intent.CATEGORY_HOME);
                            startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startHomescreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startHomescreen);
                        }
                        Log.d("Test", processName);
                    }
                }
            }
        }, 2000, 1000);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    public void shortToast(String toast) {
        final String tempString = toast;

        if(appContext!=null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(appContext, tempString, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
