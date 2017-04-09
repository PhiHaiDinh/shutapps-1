package pp_ss2017.controllingapps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainBLFragment extends Fragment {

    private ViewGroup root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_main_bl, null);
        return root;
    }

    public void setMessage(String appName) {
        TextView txt = (TextView) this.getView().findViewById(R.id.textView1);
        txt.setText("basfaoufgbouagnoa");
        Log.d("Hello", appName);
    }
}
