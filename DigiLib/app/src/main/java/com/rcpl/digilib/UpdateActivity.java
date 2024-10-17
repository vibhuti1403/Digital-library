package com.rcpl.digilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateActivity extends AppCompatActivity {

    EditText etName,etAuthor,etEdition,etPublication,etDesc;
    Spinner sp;
    String bookId;
    DatabaseReference dr;
    String img="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        etName=(EditText)findViewById(R.id.ETBookName);
        etAuthor=(EditText)findViewById(R.id.ETAuthorNAme);
        etEdition=(EditText)findViewById(R.id.ETEdition);
        etPublication=(EditText)findViewById(R.id.ETPublication);
        etDesc=(EditText)findViewById(R.id.ETDescription);
        sp=(Spinner)findViewById(R.id.spinner) ;
        String[] gen={"BIOGRAPHY","HISTORY","SCIENCE","MATH","LITERATURE","RELIGION"};
        ArrayAdapter adp=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,gen);
        sp.setAdapter(adp);

        /*etName.setEnabled(false);
        etAuthor.setEnabled(false);
        etEdition.setEnabled(false);
        etPublication.setEnabled(false);
        etDesc.setEnabled(false);
        sp.setEnabled(false);*/
        //set texts
        Intent i=getIntent();
        bookId=i.getStringExtra("BID");
        dr= FirebaseDatabase.getInstance().getReference("Books/"+bookId);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                etName.setText(dataSnapshot.child("NAME").getValue().toString());
                etAuthor.setText(dataSnapshot.child("AUTHOR").getValue().toString());
                etEdition.setText(dataSnapshot.child("EDITION").getValue().toString());
                etPublication.setText(dataSnapshot.child("PUBLICATION").getValue().toString());
                etDesc.setText(dataSnapshot.child("DESCRIPTION").getValue().toString());
                img=dataSnapshot.child("IMAGES").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void UpdateDatabase(View v)
    {
        dr.child("NAME").setValue(etName.getText().toString());
        dr.child("AUTHOR").setValue(etAuthor.getText().toString());
        dr.child("EDITION").setValue(etEdition.getText().toString());
        dr.child("PUBLICATION").setValue(etPublication.getText().toString());
        dr.child("DESCRIPTION").setValue(etDesc.getText().toString());
    }

    public void ViewBooks(View v)
    {
        Intent intent = new Intent(UpdateActivity.this,viewPdfImg.class);
        intent.putExtra("IMAGE URL",img);
        startActivity(intent);
    }

}
