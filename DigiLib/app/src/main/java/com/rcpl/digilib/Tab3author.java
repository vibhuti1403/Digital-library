package com.rcpl.digilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by HP on 15-Jan-18.
 */

public class Tab3author extends Fragment {
    ListView lv;
    FirebaseDatabase FDB;
    DatabaseReference DBR;
    List<String> auth = new ArrayList<String>();
    Map<String,String> map;
    String userType,userID,number;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab3author, container, false);

        Intent i = getActivity().getIntent();
        Bundle b = i.getExtras();
        userType = b.getString("TYPE");
        userID = b.getString("UID");
        number = b.getString("UID");
      //  number=number.substring(3);
        //userID=userID.substring(3);
        lv = (ListView) rootView.findViewById(R.id.listview2);
        auth.clear();
        FDB = FirebaseDatabase.getInstance();
        GetDataFirebase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String author = (String) parent.getItemAtPosition(position);
                String books = map.get(author).toString();
                Intent intent = new Intent(getContext(),BookWise.class);
                intent.putExtra("BKS",books);
                intent.putExtra("TYPE",userType);
                intent.putExtra("UID",userID);
                intent.putExtra("PHONE",number);
                startActivity(intent);
         //       Toast.makeText(getContext(), "NUM"+number, Toast.LENGTH_SHORT).show();

            }
        });


        return rootView;
    }




    void GetDataFirebase()
    {
        DBR = FDB.getReference("Author");
        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                map = (Map)dataSnapshot.getValue();
                //map = new TreeMap<String,String>(map);//to sort values

                Set<String> keys = map.keySet();
                auth.addAll(keys);

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (getContext(),R.layout.author_row,auth);
                // DataBind ListView with items from ArrayAdapter
                lv.setAdapter(arrayAdapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
