package pp_ss2017.controllingapps.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pp_ss2017.controllingapps.R;

public class BlackListFragment extends Fragment implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_black_list, container, false);

        Button create = (Button) view.findViewById(R.id.createbl);
        Button clear = (Button) view.findViewById(R.id.clearbl);

        create.setOnClickListener(this);
        clear.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.createbl:

                break;
            case R.id.clearbl:
                break;
            default:
                break;
        }
    }
}
