package com.rcpl.digilib;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

public class TestActivity extends AppCompatActivity {
    static DownloadManager downloadManager;
    FirebaseDatabase fdb;
    DatabaseReference myBooks, myAuthors,DBR;
    EditText search;
    RecyclerView myrv;
    //BookWise.MyAdapter adapter;
    List<MyDataSetGet> listData;
    Context ctx;
    String favorites,userID,userType;
    int t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent=getIntent();
        t=intent.getIntExtra("TAB",0);


        search = (EditText) findViewById(R.id.ETsearch);
        myrv = (RecyclerView) findViewById(R.id.searchRecycler);
        fdb = FirebaseDatabase.getInstance();
        myBooks = fdb.getReference("Books");
         myrv.setHasFixedSize(true);
        ctx = getApplicationContext();
        //Toast.makeText(getContext(), "In ViewBook", Toast.LENGTH_SHORT).show();
        RecyclerView.LayoutManager LM = new LinearLayoutManager(getApplicationContext());
        myrv.setLayoutManager(LM);
        //myrv.setItemAnimator(new DefaultItemAnimator());
        //myrv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        //listData = new ArrayList<>();
        //adapter = new MyAdapter(listData);
        if(t==1)
            search.setHint("Search by Book Name");
        else
            if(t==2)
                search.setHint("Search by Author");


    }



    public void searchFirebase(View v)
    {
        String searchText = (search.getText().toString());
        mysearch(searchText);
    }
public void mysearch(String searchText)
{
    Query q=myBooks.orderByChild("NAME").startAt(searchText).endAt(searchText + "\uf8ff");
        if(t==1)
        {
             q = myBooks.orderByChild("NAME").startAt(searchText).endAt(searchText + "\uf8ff");
        }
        else if(t==2)
        {
            searchText=searchText.toUpperCase();
             q = myBooks.orderByChild("AUTHOR").startAt(searchText).endAt(searchText + "\uf8ff");

        }

    //Query q = myBooks.orderByKey().startAt(searchText).endAt(searchText + "\uf8ff");

    if(q.toString().isEmpty())
        Toast.makeText(ctx, "No Results Found", Toast.LENGTH_SHORT).show();
    else {
        FirebaseRecyclerAdapter<MyDataSetGet, MyViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyDataSetGet, MyViewHolder>(

                MyDataSetGet.class,
                R.layout.row,
                MyViewHolder.class,
                q


        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, MyDataSetGet model, int position) {

                viewHolder.author.setText(model.getNAME());
                viewHolder.book.setText(model.getAUTHOR());
                viewHolder.url = model.getURL();
                viewHolder.imageUrl = model.getIMAGES();


            }
        };
        myrv.setAdapter(firebaseRecyclerAdapter);
    }



}




        public static class MyViewHolder extends RecyclerView.ViewHolder {
            TextView book, author;
            ImageView iv,fav;
            String url,bid;
            String imageUrl;
            public MyViewHolder(final View itemView) {
                super(itemView);
                book = (TextView) itemView.findViewById(R.id.bookAuthor);
                author = (TextView) itemView.findViewById(R.id.bookName);
                iv = (ImageView) itemView.findViewById(R.id.imageView3);
                fav = (ImageView) itemView.findViewById(R.id.Favourites);




                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadManager = (DownloadManager)itemView.getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri =Uri.parse(url);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setDestinationInExternalFilesDir(itemView.getContext(), Environment.DIRECTORY_DOWNLOADS,book.getText().toString());
                        //request.setVisibleInDownloadsUi(true);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        Long reference = downloadManager.enqueue(request);
                        //downloadfile(uri,v);
                    }
                });


                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(itemView.getContext(), viewPdfImg.class);
                        i.putExtra("IMAGE URL", imageUrl);
                        i.putExtra("URL VALUE",url);
                        itemView.getContext().startActivity(i);
                     //   TestActivity.this.startActivity(i);


                    }


                });

            }
        }


    }


