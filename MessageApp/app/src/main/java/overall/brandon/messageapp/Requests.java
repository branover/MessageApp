package overall.brandon.messageapp;

import android.os.AsyncTask;
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
            test = (String) client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Register", user).get();
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
            test = (String) client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Update",user).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test;
        }
    }

    public static Pair<String,ArrayList<Peer>> updatePeerList() {
        Client client = new Client();
        Pair<String,ArrayList<Peer>> test = new Pair<>("",new ArrayList<Peer>());
        try {
            test = (Pair<String,ArrayList<Peer>>) client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"PeerUpdate").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test;
        }
    }

    public static String keepalive(User user) {
        Client client = new Client();
        String test = "";

        try {
            test = (String) client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"Ping",user).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        finally {
            return test;
        }
    }

    public static String connectToPeer(Peer peer) {
        Client client = new Client();
        String test = "";

        try {
            test = (String) client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"ConnectToPeer",peer).get();
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
