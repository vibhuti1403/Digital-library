package com.rcpl.digilib;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

public class LoginActivity extends AppCompatActivity {  EditText phone,pass;
    TextView tv1;
    FirebaseDatabase FDB;
    DatabaseReference DBref;
    SharedPreferences sp;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = (EditText) findViewById(R.id.ETphone);
        pass = (EditText) findViewById(R.id.ETpass);
        tv1 = (TextView) findViewById(R.id.textView);

        tv1.setVisibility(View.INVISIBLE);
        FDB = FirebaseDatabase.getInstance();
        File f = new File(
                "/data/data/" + getPackageName() + "/shared_prefs/" + "sharedFile.xml");
        if (!f.exists()) {
            sp = getSharedPreferences("sharedFile",0);
            SharedPreferences.Editor editor =sp.edit();
            editor.putString("FIRST","1");
            editor.commit();

        }

    }


    public void LoginGuest(View v)
    {
        Intent intent;
        intent = new Intent(LoginActivity.this,DashBoardActivity.class);
        intent.putExtra("TYPE","GUEST");
        intent.putExtra("UID","0000");
        startActivity(intent);
        finish();

    }

    public void check(int f)
    {
  //      Toast.makeText(this, "IN CHECK"+f, Toast.LENGTH_SHORT).show();
        Intent intent;
        if(f==1)
        {
            DBref = FDB.getReference("USERS/"+phone.getText().toString());
            DBref.child("SIGNIN").setValue("YES");

            sp = getSharedPreferences("sharedFile",0);
            SharedPreferences.Editor editor =sp.edit();
            editor.putString("ISSIGNED", "YES");
            editor.putString("TYPE", "USER");

            editor.putString("UID",phone.getText().toString());
            String n=sp.getString("FIRST",null);
            if(n.equals("0"))
                editor.putString("FIRST","0");
            else
            editor.putString("FIRST","1");
            editor.commit();

            intent = new Intent(LoginActivity.this,DashBoardActivity.class);
            intent.putExtra("PHONE",phone.getText().toString());
            intent.putExtra("TYPE","USER");

            intent.putExtra("UID",phone.getText().toString());
            startActivity(intent);
            finish();


        }

        else
            if(f==2)
        {
     //       Toast.makeText(this, "f==2", Toast.LENGTH_SHORT).show();
            DBref = FDB.getReference("ADMIN/"+phone.getText().toString());
            DBref.child("SIGNIN").setValue("YES");
            SharedPreferences sp = getSharedPreferences("sharedFile",0);
            SharedPreferences.Editor editor =sp.edit();
            editor.putString("ISSIGNED", "YES");
            editor.putString("TYPE","ADMIN");
            editor.putString("UID",phone.getText().toString());

            editor.commit();


            intent = new Intent(LoginActivity.this,AdminPage.class);
            intent.putExtra("PHONE",phone.getText().toString());
            intent.putExtra("TYPE","ADMIN");
            intent.putExtra("UID",phone.getText().toString());
            startActivity(intent);
            finish();
        }
        else
        {
       //     Toast.makeText(this, "f==0", Toast.LENGTH_SHORT).show();

            tv1.setText("Error in Login!");
            tv1.setVisibility(View.VISIBLE);


        }

    }


    public void frgtPass(View v)
    {

         Intent intent = new Intent(LoginActivity.this,Pass_Auth.class);
            startActivity(intent);
            finish();

    }

    public void Login(View v)
    {   final String type[]={"USER","ADMIN"};
        final String phoneNum = phone.getText().toString();
        final String password = pass.getText().toString();

        if(phoneNum.trim().isEmpty() || phoneNum.length()<10)
            phone.setError("Invalid Number");
        else
            if(password.trim().length()<8)
                pass.setError("Atleast 8 Characters");
        else {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Type");
                builder.setItems(type, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = type[which];
                        if (str.equals("USER")) {
                            DBref = FDB.getReference("USERS");
                            DBref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        if (postSnapshot.getKey().equals(phoneNum) && (postSnapshot.child("PASS").getValue().equals(password))) {
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
                        else if (str.equals("ADMIN")) {

                  //          Toast.makeText(getApplicationContext(), "IN ADMIN", Toast.LENGTH_SHORT).show();
                            DBref = FDB.getReference("ADMIN");
                            DBref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    //flag=2;
                                    //Toast.makeText(LoginActivity.this, dataSnapshot.getChildren().getKey()+""+postSnapshot.child("PASS").getValue().toString(), Toast.LENGTH_SHORT).show();
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                        //Toast.makeText(LoginActivity.this,postSnapshot.getKey()+""+postSnapshot.child("PASS").getValue().toString(), Toast.LENGTH_SHORT).show();
                                        if (postSnapshot.getKey().equals(phoneNum) && (postSnapshot.child("PASS").getValue().equals(password))) {
                                            flag = 2;

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

                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }

    }
    public void Register(View v)
    {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }



}

