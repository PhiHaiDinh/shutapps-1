package pp_ss2017.controllingapps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DucGiang on 09.04.2017.
 */

public class DetectionService extends Service {

    private String packageName1 = "com.google.android.youtube";
    private String packageName2 = "pp_ss2017.controllingapps";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(DetectionService.this);
                for(int i=0; i<processes.size(); i++) {
                    AndroidAppProcess process = processes.get(i);
                    String processName = process.name;
                    if(packageName1.equalsIgnoreCase(processName)) {
                        //Toast.makeText(DetectionService.this.getApplicationContext(), "blocked", Toast.LENGTH_SHORT).show();
                        Log.d("news", "blocked");
                        Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                        startHomescreen.addCategory(Intent.CATEGORY_HOME);
                        startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startHomescreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startHomescreen);
                    }
                    Log.d("Test", processName);
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
}
