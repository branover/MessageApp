package overall.brandon.messageapp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
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
                DataInputStream inputStream =  new DataInputStream(socket.getInputStream());

                InputStreamReader inputStreamReader;
                BufferedReader reader = null;

                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    reader = new BufferedReader(inputStreamReader);

                    output.append(reader.readLine());
                }
                Log.e("IP",socket.getInetAddress().toString());
                Log.e("Output",output.toString());
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
