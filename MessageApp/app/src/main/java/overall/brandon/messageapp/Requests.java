package overall.brandon.messageapp;

import android.util.Log;

import java.net.Socket;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brandon on 10/21/2016.
 */

public final class Requests {

    public static String registerUser(User user) {
        Client client = new Client();
        String test = "FAILED";
        try {
            test = client.execute("ALIAS:"+user.getAlias(),"ID:"+user.getAndroidId(),"IP:"+user.getIp(),"PORT:"+String.valueOf(user.getPort())).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test;
        }
    }
}
