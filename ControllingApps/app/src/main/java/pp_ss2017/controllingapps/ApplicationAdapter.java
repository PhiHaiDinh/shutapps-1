package pp_ss2017.controllingapps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DucGiang on 03.04.2017.
 */

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {
    private PackageManager packageManager;
    private Context context;
    private List<ApplicationInfo> appList = null;

    public ApplicationAdapter(Context context, List<ApplicationInfo> appList) {
        super(context, R.layout.listlayout, appList);
        this.context = context;
        this.appList = appList;
        packageManager = context.getPackageManager();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.listlayout, null);
        }
        ApplicationInfo applicationInfo = appList.get(position);
        if(applicationInfo != null) {
            TextView appName = (TextView) listView.findViewById(R.id.app_name);
            ImageView iconView = (ImageView) listView.findViewById(R.id.app_icon);

            appName.setText(applicationInfo.loadLabel(packageManager));
            iconView.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }

        return listView;
    }
}
