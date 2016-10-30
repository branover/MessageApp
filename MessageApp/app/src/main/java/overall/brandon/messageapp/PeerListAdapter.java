package overall.brandon.messageapp;

/**
 * Created by Brandon on 10/23/2016.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class PeerListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Peer> list = new ArrayList<>();
    private Context context;

    public PeerListAdapter(ArrayList<Peer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.peer_list_item, null);
        }

        final Peer peer = list.get(position);

        TextView peerAlias = (TextView) view.findViewById(R.id.peerAlias);
        peerAlias.setText(peer.getAlias());

        View onlineStatus = (View) view.findViewById(R.id.onlineStatus);
        Integer onlineTime = peer.getOnlineTime();
        if ( (System.currentTimeMillis() / 1000) - onlineTime <= 120 ) {
            onlineStatus.setBackgroundResource(R.drawable.online_status);
        }
        else {
            onlineStatus.setBackgroundResource(R.drawable.offline_status);
        }


        return view;
    }


}
