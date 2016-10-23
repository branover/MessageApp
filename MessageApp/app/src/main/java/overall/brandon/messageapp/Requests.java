package overall.brandon.messageapp;

import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brandon on 10/21/2016.
 */

public final class Requests {

    public static String registerUser(User user) {
        Client client = new Client();
        String test = "FAILED";
        try {
            test = client.execute("Register", new ArrayList<String>(Arrays.asList("ALIAS:"+user.getAlias(),"ID:"+user.getAndroidId(),"IP:"+user.getIp(),"PORT:"+String.valueOf(user.getPort())))).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test;
        }
    }

    public static String updateUser(User user) {
        Client client = new Client();
        String test = "FAILED";
        try {
            test = client.execute("Update", new ArrayList<String>(Arrays.asList("ALIAS:"+user.getAlias(),"ID:"+user.getAndroidId(),"IP:"+user.getIp(),"PORT:"+String.valueOf(user.getPort())))).get();
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
