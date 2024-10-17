package com.rcpl.digilib;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class Pass_Auth extends AppCompatActivity {

    private LinearLayout PhoneBar;
    private LinearLayout CodeBar;
    private EditText phone;
    private EditText code;
    private ProgressBar pb1;
    private ProgressBar pb2;
    private TextView tv1;
    private TextView tv2;
    PhoneAuthProvider.ForceResendingToken mResend;
    private Button send,verify;
    private FirebaseAuth mAuth;
    Intent i;
    String pno;
    String mVerification;
    DatabaseReference dr;
    int flag=0;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass__auth);
        mAuth = FirebaseAuth.getInstance();
        PhoneBar = (LinearLayout) findViewById(R.id.linear1);
        CodeBar = (LinearLayout) findViewById(R.id.linear2);
        phone = (EditText) findViewById(R.id.ETphone);
        code = (EditText) findViewById(R.id.ETcode);
        pb1 = (ProgressBar) findViewById(R.id.PBphone);
        pb2 = (ProgressBar) findViewById(R.id.PBcode);
        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.note);
        send = (Button) findViewById(R.id.sendNoti);
        verify = (Button) findViewById(R.id.btnVerify);
        pb2.setVisibility(View.INVISIBLE);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(phone.getText().toString().trim().isEmpty() || phone.getText().toString().length()<10)
                    phone.setError("Please Enter Valid Phone Number");
                else {
                    final String pno = "+91" + phone.getText().toString();

                    pb1.setVisibility(View.VISIBLE);
                    phone.setEnabled(false);
                    dr = FirebaseDatabase.getInstance().getReference("USERS");
                    dr.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                //  Toast.makeText(Pass_Auth.this, postSnapshot.getKey(), Toast.LENGTH_LONG).show();
                                if (postSnapshot.getKey().equals(phone.getText().toString())) {
                                    flag = 1;
                                    Toast.makeText(Pass_Auth.this, "User Registered", Toast.LENGTH_LONG).show();
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            pno,        // Phone number to verify
                                            60,         // Timeout duration
                                            TimeUnit.SECONDS,        // Unit of timeout
                                            Pass_Auth.this,               // Activity (for callback binding)
                                            mCallbacks        // OnVerificationStateChangedCallbacks
                                    );
                                }
                            }
                            if (flag == 0) {
                                pb1.setVisibility(View.INVISIBLE);
                                phone.setEnabled(true);
                                Toast.makeText(Pass_Auth.this, "User Not Registered", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    });
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = code.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerification, verificationCode);
                signInWithPhoneAuthCredential(credential);
                pb2.setVisibility(View.VISIBLE);

            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                i = new Intent(Pass_Auth.this, Recover_pass.class);
                i.putExtra("pno", phone.getText().toString());
                Toast.makeText(Pass_Auth.this, "Code verified", Toast.LENGTH_SHORT).show();
                startActivity(i);
                finish();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Pass_Auth.this, "Code verification failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerification = s;
                mResend = forceResendingToken;
                pb1.setVisibility(View.INVISIBLE);
                send.setVisibility(View.INVISIBLE);
                verify.setVisibility(View.VISIBLE);
                CodeBar.setVisibility(View.VISIBLE);
                Toast.makeText(Pass_Auth.this, "CODE IS SENT", Toast.LENGTH_SHORT).show();
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
                            i = new Intent(Pass_Auth.this, Recover_pass.class);
                            i.putExtra("pno", phone.getText().toString());
                            Toast.makeText(Pass_Auth.this, "Code verified", Toast.LENGTH_SHORT).show();
                            startActivity(i);
                            //                          Toast.makeText(AuthActivity.this, "USER: " +uid, Toast.LENGTH_SHORT).show();

                        } else {
                            // Sign in failed, display a message and update the UI
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Pass_Auth.this, "ERROR", Toast.LENGTH_SHORT).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Pass_Auth.this, "WRONG CODE ENTERED", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}

