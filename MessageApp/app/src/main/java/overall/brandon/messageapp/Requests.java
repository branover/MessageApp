package overall.brandon.messageapp;

import android.util.Log;

import java.net.Socket;

/**
 * Created by Brandon on 10/21/2016.
 */

public final class Requests {

    public static boolean registerUser(User user) {
        Client client = new Client();
        client.execute();
        Log.e("Response: ",client.getResponse());

        return true;
    }
}
