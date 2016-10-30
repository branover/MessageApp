package overall.brandon.messageapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Brandon on 10/29/2016.
 */

public class Server extends AsyncTask<Object, Void, Object> {

    private Object response;
    private String port;
    ServerSocket serverSocket;

    @Override
    protected Object doInBackground(Object... params) {
        response = "";
        port = (String) params[0];
        try {
            this.serverSocket = new ServerSocket(Integer.parseInt(port));

            while (true) {
                Socket socket = serverSocket.accept();
                Log.e("IP",socket.getInetAddress().toString());
                socket.close();
                serverSocket.close();
                break;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }
}
