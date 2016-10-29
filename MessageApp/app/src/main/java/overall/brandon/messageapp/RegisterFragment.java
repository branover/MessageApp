package overall.brandon.messageapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.WIFI_SERVICE;


public class RegisterFragment extends Fragment {
    User user;
    AlarmManager alarmManager;

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
    }

    public String getLocalIpAddress() {
        WifiManager wifiMgr = (WifiManager) getActivity().getSystemService(WIFI_SERVICE);
        if(wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            return String.format("%d.%d.%d.%d",
                    (ip & 0xff),
                    (ip >> 8 & 0xff),
                    (ip >> 16 & 0xff),
                    (ip >> 24 & 0xff));
        }

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    Log.i("","111 inetAddress.getHostAddress(): "+inetAddress.getHostAddress());
                    //the condition after && is missing in your snippet, checking instance of inetAddress
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        Log.i("","111 return inetAddress.getHostAddress(): "+inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();
                    }

                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return null;
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
            Client.SERVER_IP = SERVER_IP6;
            TextView ipv6TextView = (TextView) v.findViewById(R.id.ipv6TextView);
            ipv6TextView.setMovementMethod(new ScrollingMovementMethod());
            ipv6TextView.setText(ipv6Address);
        }

        //Get desired port from edittext on screen
        final EditText portEditText = (EditText) v.findViewById(R.id.portEditText);

        //Status TextView
        final TextView statusTextView = (TextView) v.findViewById(R.id.statusText);

        SharedPrefManager prefManager = new SharedPrefManager(getContext());
        user = prefManager.retrieveUser();
        if (user == null) {
            user = new User(androidId, aliasText.getText().toString(), ipv4Address, Integer.parseInt(portEditText.getText().toString()), ipv6Address);
            user.setAlias(aliasText.getText().toString());
            user.setPort(Integer.parseInt(portEditText.getText().toString()));
        }
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

        updateAlarm(user);

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
        prefManager.saveObject("user",user);
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPrefManager prefManager = new SharedPrefManager(getContext());
        user = prefManager.retrieveUser();
        user.getAlias();
    }
}
