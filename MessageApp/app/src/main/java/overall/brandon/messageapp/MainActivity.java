package overall.brandon.messageapp;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        TextView androidIDText = (TextView) findViewById(R.id.androidIDText);
        final EditText aliasText = (EditText) findViewById(R.id.aliasText);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        androidIDText.setText(androidId);

        //Get Android device's IP adress
        TextView ipTextView = (TextView) findViewById(R.id.ipAddressText);
        final String ipAddress = getLocalIpAddress();
        ipTextView.setText(ipAddress);

        //Get desired port from edittext on screen
        final EditText portEditText = (EditText) findViewById(R.id.portEditText);

        //Status TextView
        final TextView statusTextView = (TextView) findViewById(R.id.statusText);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alias = aliasText.getText().toString();
                user = new User(androidId,alias,ipAddress,Integer.parseInt(portEditText.getText().toString()));
                statusTextView.setText(Requests.registerUser(user));
            }
        });

    }

    public String getLocalIpAddress() {
        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        if(wifiMgr.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String wifiIpAddress = String.format("%d.%d.%d.%d",
                    (ip & 0xff),
                    (ip >> 8 & 0xff),
                    (ip >> 16 & 0xff),
                    (ip >> 24 & 0xff));

            return wifiIpAddress;
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

}
