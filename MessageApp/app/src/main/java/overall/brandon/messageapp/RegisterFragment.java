package overall.brandon.messageapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.WIFI_SERVICE;


public class RegisterFragment extends Fragment {
    User notFinalUser;
    AlarmManager alarmManager;
    MainActivity mainActivity;

    //Home
    private static final String SERVER_IP = "192.168.0.3";
    private static final String SERVER_IP6 = "2601:147:c101:90d0:7d7b:1a75:5240:1d1";

    //Work
    //private static final String SERVER_IP = "10.1.10.252";
    //private static final String SERVER_IP6 = "2603:3003:1f01:d800:492f:ec14:fd48:dfaf";

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        final String androidId = ((MainActivity)getActivity()).getAndroidId();

        TextView androidIDText = (TextView) v.findViewById(R.id.androidIDText);
        final EditText aliasText = (EditText) v.findViewById(R.id.aliasText);

        //Buttons
        Button registerButton = (Button) v.findViewById(R.id.registerButton);
        Button updateButton = (Button) v.findViewById(R.id.updateButton);

        androidIDText.setText(androidId);

        //Get Android device's IP address
        TextView ipTextView = (TextView) v.findViewById(R.id.ipAddressText);
        final String ipv4Address = Utils.getIPAddress(true);
        final String ipv6Address = Utils.getIPAddress(false);

        ipTextView.setText(ipv4Address);
        if (!ipv6Address.isEmpty()) {
            //Client.SERVER_IP = SERVER_IP6;
            TextView ipv6TextView = (TextView) v.findViewById(R.id.ipv6TextView);
            ipv6TextView.setMovementMethod(new ScrollingMovementMethod());
            ipv6TextView.setText(ipv6Address);
        }

        //Get desired port from edittext on screen
        final EditText portEditText = (EditText) v.findViewById(R.id.portEditText);

        //Status TextView
        final TextView statusTextView = (TextView) v.findViewById(R.id.statusText);

        SharedPrefManager prefManager = new SharedPrefManager(getContext());
        notFinalUser = prefManager.retrieveUser();
        if (notFinalUser == null) {
            notFinalUser = new User(androidId, aliasText.getText().toString(), ipv4Address, Integer.parseInt(portEditText.getText().toString()), ipv6Address);
            notFinalUser.setAlias(aliasText.getText().toString());
            notFinalUser.setPort(Integer.parseInt(portEditText.getText().toString()));
        }
        final User user = notFinalUser;
        portEditText.setText(String.valueOf(user.getPort()));
        aliasText.setText(user.getAlias());
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTextView.setText(Requests.registerUser(user));
                updateAlarm(user);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusTextView.setText(Requests.updateUser(user));
                updateAlarm(user);
            }
        });

        //Auto update alias and port whenever it is modified
        aliasText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setAlias(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        portEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setPort(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        //Server start and stop buttons
        Button startServerButton = (Button) v.findViewById(R.id.startServiceButton);
        Button stopServerButton = (Button) v.findViewById(R.id.stopServiceButton);

        startServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.serverService.startServer(getContext(),user.getPort());
            }
        });
        stopServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.serverService.stopServer(getContext());
            }
        });

        //updateAlarm(user);
        //Server server = new Server();
        //server.execute(String.valueOf(user.getPort()));



        return v;
    }

    private void updateAlarm(User user) {
        //Set keepalive alarm to remain online
        Intent i = new Intent(getContext(), AlarmReceiver.class);
        Bundle b = new Bundle();
        b.putSerializable("User",user);
        i.putExtra("bundle",b);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,i,PendingIntent.FLAG_CANCEL_CURRENT);
        Requests.keepalive(user);
        alarmManager = (AlarmManager) ((MainActivity)getActivity()).getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 1000 * 30,pendingIntent);
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPrefManager prefManager = new SharedPrefManager(getContext());
        prefManager.saveObject("user",notFinalUser);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPrefManager prefManager = new SharedPrefManager(getContext());
        notFinalUser = prefManager.retrieveUser();
        if (notFinalUser != null) {
            notFinalUser.getAlias();
        }
    }

}
