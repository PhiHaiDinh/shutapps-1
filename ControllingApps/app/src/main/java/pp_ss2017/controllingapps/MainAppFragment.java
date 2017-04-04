package pp_ss2017.controllingapps;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;

public class MainAppFragment extends ListFragment {

    private PackageManager packageManager = null;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ApplicationAdapter adapter = new ApplicationAdapter(getActivity(), values);
        packageManager = getActivity().getPackageManager();
        setListAdapter(adapter);
    }
}
