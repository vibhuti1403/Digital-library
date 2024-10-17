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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookWise extends AppCompatActivity {
    RecyclerView rv2;
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
        setContentView(R.layout.activity_book_wise);
        rv2 = (RecyclerView) findViewById(R.id.recylerView2);
        rv2.setHasFixedSize(true);

      //  Toast.makeText(this, "In ViewBook", Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        rv2.setLayoutManager(LM);
        rv2.setItemAnimator(new DefaultItemAnimator());
        rv2.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        listData = new ArrayList<>();
        adapter = new MyAdapter(listData);
        FDB = FirebaseDatabase.getInstance();
        Intent i = getIntent();
        Bundle b = i.getExtras();
        bks = b.getString("BKS");
         userType = b.getString("TYPE");
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
        //        Toast.makeText(BookWise.this, "FAVO:"+favorites, Toast.LENGTH_SHORT).show();
                // if(favorites.contains(","))
                //  f=favorites.split(",");

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
        if(!bks.contains(","))
            bks = bks+",";


        b = bks.split(",");
//        Toast.makeText(this, "B:"+b[0]+b[1]+b[2]+b.length, Toast.LENGTH_LONG).show();
        i=0;

        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //if(data.NAME.equals(b))

         //       Toast.makeText(BookWise.this, "***"+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                /*for(DataSnapshot dp:dataSnapshot.getChildren()){
                    Toast.makeText(BookWise.this, "Check:"+dp.getKey(), Toast.LENGTH_SHORT).show();

                if(bks.contains(dp.getKey()))
                {
                    Toast.makeText(BookWise.this, "=="+dp.getKey(), Toast.LENGTH_SHORT).show();
                    MyDataSetGet data = dp.getValue(MyDataSetGet.class);
                   // Toast.makeText(BookWise.this, "bInside"+dp.getKey(), Toast.LENGTH_SHORT).show();
                    listData.add(data);
                }
                   // i++;
                }*/
                String bkid=dataSnapshot.getKey()+",";
                   if(bks.contains(bkid))
                    {
                        try{
                            MyDataSetGet data = dataSnapshot.getValue(MyDataSetGet.class);
                        //Toast.makeText(BookWise.this, "value aded"+i, Toast.LENGTH_SHORT).show();
                        listData.add(data);
                        }
                        catch (Exception e){}

                        //i++;

                    }

                rv2.setAdapter(adapter);
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
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            final MyDataSetGet data = listArray.get(position);
            holder.book.setText(data.getNAME());
            holder.author.setText(data.getAUTHOR());
            holder.url = data.getURL();
            holder.imageUrl=data.getIMAGES();
            holder.bid=data.getBOOKID();
            //holder.bid=holder.bid.substring(4);
 //           Toast.makeText(BookWise.this, "ABID:"+holder.bid, Toast.LENGTH_SHORT).show();
            if(userType.equals("USER")&&favorites.contains(holder.bid))
                holder.fav.setImageResource(R.drawable.red_heart);
            //Toast.makeText(getContext(), "BID:"+holder.bid, Toast.LENGTH_SHORT).show();

            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(userType.equals("GUEST"))
                        Toast.makeText(getApplicationContext(), "Please Register", Toast.LENGTH_SHORT).show();
                    else {

                    holder.fav.setImageResource(R.drawable.red_heart);
                    UserRef = FDB.getReference("USERS/"+number+"/FAVOURITES");
                    UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                                addFav(dataSnapshot.getValue(String.class),data.getBOOKID());

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
            ImageView iv,fav;;
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
                            Toast.makeText(BookWise.this, "Please Register", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else
                {
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri =Uri.parse(url);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setDestinationInExternalFilesDir(BookWise.this, Environment.DIRECTORY_DOWNLOADS,book.getText().toString());
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

                        Intent i = new Intent(BookWise.this, viewPdfImg.class);
                        i.putExtra("IMAGE URL", imageUrl);
                        i.putExtra("URL VALUE",url);
                        startActivity(i);

                    }
                });

            }
        }


    }


    public void addFav(String val,String bk)
    {
        String book="";
        if(val.length()>0&&val.contains(bk))
            Toast.makeText(BookWise.this, "Already added to fav", Toast.LENGTH_SHORT).show();
        else {

            book = val  + bk;

            if (book.contains("null"))
                book = book.substring(5);
            if(book.contains("XXX"))
                book=book.substring(3);
            UserRef = FDB.getReference("USERS/" + number);
            book=book+",";
            UserRef.child("FAVOURITES").setValue(book);
            Toast.makeText(BookWise.this, "Books Added to Favourites", Toast.LENGTH_SHORT).show();
        }

    }

}


