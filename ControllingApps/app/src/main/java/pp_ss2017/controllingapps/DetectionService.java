package pp_ss2017.controllingapps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by DucGiang on 09.04.2017.
 */

public class DetectionService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TODO do something useful
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
}
