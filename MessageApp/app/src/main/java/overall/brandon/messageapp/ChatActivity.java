package overall.brandon.messageapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Chat Window");
            setSupportActionBar(toolbar);
        }

        Intent i = getIntent();
        final Peer peer = (Peer) i.getSerializableExtra("peer");

        TextView peerAliasText = (TextView) findViewById(R.id.peerAliasText);
        peerAliasText.setText(peer.getAlias());

        Button connectButton = (Button) findViewById(R.id.connectButton);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Requests.connectToPeer(peer);
            }
        });

    }
}
