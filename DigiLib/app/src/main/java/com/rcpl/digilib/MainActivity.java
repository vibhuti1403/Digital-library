package com.rcpl.digilib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText etName,etPass,etPhone;
    Intent intent;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.Name);
        etPass = (EditText) findViewById(R.id.pass);
        etPhone = (EditText) findViewById(R.id.phone);

        flag=0;
    }

    public void btnRegister(View v) {
        final String personName = etName.getText().toString();
        String personPass = etPass.getText().toString();
        final String personPhone = etPhone.getText().toString();

        if (personName.trim().isEmpty())
            etName.setError("Enter Name");
        else if (personPass.trim().isEmpty())
            etPass.setError("Enter Password");
        else if( personPass.trim().length()<8)
            etPass.setError("Atleast 8 Characters");
        else if (personPhone.trim().isEmpty() || personPhone.length() < 10)
            etPhone.setError("Invalid Number");

        else {
            intent = new Intent(MainActivity.this, AuthActivity.class);
            intent.putExtra("NAME", personName);
            intent.putExtra("PASSWORD", personPass);
            intent.putExtra("PHONE", personPhone);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("USERS");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.getKey().equals(personPhone)) {
                            flag = 1;

                        }

                    }
                    check(flag);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }



        public void check(int f)
    {
        if(f==1)
        {
            Toast.makeText(MainActivity.this, "Already Registered!", Toast.LENGTH_LONG).show();
            etPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    flag = 0;

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        else
            startActivity(intent);
        finish();

    }



    }

