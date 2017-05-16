package pp_ss2017.controllingapps.activities;

import android.app.Dialog;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.RemoteException;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pp_ss2017.controllingapps.adapters.DialogAdapter;
import pp_ss2017.controllingapps.adapters.PagerAdapter;
import pp_ss2017.controllingapps.R;
import pp_ss2017.controllingapps.fragments.AppListFragment;
import pp_ss2017.controllingapps.services.BlockService;

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private NotificationReceiver notificationReceiver;

    private FirebaseAuth firebaseAuth;

    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    private AlertDialog enableNotificationListenerAlertDialog;
    private DialogAdapter dialogAdapter;

    private BeaconManager beaconManager;

    private String profileID;
    private List<String> personList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        firebaseAuth = firebaseAuth.getInstance();

        if (AccessToken.getCurrentAccessToken() == null) {
            goLoginActivity();
        }

        Intent intent = new Intent(MainActivity.this, BlockService.class);
        startService(intent);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Applist"));
        tabLayout.addTab(tabLayout.newTab().setText("Blacklist"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        notificationReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("pp_ss2017.controllingapps.NOTIFICATION_LISTENER_SERVICE");
        registerReceiver(notificationReceiver, filter);

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        Log.d("test", response.getJSONObject().toString());
                        try {
                            profileID = object.getString("id");
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }

                        Beacon beacon = new Beacon.Builder()
                                .setId1(toHex(profileID))
                                .setId2("1")
                                .setId3("2")
                                .setManufacturer(0x0118)
                                .setTxPower(-56)
                                .setDataFields(Arrays.asList(new Long[] {0l}))
                                .build();

                        BeaconParser beaconParser = new BeaconParser()
                                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
                        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
                        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {

                            @Override
                            public void onStartFailure(int errorCode) {
                                Log.e(TAG, "Advertisement start failed with code: "+errorCode);
                            }

                            @Override
                            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                                Log.i(TAG, "Advertisement start succeeded.");
                            }
                        });
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name");
        request.setParameters(parameters);
        request.executeAsync();

        beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                final Collection<Beacon> beacon = collection;

                if(beacon.size() > 0) {
                    String cleanID = cleanString(beacon.iterator().next().getId1().toString());
                    final String convertedID = convertHexToStringValue(cleanID);
                    final String fixedID = removeZero(convertedID);
                    Log.d("String", fixedID);

                    final Region singleBeaconRegion = new Region(beacon.iterator().next().toString(), beacon.iterator().next().getIdentifiers());

                    beaconManager.addMonitorNotifier(new MonitorNotifier() {
                        @Override
                        public void didEnterRegion(Region region) {
                            Log.d("Enter", "entered");
                            final GraphRequest friendRequest = GraphRequest.newMyFriendsRequest(
                                    AccessToken.getCurrentAccessToken(),
                                    new GraphRequest.GraphJSONArrayCallback() {
                                        @Override
                                        public void onCompleted(JSONArray jsonArray, GraphResponse response) {
                                            try {
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject obj = jsonArray.getJSONObject(i);
                                                    String name = obj.getString("name");
                                                    String id = obj.getString("id");

                                                    if (fixedID.equals(id)) {
                                                        if(!personList.contains(fixedID)) {
                                                            Log.d("abgleich", name + " ist ein Freund und in der Nähe!");
                                                            personList.add(fixedID);

                                                            if(personList.size() == 1) {

                                                                Intent blockIntent = new Intent("pp_ss2017.controllingapps.BLOCK_SERVICE");
                                                                blockIntent.putExtra("command", "block");
                                                                blockIntent.putExtra("id", fixedID);
                                                                sendBroadcast(blockIntent);

                                                                Intent unnotifyIntent = new Intent("pp_ss2017.controllingapps.NOTIFICATION_LISTENER");
                                                                unnotifyIntent.putExtra("command", "block");
                                                                unnotifyIntent.putExtra("id", fixedID);
                                                                sendBroadcast(unnotifyIntent);
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            friendRequest.executeAndWait();
                        }

                        @Override
                        public void didExitRegion(Region region) {
                            Log.d("Exit", "exited");

                            if(personList.contains(fixedID)) {
                                personList.remove(fixedID);

                                if (personList.isEmpty()) {
                                    personList.clear();

                                    Intent unblockIntent = new Intent("pp_ss2017.controllingapps.BLOCK_SERVICE");
                                    unblockIntent.putExtra("command", "unblock");
                                    sendBroadcast(unblockIntent);

                                    Intent notifyIntent = new Intent("pp_ss2017.controllingapps.NOTIFICATION_LISTENER");
                                    notifyIntent.putExtra("command", "unblock");
                                    sendBroadcast(notifyIntent);
                                }
                            }

                            try {
                                beaconManager.stopMonitoringBeaconsInRegion(singleBeaconRegion);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void didDetermineStateForRegion(int i, Region region) {

                        }
                    });

                        try {
                            beaconManager.startMonitoringBeaconsInRegion(singleBeaconRegion);
                        } catch(RemoteException e) {
                            e.printStackTrace();
                        }
                    }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myBeacon", null, null, null));
        } catch(RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
        unregisterReceiver(notificationReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                firebaseAuth.signOut();
                LoginManager.getInstance().logOut();
                AccessToken.setCurrentAccessToken(null);
                goLoginActivity();

                return true;
            case R.id.action_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClicked(View view) {
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.image_click));
        Intent intent = new Intent(MainActivity.this, FriendsActivity.class);
        startActivity(intent);
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

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public String toHex(String arg) {
        return String.format("%032x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }

    public static String convertHexToStringValue(String hex) {
        StringBuilder stringbuilder = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            stringbuilder.append((char)decimal);
        }
        return stringbuilder.toString();
    }

    public static String cleanString(String id) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] parts = id.split("-");
        for(int i=0; i<parts.length; i++) {
            stringBuilder.append(parts[i]);
        }
        return stringBuilder.toString();
    }

    public String removeZero(String string) {
        char[] tempCharArray = string.toCharArray();
        List<Character> cList = new ArrayList<Character>();
        for(char c : tempCharArray) {
            cList.add(c);
        }
        int i = 0;
        while(cList.get(i) == 0) {
            cList.remove(i);
            i = i + 1;
        }
        StringBuilder builder = new StringBuilder(cList.size());
        for(Character ch: cList) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public String getProfileID() {
        return profileID;
    }

    class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent != null) {
                ArrayList<StatusBarNotification> savedNotifications = intent.getParcelableArrayListExtra("notifyList");
                if(savedNotifications != null) {
                    Log.d("saved", savedNotifications.toString());

                    Dialog dialog = new Dialog(MainActivity.this);
                    dialog.setContentView(R.layout.dialog_list);
                    dialog.setTitle("Notifications");
                    ListView listView = (ListView) dialog.findViewById(R.id.List);
                    dialogAdapter = new DialogAdapter(MainActivity.this, R.layout.dialog_listlayout, savedNotifications);
                    listView.setAdapter(dialogAdapter);
                    dialog.show();
                }
            }
        }
    }
}
