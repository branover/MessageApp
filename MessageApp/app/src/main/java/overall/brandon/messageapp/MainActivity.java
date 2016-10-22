package overall.brandon.messageapp;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String androidId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        user.setAndroidId(androidId);

        TextView androidIDText = (TextView) findViewById(R.id.androidIDText);
        final EditText aliasText = (EditText) findViewById(R.id.aliasText);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        androidIDText.setText(androidId);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alias = aliasText.getText().toString();
                user.setAlias(alias);
                if (Requests.registerUser(user)) {

                }

            }
        });

    }
}
