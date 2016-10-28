package overall.brandon.messageapp;

import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.util.Log;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Brandon on 10/21/2016.
 */

public class Client extends AsyncTask<Object, Void, Object> {

    public static String SERVER_IP = "192.168.0.3";
    //public static String SERVER_IP = "10.1.10.252";
    private static final Integer SERVER_PORT = 6666;
    private Object response;


    @Override
    protected Object doInBackground(Object... params) {
        response = "";
        String command = (String) params[0];
        User user;
        ArrayList<String> userParams;
        switch (command) {
            case "Register":
                user = (User) params[1];
                userParams = new ArrayList<>();
                userParams.add("ALIAS:"+user.getAlias());
                userParams.add("ID:"+user.getAndroidId());
                userParams.add("IP:"+user.getIp());
                userParams.add("IPV6:"+user.getIPv6());
                userParams.add("PORT:"+String.valueOf(user.getPort()));
                response = handleRegisterConnection(userParams);
                break;
            case "Update":
                user = (User) params[1];
                userParams = new ArrayList<>();
                userParams.add("ALIAS:"+user.getAlias());
                userParams.add("ID:"+user.getAndroidId());
                userParams.add("IP:"+user.getIp());
                userParams.add("IPV6:"+user.getIPv6());
                userParams.add("PORT:"+String.valueOf(user.getPort()));
                response = handleUpdateCommand(userParams);
                break;
            case "PeerUpdate":
                response = handleUpdatePeerCommand();
                break;
            case "Ping":
                response = handleKeepaliveCommand((User) params[1]);
                break;
            default:
                response = "INVALID COMMAND";
        }

        return response;

    }

    private String handleKeepaliveCommand(User user) {
        Log.e("Server Info", SERVER_IP + "   " + SERVER_PORT);
        Log.e("Test","test");


        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP,SERVER_PORT);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            dataOutputStream.write("PING".getBytes());
            dataOutputStream.write(("ALIAS:"+user.getAlias()).getBytes());
            dataOutputStream.write(("ID:"+user.getAndroidId()).getBytes());

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            //response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "TRUE";
    }

    private Pair<String,ArrayList<Peer>> handleUpdatePeerCommand() {
        Socket socket = null;

        try {
            ArrayList<Peer> peerList = new ArrayList<Peer>();
            socket = new Socket(SERVER_IP,SERVER_PORT);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream =  new DataInputStream(socket.getInputStream());

            dataOutputStream.write("UPDATEPEER".getBytes());
            dataOutputStream.flush();

            InputStreamReader inputStreamReader;
            BufferedReader reader = null;

            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                reader = new BufferedReader(inputStreamReader);
                Integer peerListSize = (Integer.parseInt(reader.readLine()));

                output.append(reader.readLine());

                Log.e("Output",output.toString());
                Gson gson = new Gson();
                JSONObject json = null;
                try {
                    json = new JSONObject(output.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    json = new JSONObject();
                }
                if (json.length() == peerListSize) {
                    for (Iterator<String> iter = json.keys(); iter.hasNext();) {
                        try {
                            String strEntry = json.get(iter.next()).toString();
                            JsonElement entry = new JsonParser().parse(strEntry);
                            Peer peer = gson.fromJson(entry, Peer.class);
                            peerList.add(peer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    return new Pair<>("PEER UPDATE SUCCESS", peerList);
                }
                else {
                    return new Pair<>("SIZE MISMATCH",new ArrayList<Peer>());
                }
            }


//            response += reader.readLine() + "\n";

        } catch (UnknownHostException e) {
            e.printStackTrace();
            //response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            //response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        response = new Pair<String, ArrayList<Peer>>("TESTRESPONSE",new ArrayList<Peer>());
        return (Pair<String,ArrayList<Peer>>) response;
    }

    private String handleUpdateCommand(ArrayList<String> params) {
        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP,SERVER_PORT);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream =  new DataInputStream(socket.getInputStream());

            dataOutputStream.write("UPDATE".getBytes());

            InputStreamReader inputStreamReader;
            BufferedReader reader = null;

            for(String string : params) {
                dataOutputStream.write(string.getBytes());
                dataOutputStream.flush();

                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    reader = new BufferedReader(inputStreamReader);
                    output.append(reader.readLine());
                }
                response += output.toString() + "\n";
            }
            response += reader.readLine() + "\n";


        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return (String) response;
    }

    private String handleRegisterConnection(ArrayList<String> params) {
        Socket socket = null;

        try {
            socket = new Socket(SERVER_IP,SERVER_PORT);

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream inputStream =  new DataInputStream(socket.getInputStream());

            dataOutputStream.write("REGISTER".getBytes());

            InputStreamReader inputStreamReader;
            BufferedReader reader = null;

            for(String string : params) {
                dataOutputStream.write(string.getBytes());
                dataOutputStream.flush();

                StringBuilder output = new StringBuilder();
                if (inputStream != null) {
                    inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                    reader = new BufferedReader(inputStreamReader);
                    output.append(reader.readLine());
                }
                response += output.toString() + "\n";
            }
            response += reader.readLine() + "\n";


        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return (String) response;
    }

}
