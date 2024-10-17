package com.rcpl.digilib;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by srish on 4/1/2018.
 */

public class FavAdapter extends RecyclerView.Adapter<FavHolder> {
    Context c;
    ArrayList<Favourites> favourites;
    String uID;

    public FavAdapter(Context c, ArrayList<Favourites> favourites,String uID) {
        this.c = c;
        this.favourites = favourites;
        this.uID=uID;
      //  Toast.makeText(c, "Constructor", Toast.LENGTH_SHORT).show();
    }


    //Initialize FavHolder
    @Override
    public FavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // Toast.makeText(c, "oncreate", Toast.LENGTH_SHORT).show();
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fav,null);
        FavHolder fh=new FavHolder(v);
        return fh;
    }

    @Override
    public void onBindViewHolder(final FavHolder favHolder, final int position) {
       // Toast.makeText(c, "onBindViewHolder()", Toast.LENGTH_SHORT).show();
        favHolder.bookName.setText(favourites.get(position).getBookName());
        favHolder.authorName.setText(favourites.get(position).getAuthorName());
        favHolder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final DatabaseReference dr= FirebaseDatabase.getInstance().getReference("USERS/"+uID);
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String bID=favourites.get(position).getBookID();
                        Log.d("Favourites_Debugging",uID);
                        String dBFav=dataSnapshot.child("FAVOURITES").getValue(String.class);
                        String newDBFav;int i;
                        // String[] favArray=dBFav.split(",")
                        if(dBFav.indexOf(",")==dBFav.length()-1)
                            newDBFav="XXX";
                        else {
                            i = dBFav.indexOf(bID + ",");
                            if(i==0)
                            {
                                i=dBFav.indexOf(",");
                                newDBFav=dBFav.substring(i+1,dBFav.length());
                            }
                            else {
                                Log.d("Favourites_Debugging", dBFav + "i=" + i);
                                newDBFav = dBFav.substring(0, i - 1) + dBFav.substring(i + bID.length(), dBFav.length());
                            }
                        }
                        dr.child("FAVOURITES").setValue(newDBFav);
                        favHolder.del.setEnabled(false);
                        favHolder.del.setVisibility(View.INVISIBLE);
                        favHolder.download.setEnabled(false);
                        favHolder.download.setVisibility(View.INVISIBLE);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        favHolder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c, "Downloading", Toast.LENGTH_SHORT).show();
                DownloadManager downloadManager;
                String url=favourites.get(position).getBookUrl();
                downloadManager = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri =Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setDestinationInExternalFilesDir(c, Environment.DIRECTORY_DOWNLOADS,favHolder.bookName.getText().toString());
                //request.setVisibleInDownloadsUi(true);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });
   /*     favHolder.del.setItemClickListener(new ItemClickListener() {
            @Override
            public void onIemClick(View v, int pos) {
                final String bID=favourites.get(pos).getBookID();
                final DatabaseReference dr= FirebaseDatabase.getInstance().getReference("USERS/"+uID);
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String dBFav=dataSnapshot.child("FAVOURITES").getValue(String.class);
                       // String[] favArray=dBFav.split(",");
                        int i=dBFav.indexOf(bID+",");
                        String newDBFav=dBFav.substring(0,i)+dBFav.substring(i+bID.length(),dBFav.length());
                        dr.child("FAVOURITES").setValue(newDBFav);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }
}
