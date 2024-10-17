package com.rcpl.digilib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class AuthActivity extends AppCompatActivity {

    LinearLayout PhoneBar,CodeBar;
    Button sendbtn;
    ImageView iv1,iv2;
    ProgressBar pb1,pb2;
    EditText phone,code;
    TextView tv1,tv2;
    String mVerification;
    PhoneAuthProvider.ForceResendingToken mResend;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    FirebaseDatabase FDB;
    DatabaseReference DBRef;
    int btnType = 0;
    String name,pass,ph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        name = bundle.getString("NAME");
        pass = bundle.getString("PASSWORD");
        ph = bundle.getString("PHONE");


        PhoneBar = (LinearLayout) findViewById(R.id.linear1);
        CodeBar = (LinearLayout) findViewById(R.id.linear2);

        phone = (EditText) findViewById(R.id.ETphone);
        code = (EditText) findViewById(R.id.ETcode);

        iv1 = (ImageView) findViewById(R.id.callimg);
        iv2 = (ImageView) findViewById(R.id.lockimg);

        pb1 = (ProgressBar) findViewById(R.id.PBphone);
        pb2 = (ProgressBar) findViewById(R.id.PBcode);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.note);
        sendbtn = (Button) findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        phone.setText("+91"+ph);
        phone.setEnabled(false);



        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnType == 0)

                {
                    sendbtn.setEnabled(false);
                    pb1.setVisibility(View.VISIBLE);
                    phone.setEnabled(false);
                    String PhoneNumber = phone.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            PhoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            AuthActivity.this,               // Activity (for callback binding)
                            mCallbacks);


                }

                else
                {
                    sendbtn.setEnabled(false);
                    pb2.setVisibility(View.VISIBLE);
                    String verificationCode = code.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                tv1.setText("verification Failed!");
                tv1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                btnType=1;
                mVerification = s;
                mResend = forceResendingToken;
                pb1.setVisibility(View.INVISIBLE);
                sendbtn.setText("Verify");
                sendbtn.setEnabled(true);
                CodeBar.setVisibility(View.VISIBLE);
                Toast.makeText(AuthActivity.this, "CODE IS SENT", Toast.LENGTH_SHORT).show();
            }
        };


    }




    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            //                          Toast.makeText(AuthActivity.this, "USER: " +uid, Toast.LENGTH_SHORT).show();
                            FDB = FirebaseDatabase.getInstance();
                            DBRef = FDB.getReference("USERS/"+ph);
                            DBRef.child("NAME").setValue(name);
                            DBRef.child("PASS").setValue(pass);
                            DBRef.child("SIGNIN").setValue("YES");
                            DBRef.child("FAVOURITES").setValue("XXX");
                            DBRef.child("NOTIFICATION").setValue("SEEN");
                            //Toast.makeText(AuthActivity.this, "NAME: "+mAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(AuthActivity.this, "NAME: "+mAuth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(AuthActivity.this,DashBoardActivity.class);

                            SharedPreferences sp = getSharedPreferences("sharedFile",0);
                            SharedPreferences.Editor editor =sp.edit();
                            editor.putString("ISSIGNED", "YES");
                            editor.putString("FIRST","1");
                            editor.putString("UID",phone.getText().toString().substring(3));
                            editor.putString("TYPE","USER");

                            editor.commit();

                            homeIntent.putExtra("TYPE","USER");
                            homeIntent.putExtra("UID",ph);
                            homeIntent.putExtra("PHONE",ph);
                            startActivity(homeIntent);
                            finish();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                            tv1.setText("Sign in Failed!");
                            tv1.setVisibility(View.VISIBLE);
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(AuthActivity.this, "WRONG CODE ENTERED", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
