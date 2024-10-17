package com.rcpl.digilib;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewFav extends AppCompatActivity {

    RecyclerView rvfav;
    //ListView rv2;
    DownloadManager downloadManager;
    MyAdapter adapter;
    List<MyDataSetGet> listData;
    FirebaseDatabase FDB;
    DatabaseReference DBR,UserRef;
    int i = 0;
    String b[];
    String bks,userType,userID,number,favorites="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fav);
        rvfav = (RecyclerView) findViewById(R.id.recylerView3);
        rvfav.setHasFixedSize(true);

        //  Toast.makeText(this, "In ViewBook", Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        rvfav.setLayoutManager(LM);
        rvfav.setItemAnimator(new DefaultItemAnimator());
        rvfav.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        listData = new ArrayList<>();
        adapter = new MyAdapter(listData);
        FDB = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        //bks = b.getString("BKS");
        //userType = b.getString("TYPE");
        userID = b.getString("UID");
        number = b.getString("UID");

        DBR =FirebaseDatabase.getInstance().getReference("USERS/"+userID);

        getFav();
        CheckFirebase();
    }

    void getFav()
    {
        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favorites=dataSnapshot.child("FAVOURITES").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void CheckFirebase()
    {
        //      Toast.makeText(this, "in book", Toast.LENGTH_SHORT).show();
        DBR = FDB.getReference("Books");
        //if(!bks.contains(","))
           // bks = bks+",";


       // b = bks.split(",");
//        Toast.makeText(this, "B:"+b[0]+b[1]+b[2]+b.length, Toast.LENGTH_LONG).show();
        i=0;

        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String bkid=dataSnapshot.getKey()+",";
                if(favorites.contains(bkid))
                {
                    try{
                        MyDataSetGet data = dataSnapshot.getValue(MyDataSetGet.class);
                        //Toast.makeText(BookWise.this, "value aded"+i, Toast.LENGTH_SHORT).show();
                        listData.add(data);
                    }
                    catch (Exception e){}

                    //i++;

                }

                rvfav.setAdapter(adapter);
                //rv2.setAdapter(adapter);
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<MyDataSetGet> listArray;

        public MyAdapter(List<MyDataSetGet> list) {
            this.listArray = list;
        }


        public MyAdapter() {
            super();
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, int position) {

            final MyDataSetGet data = listArray.get(position);
            holder.bk.setText(data.getNAME());
            holder.auth.setText(data.getAUTHOR());
            holder.url = data.getURL();
            holder.bid=data.getBOOKID();
        }

        @Override
        public int getItemCount() {
            return listArray.size();
        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fav, parent, false);
            return new MyAdapter.MyViewHolder(view);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView bk,auth;
            ImageView del,down;
            String url,bid;
            int i=0,j=0;
            public MyViewHolder(View itemView) {
                super(itemView);
                bk= (TextView) itemView.findViewById(R.id.bookTV);
                auth= (TextView) itemView.findViewById(R.id.authTV);
                del= (ImageView) itemView.findViewById(R.id.delete);
                down= (ImageView) itemView.findViewById(R.id.IVdown);

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        down.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                                Uri uri =Uri.parse(url);
                                DownloadManager.Request request = new DownloadManager.Request(uri);
                                request.setDestinationInExternalFilesDir(NewFav.this, Environment.DIRECTORY_DOWNLOADS,bk.getText().toString());
                                //request.setVisibleInDownloadsUi(true);
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                Long reference = downloadManager.enqueue(request);
                                //downloadfile(uri,v);
                            }
                        });
                    }
                });

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String b[];
                        b=favorites.split(",");
                        for(i=0;i<b.length;i++)
                        {
                            if(b[i].equals(bid))
                            {   j=i;
                                i=i+1;
                                delFav(j,i,b);
                                finish();
                                startActivity(getIntent());
                                break;

                            }
                        }
                    }
                });



            }

            //row





        }


    }

    public void delFav(int j,int i,String[] bk)
    {
        int k=0;
        String myFav;
        for(k=i;k<bk.length;k++)
        {
            bk[j]=bk[k];
            j++;
        }
        myFav=bk[0];
        for(k=1;k<bk.length;k++)
        {
            myFav=bk[k]+",";
        }
        //setvalue
        DBR =FirebaseDatabase.getInstance().getReference("USERS/"+userID);
        DBR.child("FAVOURITES").setValue(myFav);

    }

}


