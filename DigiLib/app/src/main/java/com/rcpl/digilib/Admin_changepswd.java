package com.rcpl.digilib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_changepswd extends AppCompatActivity {
    Button submit;
    EditText oldpwd,newpwd,cnewpwd;
    DatabaseReference dr;
    String pass;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_changepswd);
        submit=(Button)findViewById(R.id.button7);
        oldpwd=(EditText)findViewById(R.id.editText);
        newpwd=(EditText)findViewById(R.id.editText9);
        cnewpwd=(EditText)findViewById(R.id.editText10);
        Intent i=getIntent();
        uid=i.getStringExtra("uid");
        dr= FirebaseDatabase.getInstance().getReference("ADMIN/"+uid);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(oldpwd.getText().toString().trim().isEmpty())
                    oldpwd.setError("Please Enter Old Password");
                else if(oldpwd.getText().toString().trim().isEmpty())
                    oldpwd.setError("Please Enter New Password");
                else if(oldpwd.getText().toString().trim().isEmpty())
                    oldpwd.setError("Please Confirm New Password");
                else {
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pass = dataSnapshot.child("PASS").getValue(String.class);
                            Toast.makeText(Admin_changepswd.this, pass + uid, Toast.LENGTH_SHORT).show();
                            if (oldpwd.getText().toString().equals(pass)) {
                                Toast.makeText(Admin_changepswd.this, pass, Toast.LENGTH_SHORT).show();
                                if ((!newpwd.getText().toString().equals("")) && (newpwd.getText().toString().length() >= 8) && (newpwd.getText().toString().equals(cnewpwd.getText().toString()))) {
                                    dr.child("PASS").setValue(newpwd.getText().toString());
                                    Toast.makeText(Admin_changepswd.this, "Password changed", Toast.LENGTH_SHORT).show();
                                    finish();
                                    //  Intent in=new Intent(Admin_changepswd.this,AdminPage.class);
                                    //  startActivity(in);
                                } else {
                                    newpwd.setError("Enter a VALID Password");
                                    //  Toast.makeText(Admin_changepswd.this, "enter a valid password (atleast 8 characters)", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                oldpwd.setError("Old Password does NOT match");
                            }
                            //  Toast.makeText(Admin_changepswd.this, "old password does not match", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            }
        });
    }
}

