package pp_ss2017.controllingapps.adapters;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import pp_ss2017.controllingapps.R;


/**
 * Created by DucGiang on 02.05.2017.
 */

public class DialogAdapter extends ArrayAdapter<StatusBarNotification> {

    private static final String TAG = "DialogAdapter";

    private Context context;
    private ArrayList<StatusBarNotification> notifyList;

    public DialogAdapter(Context context, int textViewResourceId, ArrayList<StatusBarNotification> notifyList) {
        super(context, textViewResourceId, notifyList);
        this.context = context;
        this.notifyList = notifyList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if(listView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = layoutInflater.inflate(R.layout.dialog_listlayout, null);
        }

        StatusBarNotification statusBarNotification = notifyList.get(position);
        if(statusBarNotification != null) {
            ImageView appIcon = (ImageView) listView.findViewById(R.id.appIcon);
            TextView notifyTitle = (TextView) listView.findViewById(R.id.notifyTitle);
            TextView notifyText = (TextView) listView.findViewById(R.id.notifyText);

            String packageName = statusBarNotification.getPackageName();
            try {
                appIcon.setImageDrawable(context.getPackageManager().getApplicationIcon(packageName));
            } catch(PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            notifyTitle.setText(statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE));
            notifyText.setText(statusBarNotification.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT));
        }

        return listView;
    }
}
