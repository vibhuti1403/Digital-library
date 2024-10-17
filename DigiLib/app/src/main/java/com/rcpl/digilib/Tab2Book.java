package com.rcpl.digilib;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HP on 15-Jan-18.
 */

public class Tab2Book  extends Fragment {
    RecyclerView rv2;
    //ListView rv2;
    DownloadManager downloadManager;
    MyAdapter adapter;
    List<MyDataSetGet> listData;
    FirebaseDatabase FDB;
    DatabaseReference DBR,UserRef;
    int i = 0;
    String b[] = new String[10];
    private FirebaseAuth mAuth;
    String number,userType,userID;
    String visibility,favorites="",f[];
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_view_books, container, false);

        Intent i = getActivity().getIntent();
        Bundle b = i.getExtras();
        userType = b.getString("TYPE");
        userID = b.getString("UID");
        number = b.getString("UID");

        //number=number.substring(3);
        //userID=userID.substring(3);
        DBR =FirebaseDatabase.getInstance().getReference("USERS/"+userID);
        DBR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favorites=dataSnapshot.child("FAVOURITES").getValue(String.class);
            //   Toast.makeText(getContext(), "FAVO:"+favorites, Toast.LENGTH_SHORT).show();
               // if(favorites.contains(","))
                 //  f=favorites.split(",");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    //    Toast.makeText(getContext(), "TYPE:::::"+userType, Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();

 //       Toast.makeText(getContext(), number, Toast.LENGTH_SHORT).show();

        rv2 = (RecyclerView) rootView.findViewById(R.id.recylerView);
        rv2.setHasFixedSize(true);

  //      Toast.makeText(getContext(), "In ViewBook", Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getContext().getApplicationContext());
        rv2.setLayoutManager(LM);
        rv2.setItemAnimator(new DefaultItemAnimator());
        rv2.addItemDecoration(new DividerItemDecoration(getContext().getApplicationContext(), LinearLayoutManager.VERTICAL));
        listData = new ArrayList<>();
        adapter = new MyAdapter(listData);
        FDB = FirebaseDatabase.getInstance();
        //adapter.notifyDataSetChanged();
        GetDataFirebase();

        return rootView;
    }


    void GetDataFirebase()
    {
        DBR = FDB.getReference("Books");

        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                try{
                    MyDataSetGet data = dataSnapshot.getValue(MyDataSetGet.class);
                visibility=data.getVISIBILITY();
                if(visibility.equals("TRUE"))
                listData.add(data);}
                catch (Exception e){

                }

                rv2.setAdapter(adapter);


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

            holder.book.setText(data.getNAME());
            holder.author.setText(data.getAUTHOR());
            holder.url = data.getURL();
            holder.imageUrl=data.getIMAGES();
            holder.bid=data.getBOOKID();
            //String id=holder.bid+",";
            //holder.bid=holder.bid.substring(4);
            //Toast.makeText(getContext(), "ID:"+id, Toast.LENGTH_SHORT).show();
            if(userType.equals("USER"))
            {
                if(favorites.contains(holder.bid))
                    holder.fav.setImageResource(R.drawable.red_heart);
            }
       //     Toast.makeText(getContext(), "BID:"+holder.bid, Toast.LENGTH_SHORT).show();

            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(userType.equals("GUEST"))
                        Toast.makeText(getContext(), "Please Register", Toast.LENGTH_SHORT).show();
                    else {
                        holder.fav.setImageResource(R.drawable.red_heart);
                        UserRef = FDB.getReference("USERS/" + number + "/FAVOURITES");
                        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                addFav(dataSnapshot.getValue(String.class), data.getBOOKID());

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            });


        }

        @Override
        public int getItemCount() {
            return listArray.size();
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new MyViewHolder(view);
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView book, author;
            ImageView iv,fav;
            String url,bid;
            String imageUrl;
            public MyViewHolder(View itemView) {
                super(itemView);
                book = (TextView) itemView.findViewById(R.id.bookName);
                author = (TextView) itemView.findViewById(R.id.bookAuthor);
                iv = (ImageView) itemView.findViewById(R.id.imageView3);
                fav = (ImageView) itemView.findViewById(R.id.Favourites);

                if(userType.equals("GUEST"))
                {
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getContext(), "Please Register", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else {

                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(url);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, book.getText().toString());
                            //request.setVisibleInDownloadsUi(true);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            Long reference = downloadManager.enqueue(request);
                            //downloadfile(uri,v);
                        }
                    });
                }


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(getContext(), viewPdfImg.class);
                        i.putExtra("NUM",number);
                        i.putExtra("IMAGE URL", imageUrl);
                        i.putExtra("URL VALUE",url);
                        startActivity(i);

                    }
                });

            }
        }


    }

    public void getfav()
    {
        DBR =FirebaseDatabase.getInstance().getReference("USERS/"+userID);
        DBR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favorites=dataSnapshot.child("FAVOURITES").getValue(String.class);
                //   Toast.makeText(getContext(), "FAVO:"+favorites, Toast.LENGTH_SHORT).show();
                // if(favorites.contains(","))
                //  f=favorites.split(",");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addFav(String val,String bk)
    {
            String book="";
        if(val.length()>0&&val.contains(bk))
            Toast.makeText(getContext(), "Already added to fav", Toast.LENGTH_SHORT).show();
         else {

                 book = val + bk;

            if (book.contains("null"))
                book = book.substring(5);
            if(book.contains("XXX"))
                book=book.substring(3);
            UserRef = FDB.getReference("USERS/" + number);
            book=book+",";
            UserRef.child("FAVOURITES").setValue(book);
            Toast.makeText(getContext(), "Books Added to Favourites", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            listData.clear();
            getfav();
            //adapter = new MyAdapter(listData);
            //adapter.notifyDataSetChanged();
            GetDataFirebase();

        }
    }
}


