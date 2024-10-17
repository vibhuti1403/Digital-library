package com.rcpl.digilib;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by vartika on 19/02/2018.
 */

public class MyHolderAdmin extends RecyclerView.ViewHolder implements View.OnClickListener {
TextView tv1,tv2;
    CheckBox chk;
    ImageView update;
    Context ctx;
    ItemClickListener itemClickListener;

    public MyHolderAdmin(View itemView) {
        super(itemView);
//itemView.setOnClickListener(this);

        tv1=(TextView)itemView.findViewById(R.id.bookName);
        tv2=(TextView)itemView.findViewById(R.id.bookAuthor);
        chk=(CheckBox)itemView.findViewById(R.id.checkBox);
        //btn=(Button)itemView.findViewById(R.id.button6);
        update = itemView.findViewById(R.id.updateMe);
         chk.setOnClickListener(this);
        ctx = itemView.getContext();
        //btn.setOnClickListener(this);


    }
    public  void setItemClickListener(ItemClickListener ic)
    {
        this.itemClickListener=ic;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onIemClick(v,getLayoutPosition());
    }
}