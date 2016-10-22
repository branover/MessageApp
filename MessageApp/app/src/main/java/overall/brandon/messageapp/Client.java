package overall.brandon.messageapp;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Brandon on 10/21/2016.
 */

public class Client extends AsyncTask<String, Void, Void> {

    private static final String SERVER_IP = "192.168.0.3";
    private static final Integer SERVER_PORT = 6666;
    private String response = "";

    @Override
    protected Void doInBackground(String... params) {

        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP,SERVER_PORT);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.write("TEST".getBytes());
            dataOutputStream.flush();
            int bytesRead;
            InputStream inputStream =  new DataInputStream(socket.getInputStream());
            byte buffer[] = null;

         /*
          * notice: inputStream.read() will block if no data return
          */
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                //dataOutputStream.write(buffer, 0, bytesRead);
                response += buffer.toString();
            }

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;

    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
