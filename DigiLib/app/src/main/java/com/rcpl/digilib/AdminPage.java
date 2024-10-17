package com.rcpl.digilib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AdminPage extends AppCompatActivity {


    Button b;
    MyAdapterAdmin adp;
    static ArrayList<Players> player = new ArrayList<>();
    static int h;
    RecyclerView rv;
    String g1="",g2="",g3="",g4="",g5="",g6="";
    DatabaseReference dr3;
    String books="",dauthor="",val="",b1="";
    String book_author[][];
    int flag=0,i=0,real_count=0;;
    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Books");
    DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference("Author");
    DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference("Genre");
    DatabaseReference dr4=FirebaseDatabase.getInstance().getReference("Author");
    DatabaseReference dr5=FirebaseDatabase.getInstance().getReference("USERS");
String phoneNum,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar4);
        //setSupportActionBar(toolbar);



        adp = new MyAdapterAdmin(this, getPlayers());
        b = (Button) findViewById(R.id.click);
        rv = (RecyclerView) findViewById(R.id.recycler);
        BottomNavigationView bnv = (BottomNavigationView) findViewById(R.id.navigation);

        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        phoneNum=bundle.getString("PHONE");
        uid=bundle.getString("UID");

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });

        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_1:
                        Toast.makeText(AdminPage.this, "clicked", Toast.LENGTH_LONG).show();
                        if (adp.checkedPlayers.size() > 0) {

                            notifyUsers();

                            Toast.makeText(AdminPage.this, adp.checkedPlayers.size()+"", Toast.LENGTH_SHORT).show();
//                                  insert in genre
                            for (final Players p1 : adp.checkedPlayers) {
                                if (p1.getBookGenre().equals("BIOGRAPHY")) {
                                    g1 += p1.getBookId() + ",";
                                } else if (p1.getBookGenre().equals("HISTORY")) {
                                    g2 += p1.getBookId() + ",";
                                }
                                else if (p1.getBookGenre().equals("SCIENCE")) {
                                    g3 += p1.getBookId() + ",";
                                }
                                else if (p1.getBookGenre().equals("MATH")) {
                                    g4 += p1.getBookId() + ",";
                                }
                                else if (p1.getBookGenre().equals("LITERATURE")) {
                                    g5 += p1.getBookId() + ",";
                                }
                                else if (p1.getBookGenre().equals("RELIGION")) {
                                    g6 += p1.getBookId() + ",";
                                }
                            }
                            dr2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(g1.length()>0)

                                        dr2.child("BIOGRAPHY").setValue( dataSnapshot.child("BIOGRAPHY").getValue(String.class)+g1);
                                    if(g2.length()>0)

                                        dr2.child("HISTORY").setValue(dataSnapshot.child("HISTORY").getValue(String.class)+g2);
                                    if(g3.length()>0)

                                        dr2.child("SCIENCE").setValue(dataSnapshot.child("SCIENCE").getValue(String.class)+g3);
                                    if(g4.length()>0)

                                        dr2.child("MATH").setValue(dataSnapshot.child("MATH").getValue(String.class)+g4);
                                    if(g5.length()>0)

                                        dr2.child("LITERATURE").setValue(dataSnapshot.child("LITERATURE").getValue(String.class)+g5);

                                    if(g6.length()>0)

                                        dr2.child("RELIGION").setValue( dataSnapshot.child("RELIGION").getValue(String.class)+g6);


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            for (final Players p : adp.checkedPlayers) {
                                dr.child(p.getBookId()).child("VISIBILITY").setValue("TRUE");
                            }

//                                  insert in author
                            dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String temp1[],temmp="";
                                    for (Players p : adp.checkedPlayers)
                                    {
                                        temp1=p.getBookAuthor().split(",");
                                        Toast.makeText(AdminPage.this, temp1[0]+"", Toast.LENGTH_SHORT).show();
                                        for(String temp:temp1)
                                        {
                                            temmp=temp;
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                                            {
                                                flag = 0;
                                                if (temp.equals(postSnapshot.getKey()))
                                                {
                                                    flag = 1;
                                                    break;
                                                }
                                            }
                                            if (flag == 0) {
                                                dr3 = FirebaseDatabase.getInstance().getReference("Author/" + temmp);
                                                dr3.setValue("yo");
                                            }
                                        }

                                    }
                                    dr4.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final Map<String, String> map = (Map) dataSnapshot.getValue();
                                            Set<String> keys = map.keySet();
                                            Iterator<String> itr = keys.iterator();
                                            String temp="";
                                            String a[];
                                            int c,count=0;
                                            for (Players p2:adp.checkedPlayers)
                                            {
                                                temp=p2.getBookAuthor();
                                                a=temp.split(",");
                                                c=a.length;
                                                count+=c;
                                            }
                                            book_author=new String[count][2];
                                            while (itr.hasNext())
                                            {
                                                dauthor=itr.next();
                                                for(Players p2 : adp.checkedPlayers)
                                                {
                                                    if(p2.getBookAuthor().contains(dauthor))
                                                        books+=p2.getBookId()+",";
                                                }
                                                if(!books.equals("")) {
                                                    insert(dauthor,i,0);
                                                    real_count++;
                                                    //book_author[i][0] = dauthor;
                                                    if (!map.get(dauthor).equals("yo"))
                                                    {
                                                        val=map.get((dauthor));
                                                        //Toast.makeText(Admin.this, "val="+val, Toast.LENGTH_SHORT).show();
                                                        //Log.d("Banas","val="+val);
                                                        insert(val+books,i,1);
                                                    }
                                                    // book_author[i][1] = books + map.get(dauthor);
                                                    else
                                                        insert(books,i,1);
                                                    // book_author[i][1] = books;
                                                    i++;
                                                }
                                                books="";
                                                //Toast.makeText(Admin.this, dauthor+":"+books, Toast.LENGTH_SHORT).show();
                                            }
                                            change();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }

                        else
                            Toast.makeText(AdminPage.this, "select item", Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());
                        break;

                    //*********************************************
                    case R.id.item_2:
                        if (adp.checkedPlayers.size() > 0) {
                            for (final Players p : adp.checkedPlayers) {
                                Toast.makeText(AdminPage.this, p.getBookName(), Toast.LENGTH_LONG).show();
                                dr.child(p.getBookId()).removeValue();
                            }
                            //****************************************************
                        } else
                            Toast.makeText(AdminPage.this, "select item", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                        break;
                    case R.id.item_3:
                        Intent in=new Intent(AdminPage.this,Admin_changepswd.class);
                        in.putExtra("uid",uid);
                        //in.putExtra("PHONE",phoneNum);
                        startActivity(in);
                        Toast.makeText(AdminPage.this, "Edit", Toast.LENGTH_LONG).show();
                        break;


                }
                return true;
            }
        });

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setHasFixedSize(true);
        //rv.setAdapter(adp);
    }
    void notifyUsers()
    {
        Toast.makeText(this, "notify", Toast.LENGTH_SHORT).show();
        dr5.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference dr6;
                String uid;
                for(DataSnapshot user:dataSnapshot.getChildren())
                {
                    uid=user.getKey();
                    Toast.makeText(AdminPage.this, uid, Toast.LENGTH_SHORT).show();
                    dr6=FirebaseDatabase.getInstance().getReference("USERS/"+uid);
                    dr6.child("NOTIFICATION").setValue("UNSEEN");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    void insert(String a,int r,int c)
    {
        try{
            book_author[r][c]=a;
            Log.d("Banas",i+book_author[r][c]);}
        catch(Exception e){
            Log.d("bv","in exception");
        }

    }
    void change() {
        try{
            for (int j = 0; j <real_count; j++) {
                Log.d("bv", "j=" + j);
                dr1.child(book_author[j][0]).setValue(book_author[j][1]);
            }

            //Log.d("bv",book_author[i][0]+" "+book_author[i][1]);

        }
        catch(Exception e)
        {
            Log.d("bv","in exception");
        }
    }

    private ArrayList<Players> getPlayers() {


        final DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Books");
        DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference("Books");
        dr1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    String bookId, bookAuthor, bookName,bookGenre;
                    ArrayList<Players> players = new ArrayList<>();
                    int i;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot sp : dataSnapshot.getChildren()) {
                            // Toast.makeText(Admin.this, ""+sp.getKey().toString(), Toast.LENGTH_SHORT).show();
                            bookId = sp.getKey();
                            // Log.d("banasthali","hello World");
                            //Log.d("banasthali",bookId);
                            // Toast.makeText(Admin.this, bookId, Toast.LENGTH_SHORT).show();
                            i = 1;
                            if (sp.child("VISIBILITY").getValue(String.class).equals("FALSE")) {
                                 Toast.makeText(AdminPage.this, bookId, Toast.LENGTH_SHORT).show();
                                bookName = sp.child("NAME").getValue(String.class);
                                bookAuthor = sp.child("AUTHOR").getValue(String.class);
                                bookGenre=sp.child("GENRE").getValue(String.class);
                                Players p = new Players(bookId, bookName, bookAuthor,bookGenre);
                                players.add(p);
                            }
                        }

                        setPlayer(players, i);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //  Toast.makeText(this, "Here", Toast.LENGTH_SHORT).show();
        return player;
    }

    void setPlayer(ArrayList<Players> p, int i) {
        h = i;
        player = p;
        rv.setAdapter(adp);
        // Toast.makeText(this, "h="+h, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Intent i;

        switch (id) {
            case R.id.action_settings:
                signout();
                break;
            case R.id.action_upload:
                i = new Intent(AdminPage.this, UploadActivity.class);
                i.putExtra("TYPE", "ADMIN");
                i.putExtra("UID", uid);
                i.putExtra("PHONE", phoneNum);
                startActivity(i);
                break;
        }

            return super.onOptionsItemSelected(item);

    }

    public void signout()
    {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();

      //  Toast.makeText(this, "PHONENUM:"+phoneNum, Toast.LENGTH_SHORT).show();

        DatabaseReference DBref = fdb.getReference("ADMIN/"+phoneNum);
        DBref.child("SIGNIN").setValue("NO");

        SharedPreferences sp = getSharedPreferences("sharedFile",0);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("ISSIGNED", "NO");
        editor.commit();

        finish();
    }
}
