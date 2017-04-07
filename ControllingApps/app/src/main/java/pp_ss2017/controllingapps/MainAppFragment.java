package pp_ss2017.controllingapps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainAppFragment extends ListFragment implements ApplicationAdapterListener {

    private PackageManager packageManager;
    private List<ApplicationInfo> appList;
    private ApplicationAdapter listAdapter;
    private AppInfoListener appListener;

    private ApplicationAdapterListener temp;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            appListener = (AppInfoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement sendAppData");
        }

    }

    @Override
    public void onDetach() {
        appListener = null;
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        packageManager = getActivity().getPackageManager();
        temp = this;

        new LoadApplications().execute();
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
        ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
        for(ApplicationInfo info : list) {
            try {
                if(packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    @Override
    public void buttonPressed(String appName) {
        //Toast.makeText(getActivity(),   "click ok button at " + position, Toast.LENGTH_SHORT).show();
        //Toast.makeText(getActivity(), appName, Toast.LENGTH_SHORT).show();
        appListener.sendAppData(appName);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_bl, new MainBLFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress;

        @Override
        protected Void doInBackground(Void... params) {
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listAdapter = new ApplicationAdapter(getActivity(), R.layout.listlayout, appList, temp);

            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listAdapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), null, "Loading application info...");
            super.onPreExecute();
        }
    }
}
