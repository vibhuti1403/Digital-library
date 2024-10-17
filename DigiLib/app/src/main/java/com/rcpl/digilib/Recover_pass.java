package com.rcpl.digilib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Recover_pass extends AppCompatActivity {

    DatabaseReference dr;
    EditText et1,et2;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pass);
        Intent ic=getIntent();
        uid=ic.getStringExtra("pno");;
        et1=(EditText)findViewById(R.id.editText3);
        et2=(EditText)findViewById(R.id.editText8);
    }
    public void onSubmit(View view)
    {
        dr= FirebaseDatabase.getInstance().getReference("USERS/"+uid);
        if(et1.getText().toString().trim().isEmpty()|| et1.getText().toString().length()<8 )
            et1.setError("Enter Valid Password");
        else if(et2.getText().toString().trim().isEmpty())

            et2.setError("Enter Valid Password");

        else
        {
            if(et1.getText().toString().equals(et2.getText().toString()))
            {
                dr.child("PASS").setValue(et1.getText().toString());
                Toast.makeText(this, "Password recovered", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Recover_pass.this,DashBoardActivity.class);
                intent.putExtra("PHONE",uid);
                intent.putExtra("TYPE","USER");
                intent.putExtra("UID",uid);
                startActivity(intent);
            }

            else
                Toast.makeText(Recover_pass.this, "Confirm New Password", Toast.LENGTH_LONG).show();
        }

    }
}
