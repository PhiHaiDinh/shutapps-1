package pp_ss2017.controllingapps.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import pp_ss2017.controllingapps.R;

/**
 * Created by DucGiang on 28.04.2017.
 */

public class AppListAdapter extends ArrayAdapter<ApplicationInfo> {

    private static final String TAG = "AppListAdapter";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private PackageManager packageManager;
    private Context context;
    private List<ApplicationInfo> appList;
    private String profileID;
    private String tempProfileID = "1350028491755775";

    public AppListAdapter(Context context, int textViewResourceId, List<ApplicationInfo> appList, String profileID) {
        super(context, textViewResourceId, appList);
        this.context = context;
        this.appList = appList;
        this.profileID = profileID;
        packageManager = context.getPackageManager();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        final int clickPosition = position;
        final ApplicationInfo applicationInfo = appList.get(position);

        if(listView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.app_listlayout, null);
        }

        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Position: " + clickPosition);
                Log.d(TAG, profileID);
                final String dbKey = "blacklist" + tempProfileID;

                myRef.child(dbKey).child(applicationInfo.loadLabel(packageManager).toString()).setValue(applicationInfo.packageName);
            }
        });

        if(applicationInfo != null) {
            TextView appName = (TextView) listView.findViewById(R.id.app_name);
            ImageView iconView = (ImageView) listView.findViewById(R.id.app_icon);

            appName.setText(applicationInfo.loadLabel(packageManager));
            iconView.setImageDrawable(applicationInfo.loadIcon(packageManager));
        }

        return listView;
    }
}
