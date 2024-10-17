package com.rcpl.digilib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavActivity extends AppCompatActivity {

    FavAdapter favAdapter;
    ArrayList<Favourites> fav;
    //Button refresh;
    RecyclerView rv;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);
        //Toast.makeText(this, "Bhaai 1 step pe aa gye", Toast.LENGTH_SHORT).show();
        fav=new ArrayList<Favourites>();
        uid=getIntent().getStringExtra("uid");
        favAdapter=new FavAdapter(this,getFavourites(),uid);
       // refresh=(Button)findViewById(R.id.button7);
        rv=(RecyclerView)findViewById(R.id.favrecycler);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);
        //favAdapter.notifyDataSetChanged();


    }


    public ArrayList<Favourites> getFavourites() {


        DatabaseReference dr= FirebaseDatabase.getInstance().getReference("USERS/"+uid);
        final DatabaseReference dr1= FirebaseDatabase.getInstance().getReference("Books");
        //Toast.makeText(this, "Bhaai getFavourites()", Toast.LENGTH_SHORT).show();
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //        Toast.makeText(FavActivity.this,dataSnapshot.child("FAVOURITES").getValue(String.class) , Toast.LENGTH_SHORT).show();
                String[] dBFav=dataSnapshot.child("FAVOURITES").getValue(String.class).split(",");


                if(dBFav[0].equals("XXX"))
                {
                    rv.setVisibility(View.INVISIBLE);
                }
                else{
                    for(String book1:dBFav)
                    {
                        //    Toast.makeText(FavActivity.this,"Splitted:"+book1, Toast.LENGTH_SHORT).show();
                        final String book=book1;
                        dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String bookName,authorName,bID,bUrl;
                                bookName=(dataSnapshot.child(book).child("NAME").getValue(String.class));
                                authorName=(dataSnapshot.child(book).child("AUTHOR").getValue(String.class));
                                bID=(dataSnapshot.child(book)).child("BOOKID").getValue(String.class);
                                bUrl=(dataSnapshot.child(book).child("URL").getValue(String.class));
                                Favourites favourites=new Favourites(bookName,authorName,bID,bUrl);
                                setFavArrayList(favourites);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return fav;
    }
    public void setFavArrayList(Favourites favourites)
    {
        //     Toast.makeText(this, "Setting:"+favourites.getBookID(), Toast.LENGTH_SHORT).show();
        fav.add(favourites);
        rv.setAdapter(favAdapter);
    }
}
