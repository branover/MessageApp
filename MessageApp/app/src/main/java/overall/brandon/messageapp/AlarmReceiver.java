package overall.brandon.messageapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Brandon on 10/27/2016.
 */


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getBundleExtra("bundle");
        User user = (User) b.getSerializable("User");
        Requests.keepalive(user);

    }
}