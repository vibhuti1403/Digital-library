package com.rcpl.digilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RequestActivity extends AppCompatActivity {

    RecyclerView myRecycleview;
    DatabaseReference myRef;
    FirebaseDatabase database;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);


        myRecycleview=(RecyclerView)findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RequestActivity.this,AddReqActivity.class);
                startActivity(i);
            }
        });
        database= FirebaseDatabase.getInstance();
        myRef=database.getReference("Request");
        myRecycleview.setHasFixedSize(true);
        RecyclerView.LayoutManager LM = new LinearLayoutManager(this);
        myRecycleview.setLayoutManager(LM);
        abcd();
    }
    public void abcd()
    {
     //   Toast.makeText(this, "in abcfunc", Toast.LENGTH_SHORT).show();
        FirebaseRecyclerAdapter<MyData,MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyData,MyViewHolder>(

                MyData.class,
                R.layout.card,
                MyViewHolder.class,
                myRef

        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, MyData model, int position) {

                viewHolder.book.setText(model.getBk());
                viewHolder.author.setText(model.getAuth());
                viewHolder.publication.setText(model.getPub());
           //     Toast.makeText(RequestActivity.this, "populate**", Toast.LENGTH_SHORT).show();

            }
        };

        myRecycleview.setAdapter(firebaseRecyclerAdapter);
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView book,author,publication;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            book= (TextView)itemView.findViewById(R.id.TVBookHead);
            author= (TextView)itemView.findViewById(R.id.TVAuthorDesc);
            publication=(TextView)itemView.findViewById(R.id.TVPubDesc);
        }
    }
}
