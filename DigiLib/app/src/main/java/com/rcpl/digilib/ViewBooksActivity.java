package com.rcpl.digilib;

import android.os.Bundle;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ViewBooksActivity extends AppCompatActivity {
    RecyclerView rv2;
    //ListView rv2;
    MyAdapter adapter;
    List<MyDataSetGet> listData;
    FirebaseDatabase FDB;
    DatabaseReference DBR;
    int i=0;
    String b[]= new String[10];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);
        rv2 = (RecyclerView) findViewById(R.id.recylerView);
        rv2.setHasFixedSize(true);

  //      Toast.makeText(this, "In ViewBook", Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        rv2.setLayoutManager(LM);
        rv2.setItemAnimator(new DefaultItemAnimator());
        rv2.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        //adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,listData);
        //rv2.setAdapter(adapter);
        listData = new ArrayList<>();
        adapter = new MyAdapter(listData);
        FDB = FirebaseDatabase.getInstance();
        GetDataFirebase();
    }



    void GetDataFirebase()
    {
        /*DBR = FDB.getReference("Author");

       //DBR.orderByChild("NAME").equals(b);
        Toast.makeText(this, "in author", Toast.LENGTH_SHORT).show();
        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(DBR.orderByKey().equals("Author1"))
                    DBR = FDB.getReference("Author/Author1");
                //b[i]=dataSnapshot.getKey();
                //i++;
                Toast.makeText(ViewBooksActivity.this, "KEY: "+dataSnapshot.getValue(), Toast.LENGTH_SHORT).show();

               // MyDataSetGet data = dataSnapshot.getValue(MyDataSetGet.class);
                //data =
                //if(data.NAME.equals(b))

                //listData.add(data);
                //rv2.setAdapter(adapter);

                //listData.add(dataSnapshot.getValue(String.class));

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                finish();
                startActivity(getIntent());

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

        //CheckFirebase();*/
        DBR = FDB.getReference("Author");
        DBR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> map = (Map)dataSnapshot.getValue();
                map = new TreeMap<String,String>(map);//to sort values

                Set<String> keys = map.keySet();
                //tv1.setText("");

                Iterator<String> itr = keys.iterator();
                while(itr.hasNext())
                {
                    String author = itr.next();
                    String books = map.get(author).toString();//will give the name correspnding to roll
         //           Toast.makeText(ViewBooksActivity.this, author+"::  "+books, Toast.LENGTH_SHORT).show();
                    CheckFirebase(books);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    void CheckFirebase(String bks)
    {
     //   Toast.makeText(this, "in book", Toast.LENGTH_SHORT).show();
        DBR = FDB.getReference("Books");
        //DBR.orderByChild("NAME").equals(b);
        b = bks.split(",");
        i=0;
        DBR.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //if(data.NAME.equals(b))

        //        Toast.makeText(ViewBooksActivity.this, "***"+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                    if(dataSnapshot.getKey().equals(b[i]) && i<b.length)
                {
                    MyDataSetGet data = dataSnapshot.getValue(MyDataSetGet.class);
         //           Toast.makeText(ViewBooksActivity.this, "value aded"+i, Toast.LENGTH_SHORT).show();
                    listData.add(data);
                    i++;

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



        public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {
        List<MyDataSetGet> listArray;

        public MyAdapter(List<MyDataSetGet> list)
        {
            this.listArray = list;
        }


        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position, List<Object> payloads) {
            super.onBindViewHolder(holder, position, payloads);

        }

        @Override
        public int getItemCount() {
            return listArray.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final MyDataSetGet data = listArray.get(position);
            holder.book.setText(data.getNAME());
            holder.author.setText(data.getAUTHOR());
            holder.url = data.getURL();
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
        //            Toast.makeText(ViewBooksActivity.this,"URL"+data.getURL(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        public class MyViewHolder extends RecyclerView.ViewHolder
        {   TextView book,author;
            ImageView iv;
            String url;
            public MyViewHolder(View itemView)
            {
                super(itemView);
                book = (TextView) itemView.findViewById(R.id.bookAuthor);
                author = (TextView) itemView.findViewById(R.id.bookName);
                iv = (ImageView)itemView.findViewById(R.id.imageView3);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Intent i = new Intent(ViewBooksActivity.this,DisplayActivity.class);
                        //i.putExtra("URL VALUE",url);
                        //startActivity(i);

                    }
                });

            }
        }


    }
}

