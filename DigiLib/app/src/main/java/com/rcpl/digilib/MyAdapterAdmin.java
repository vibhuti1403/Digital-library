package com.rcpl.digilib;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by vartika on 19/02/2018.
 */

public class MyAdapterAdmin extends RecyclerView.Adapter<MyHolderAdmin> {

    Context c;
    ArrayList<Players> players;
    ArrayList<Players>checkedPlayers=new ArrayList<>();
    ArrayList<Integer> sendpos=new ArrayList<>();


    public MyAdapterAdmin(Context c, ArrayList<Players> players) {
        this.c = c;
        this.players = players;
        sendpos.add(new Integer(-1));
    }

    @Override
    public MyHolderAdmin onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.model,null);
        MyHolderAdmin holder=new MyHolderAdmin(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolderAdmin holder, final int position) {
        holder.tv1.setText(players.get(position).getBookName());
        holder.tv2.setText(players.get(position).getBookAuthor());




        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.ctx,UpdateActivity.class);
                i.putExtra("BID", players.get(position).getBookId());
                holder.ctx.startActivity(i);
            }
        });





        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onIemClick(View v, int pos) {
                final int p=pos;
               /* v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // v.findViewById(R.id.checkBox);
                        Toast.makeText(c, "pos"+p, Toast.LENGTH_SHORT).show();

                    }
                });*/

                CheckBox chk=(CheckBox)v.findViewById(R.id.checkBox);
                //Button btn=(Button)v.findViewById(R.id.button6);
               /* btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(c, "pos"+p, Toast.LENGTH_SHORT).show();
                    }
                });*/
                if(chk.isChecked())
                {
                    checkedPlayers.add(players.get(pos));
                    sendpos.add(pos);
                  //  Toast.makeText(c, ""+pos, Toast.LENGTH_SHORT).show();
                }
                else if(!chk.isChecked())
                {
                    checkedPlayers.remove(players.get(pos));
                    sendpos.remove(new Integer(pos));
                 //   Toast.makeText(c, ""+pos, Toast.LENGTH_SHORT).show();
                }

            }
        });
        // holder.setItemClickListener();
    }
    @Override
    public int getItemCount() {
        return players.size();
    }
}
