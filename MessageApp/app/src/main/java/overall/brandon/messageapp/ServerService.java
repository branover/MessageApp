package overall.brandon.messageapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Brandon on 10/29/2016.
 */

public class ServerService extends Service {

    private int port;
    private final IBinder binder = new LocalBinder();

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

    public void startServer(Context context, int port) {
        Toast.makeText(context, "Started Server", Toast.LENGTH_SHORT).show();
        Server server = new Server();
        server.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,String.valueOf(port));
    }

    public void stopServer(Context context) {
        Toast.makeText(context, "Stopped Server", Toast.LENGTH_SHORT).show();
    }

}
