package overall.brandon.messageapp;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import static android.content.Context.WIFI_SERVICE;


public class RegisterFragment extends Fragment {
    User user;

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
            TextView ipv6TextView = (TextView) v.findViewById(R.id.ipv6TextView);
            ipv6TextView.setMovementMethod(new ScrollingMovementMethod());
            ipv6TextView.setText(ipv6Address);
        }

        //Get desired port from edittext on screen
        final EditText portEditText = (EditText) v.findViewById(R.id.portEditText);

        //Status TextView
        final TextView statusTextView = (TextView) v.findViewById(R.id.statusText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alias = aliasText.getText().toString();
                user = new User(androidId,alias,ipv4Address,Integer.parseInt(portEditText.getText().toString()),ipv6Address);
                statusTextView.setText(Requests.registerUser(user));
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alias = aliasText.getText().toString();
                user = new User(androidId,alias,ipv4Address,Integer.parseInt(portEditText.getText().toString()),ipv6Address);
                statusTextView.setText(Requests.updateUser(user));
            }
        });
        return v;
    }
}
