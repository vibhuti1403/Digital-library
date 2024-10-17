package com.rcpl.digilib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddReqActivity extends AppCompatActivity {
    EditText et1,et2,et3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_req);
        et1=(EditText)findViewById(R.id.editText1);
        et2=(EditText)findViewById(R.id.editText2);
        et3=(EditText)findViewById(R.id.editText3);
    }
    public void submit(View v)
    {
        String book=et1.getText().toString();
        String author=et2.getText().toString();
        String publication=et3.getText().toString();

        if(et1.getText().toString().trim().isEmpty())
            et1.setError("Please Enter Book Name!");
        else
            if(et2.getText().toString().trim().isEmpty())
                et2.setError("Please Enter Author Name!");
            else
                    if(et3.getText().toString().trim().isEmpty())
                        et3.setError("Please Enter Publication!");
                    else {


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("Request/" + book);
                        myRef.child("Auth").setValue(author);
                        myRef.child("Pub").setValue(publication);
                        myRef.child("Bk").setValue(book);


                        Toast.makeText(this, "Firebase book details saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }

    }
}
