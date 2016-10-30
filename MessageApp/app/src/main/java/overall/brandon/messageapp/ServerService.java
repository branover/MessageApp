package overall.brandon.messageapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Brandon on 10/29/2016.
 */

public class ServerService extends Service {

    private int port;
    private final IBinder binder = new LocalBinder();

    public ServerService(int port) {
        this.port = port;
    }

    public class LocalBinder extends Binder {
        ServerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
