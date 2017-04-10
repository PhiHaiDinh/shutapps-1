package pp_ss2017.controllingapps;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;
import com.jaredrummler.android.processes.models.AndroidProcess;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class TestActivity extends AppCompatActivity {

    private TextView txt;
    private PackageManager packageManager;
    private PackageInfo packageInfo;
    private ApplicationInfo appInfo;
    private String appName;
    private String packageName1 = "com.google.android.youtube";
    private String packageName2 = "pp_ss2017.controllingapps";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = new Intent(this, DetectionService.class);

        this.startService(intent);
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(this);
        for(int i=0; i<processes.size(); i++) {
            AndroidAppProcess process = processes.get(i);
            String processName = process.name;
            if(packageName2.equalsIgnoreCase(processName)) {
                Log.d("Block", processName +" wird geblockt!");
                Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                startHomescreen.addCategory(Intent.CATEGORY_HOME);
                startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startHomescreen);
            }
            Log.d("Test", processName);
        }

        if(AndroidProcesses.isMyProcessInTheForeground()) {
            Log.d("News", "ShutApps ist aktiv!");
        }
    }*/

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        txt = (TextView) this.findViewById(R.id.textView);
        packageManager = getApplicationContext().getPackageManager();
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        txt.setText(appName);

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
    }

    public void getTop(View view) {
        String topPackageName;

        UsageStatsManager mUsageStatsManager = (UsageStatsManager)getSystemService();
        long time = System.currentTimeMillis();
        // We get usage stats for the last 10 seconds
        List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);
        // Sort the stats by the last time used
        Log.d("Test1", stats.toString());
        if(stats != null) {
            SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
            for (UsageStats usageStats : stats) {
                mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
            }
            if(!mySortedMap.isEmpty()) {
                topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                Log.d("Test2", topPackageName);
            }
        }
    }*/
}
