package com.rcpl.digilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Tab1genre  extends Fragment{
    CardView c1,c2,c3,c4,c5,c6;
    FirebaseDatabase FDB;
    DatabaseReference DBR;
    String gen,userType,userID,number;
        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
               View rootView = inflater.inflate(R.layout.activity_genre_wise, container, false);
            c1= (CardView) rootView.findViewById(R.id.card1);
            c2= (CardView) rootView.findViewById(R.id.card2);
            c3= (CardView) rootView.findViewById(R.id.card3);
            c4= (CardView) rootView.findViewById(R.id.card4);
            c5= (CardView) rootView.findViewById(R.id.card5);
            c6= (CardView) rootView.findViewById(R.id.card6);

            Intent i = getActivity().getIntent();
            Bundle b = i.getExtras();
            userType = b.getString("TYPE");
            userID = b.getString("UID");
            number = b.getString("UID");
            //number=number.substring(3);
            //userID=userID.substring(3);


            FDB = FirebaseDatabase.getInstance();
            c1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gen = "BIOGRAPHY";
                    GetData();

                }
            });
            c2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    gen = "HISTORY";
                    GetData();

                }
            });
            c3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gen = "SCIENCE";
                    GetData();

                }
            });
            c4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gen = "MATH";
                    GetData();

                }
            });
            c5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gen = "LITERATURE";
                    GetData();

                }
            });
            c6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gen = "RELIGION";
                    GetData();

                }
            });
        return rootView;
    }




    void GetData()
    {

        DBR = FDB.getReference("Genre");
        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map = (Map)dataSnapshot.getValue();
                map = new TreeMap<String,String>(map);//to sort values

                Set<String> keys = map.keySet();
                //tv1.setText("");

                Iterator<String> itr = keys.iterator();
                String books = map.get(gen).toString();
                Intent intent = new Intent(getContext(),BookWise.class);
                intent.putExtra("BKS",books);
                intent.putExtra("TYPE",userType);
                intent.putExtra("UID",userID);
                intent.putExtra("PHONE",number);
                startActivity(intent);
                /*while(itr.hasNext())
                {
                    String author = itr.next();
                    String books = map.get(author).toString();//will give the name correspnding to roll
                    Toast.makeText(ViewBooksActivity.this, author+"::  "+books, Toast.LENGTH_SHORT).show();
                    CheckFirebase(books);
                }*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}

