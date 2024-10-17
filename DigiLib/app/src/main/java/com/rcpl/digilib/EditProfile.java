package com.rcpl.digilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {
    EditText et1,et2,et3,et4;
    TextView tv1,tv2;
    DatabaseReference dr;
    FirebaseDatabase fdb;
    String uid,userType;
    String name,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        et1=(EditText)findViewById(R.id.editText4);
        et2=(EditText)findViewById(R.id.editText5);
        et3=(EditText)findViewById(R.id.editText6);
        et4=(EditText)findViewById(R.id.editText7);
        tv1=(TextView)findViewById(R.id.textView6);
        tv2=(TextView)findViewById(R.id.textView7);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        userType = b.getString("TYPE");
        uid = b.getString("UID");

        fdb = FirebaseDatabase.getInstance();
        dr= fdb.getReference("USERS");
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name=dataSnapshot.child(uid).child("NAME").getValue(String.class);
                pass=dataSnapshot.child(uid).child("PASS").getValue(String.class);
        //        Toast.makeText(EditProfile.this, name, Toast.LENGTH_SHORT).show();
                et1.setText(name);
                et2.setText(pass);

                et1.setEnabled(false);
                et2.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void nameChange(View view)
    {
        et1.setEnabled(true);
    }
    public void passChange(View view)
    {
         et2.setEnabled(true);
        et2.setText("");
        et2.setHint("Enter old Password");
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        et2.setEnabled(true);
        et3.setVisibility(View.VISIBLE);
        et4.setVisibility(View.VISIBLE);
    }

    public void onSubmit(View view)
    {
        String names;


        dr= fdb.getReference("USERS/"+uid);
        names = et1.getText().toString();
        if(et2.getText().toString().trim().isEmpty())
           et2.setError("Please Enter Old Password");

        if(et2.getText().toString().equals(pass)){
            if(et1.isEnabled())
            {
                dr.child("NAME").setValue(names);
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditProfile.this, DashBoardActivity.class);
                i.putExtra("TYPE", "USER");
                i.putExtra("UID", uid);
                startActivity(i);
            }

            if(et2.isEnabled())
            {
                if(et3.getText().toString().trim().isEmpty())
                {
                   et3.setError("Please Enter new Password");
                }
                else
                {
                    if(et3.getText().toString().equals(et4.getText().toString()))
                    {
                        dr.child("PASS").setValue(et3.getText().toString());
                        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(EditProfile.this, DashBoardActivity.class);
                        i.putExtra("TYPE", "USER");
                        i.putExtra("UID", uid);
                        startActivity(i);
                    }
                    else
                        Toast.makeText(EditProfile.this, "Confirm New Passowrd", Toast.LENGTH_LONG).show();
                }
            }


        }

        else

            Toast.makeText(this, "Wrong Password Entered", Toast.LENGTH_SHORT).show();

    }
}

