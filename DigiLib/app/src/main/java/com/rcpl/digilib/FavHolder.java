package com.rcpl.digilib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by srish on 4/1/2018.
 */

public class FavHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView bookName,authorName;
    ImageView del;
    ImageView download;
    ItemClickListener items;
    Context ctx;
    public FavHolder(View itemView) {
        super(itemView);
        bookName=(TextView)itemView.findViewById(R.id.bookTV);
        authorName=(TextView)itemView.findViewById(R.id.authTV);
        del=(ImageView)itemView.findViewById(R.id.delete);
        download=(ImageView)itemView.findViewById(R.id.IVdown);
        //del.setOnClickListener(this);
        ctx=itemView.getContext();
    }
    public void setItemClickListener(ItemClickListener ic)
    {
        this.items=ic;
    }

    @Override
    public void onClick(View view) {
        this.items.onIemClick(view,getLayoutPosition());
    }
}
