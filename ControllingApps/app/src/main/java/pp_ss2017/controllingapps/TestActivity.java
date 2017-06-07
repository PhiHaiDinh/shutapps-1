package pp_ss2017.controllingapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pp_ss2017.controllingapps.services.BlockService;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivityForResult(intent, 0);
    }

    public void onClick(View view) {
        Intent unblockIntent = new Intent("pp_ss2017.controllingapps.BLOCK_SERVICE");
        unblockIntent.putExtra("command", "unblock");
        unblockIntent.putExtra("id", "1350028491755775");
        sendBroadcast(unblockIntent);
    }
}
