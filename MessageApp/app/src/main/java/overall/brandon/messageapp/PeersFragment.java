package overall.brandon.messageapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class PeersFragment extends Fragment {
    ArrayList<Peer> peerList = new ArrayList<Peer>();
    public PeersFragment() {
        // Required empty public constructor
    }

     @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_peers, container, false);
        Button updatePeerButton = (Button) v.findViewById(R.id.updatePeerButton);



        final ListView peerListView = (ListView) v.findViewById(R.id.peerListView);
        final PeerListAdapter peerListAdapter = new PeerListAdapter(peerList,this.getActivity());
        peerListView.setAdapter(peerListAdapter);

        updatePeerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<String, ArrayList<Peer>> result = Requests.updatePeerList();
                if (result.first.equals("PEER UPDATE SUCCESS")) {
                    peerList.clear();
                    peerList.addAll(result.second);
                    peerListAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(v.getContext(),"Peer Update Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

}
