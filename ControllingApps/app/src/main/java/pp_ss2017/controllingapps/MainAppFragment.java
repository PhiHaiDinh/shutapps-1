package pp_ss2017.controllingapps;

import android.app.ProgressDialog;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainAppFragment extends ListFragment {

    private PackageManager packageManager;
    private List<ApplicationInfo> appList;
    private ApplicationAdapter listAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        packageManager = getActivity().getPackageManager();

        new LoadApplications().execute();
    }

    /*@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = appList.get(position);
        try {
            Intent intent = packageManager.getLaunchIntentForPackage(app.packageName);
            if(intent != null) {
                startActivity(intent);
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }*/

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

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress;

        @Override
        protected Void doInBackground(Void... params) {
            appList = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listAdapter = new ApplicationAdapter(getActivity(), R.layout.listlayout, appList);

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
