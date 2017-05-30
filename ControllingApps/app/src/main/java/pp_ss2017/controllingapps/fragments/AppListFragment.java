package pp_ss2017.controllingapps.fragments;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

import pp_ss2017.controllingapps.R;
import pp_ss2017.controllingapps.activities.MainActivity;
import pp_ss2017.controllingapps.adapters.AppListAdapter;

public class AppListFragment extends ListFragment {

    private static final String TAG = "AppListFragment";

    private PackageManager packageManager;
    private List<ApplicationInfo> appList;
    private AppListAdapter appListAdapter;
    private IDReceiver idReceiver;

    private String profileID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idReceiver = new IDReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("pp_ss2017.controllingapps.APPLIST_IDRECEIVER");
        getActivity().registerReceiver(idReceiver, filter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        packageManager = getActivity().getPackageManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(idReceiver);
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
        for(ApplicationInfo info : list) {
            try {
                if(packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Void... params) {
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            appListAdapter = new AppListAdapter(getActivity(), R.layout.app_listlayout, appList, profileID);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(appListAdapter);
            progressDialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), null, "Loading application info...");
            super.onPreExecute();
        }
    }

    class IDReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            profileID = intent.getStringExtra("id");
            Log.d(TAG, profileID);

            new LoadApplications().execute();
        }
    }
}
