package pp_ss2017.controllingapps;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DucGiang on 03.04.2017.
 */

public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo> {

    private static String TAG = "Test";

    private PackageManager packageManager;
    private Context context;
    private List<ApplicationInfo> appList;
    private ApplicationAdapterListener buttonListener;


    public ApplicationAdapter(Context context, int textViewResourceId, List<ApplicationInfo> appList, ApplicationAdapterListener buttonListener) {
        super(context, textViewResourceId, appList);
        this.context = context;
        this.appList = appList;
        this.buttonListener = buttonListener;
        packageManager = context.getPackageManager();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.listlayout, null);
        }

        final ApplicationInfo applicationInfo = appList.get(position);
        if(applicationInfo != null) {
            TextView appName = (TextView) listView.findViewById(R.id.app_name);
            ImageView iconView = (ImageView) listView.findViewById(R.id.app_icon);

            appName.setText(applicationInfo.loadLabel(packageManager));
            iconView.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }

        ImageButton imgButton = (ImageButton) listView.findViewById(R.id.addBtn);
        imgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //Log.d(TAG, applicationInfo.loadLabel(packageManager).toString());
                //Log.d(TAG, Integer.toString(position));
                buttonListener.buttonPressed(applicationInfo.loadLabel(packageManager).toString());
            }
        });

        return listView;
    }
}
