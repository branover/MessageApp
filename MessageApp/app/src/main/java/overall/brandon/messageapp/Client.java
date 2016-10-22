package overall.brandon.messageapp;

import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Created by Brandon on 10/21/2016.
 */

public class Client extends AsyncTask<String, Void, String> {

    private static final String SERVER_IP = "192.168.0.3";
    private static final Integer SERVER_PORT = 6666;
    private String response = "";

    @Override
    protected String doInBackground(String... params) {

        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP,SERVER_PORT);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream =  new DataInputStream(socket.getInputStream());

            dataOutputStream.write("REGISTER".getBytes());

            InputStreamReader inputStreamReader;
            BufferedReader reader = null;

            for(String string : params) {
                //System.out.println(string);
                dataOutputStream.write(string.getBytes());
                dataOutputStream.flush();

                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    reader = new BufferedReader(inputStreamReader);
                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        output.append(line);
//                        System.out.println(line);
//                    }
                    output.append(reader.readLine());
                }
                response += output.toString() + "\n";
            }
            response += reader.readLine() + "\n";


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
        return response;

    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
