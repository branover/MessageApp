package overall.brandon.messageapp;

import android.support.v4.util.Pair;
import android.util.Log;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by Brandon on 10/21/2016.
 */

public final class Requests {

    public static String registerUser(User user) {
        Client client = new Client();
        String test = "FAILED";
        try {
            test = (String) client.execute("Register", user).get();
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
            test = (String) client.execute("Update",user).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test;
        }
    }

    public static ArrayList<Peer> updatePeerList() {
        Client client = new Client();
        Pair<String,ArrayList<Peer>> test = new Pair<>("",new ArrayList<Peer>());
        try {
            test = (Pair<String,ArrayList<Peer>>) client.execute("PeerUpdate").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test.second;
        }
    }
}
