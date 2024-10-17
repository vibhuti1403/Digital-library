package com.rcpl.digilib;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    final static int PICK_PDF_CODE = 2342;
    int flag = 0,c=0;
    int flag1=0;
    Uri url,url1;
    String imageUrl,userType,userID;
    int bid;
    FirebaseDatabase FDB;
    StorageReference mStorageReference;
    DatabaseReference mDatabaseReference,myCount,authorRef;
    String bookName,Author,Edition,Publication,description,myAuthor,a;
    EditText etName,etAuthor,etEdition,etPublication,etDesc;
    TextView textViewStatus;
    ProgressBar progressBar;
    Spinner sp;
    Button btnUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        etName=(EditText)findViewById(R.id.ETBookName);
        etAuthor=(EditText)findViewById(R.id.ETAuthorNAme);
        etEdition=(EditText)findViewById(R.id.ETEdition);
        etPublication=(EditText)findViewById(R.id.ETPublication);
        etDesc=(EditText)findViewById(R.id.ETDescription);
        sp=(Spinner)findViewById(R.id.spinner) ;
        btnUpload = (Button) findViewById(R.id.button5);
        btnUpload.setEnabled(false);

        textViewStatus = (TextView) findViewById(R.id.textView7);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        FDB = FirebaseDatabase.getInstance();

        authorRef = FDB.getReference("Author");
        mStorageReference = FirebaseStorage.getInstance().getReference();
        myCount = FDB.getReference("COUNT");
        myCount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bid = dataSnapshot.getValue(Integer.class);
                bid++;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Toast.makeText(this, bid+"", Toast.LENGTH_SHORT).show();
        //func();
        String[] gen={"BIOGRAPHY","HISTORY","SCIENCE","MATH","LITERATURE","RELIGION"};
        ArrayAdapter adp=new ArrayAdapter(this,R.layout.author_row,gen);
        sp.setAdapter(adp);


        Intent myintent = getIntent();
        Bundle b = myintent.getExtras();
        userID = b.getString("UID");
        userType = b.getString("TYPE");


    }


/*public void func()
{
    Toast.makeText(this, bid+"##", Toast.LENGTH_SHORT).show();
    //myCount.setValue(bid);
}
*/

    //this function will get the pdf from the storage
    public void getPDF(View v) {


        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2);
    }

/*    public void addAuthor(String auth,String val)
    {
        Toast.makeText(this, "ADDING"+val, Toast.LENGTH_SHORT).show();
        String ab="Book"+bid;
        ab = val+","+ab;
        Toast.makeText(this, ab, Toast.LENGTH_SHORT).show();
        if(ab.contains("null"))
            ab=ab.substring(5);

        authorRef = FDB.getReference("Author");
        authorRef.child(auth).setValue(ab);

    }*/


    public void writeFirebase()
    {
        bookName = etName.getText().toString();
        Author=etAuthor.getText().toString().toUpperCase();
        Edition=etEdition.getText().toString();
        Publication=etPublication.getText().toString();
        description= etDesc.getText().toString();
        myCount.setValue(bid);

        mDatabaseReference = FDB.getReference("Books/Book"+bid);
     //   Toast.makeText(this, Edition+Publication+description, Toast.LENGTH_LONG).show();
     //   Toast.makeText(this, url.toString(), Toast.LENGTH_SHORT).show();
       mDatabaseReference.child("BOOKID").setValue("Book"+bid);
        mDatabaseReference.child("AUTHOR").setValue(Author);
        mDatabaseReference.child("URL").setValue(url.toString());
        //genre;
        mDatabaseReference.child("NAME").setValue(bookName);
        mDatabaseReference.child("EDITION").setValue(Edition);
        mDatabaseReference.child("PUBLICATION").setValue(Publication);
        mDatabaseReference.child("DESCRIPTION").setValue(description);
        //mDatabaseReference.child("URL").setValue(url);
        if(imageUrl.contains("null"))
            imageUrl=imageUrl.substring(5);
        mDatabaseReference.child("IMAGES").setValue(imageUrl);
        mDatabaseReference.child("GENRE").setValue(sp.getSelectedItem().toString());
        DatabaseReference dr=FirebaseDatabase.getInstance().getReference("Genre");
        final DatabaseReference dr1=FirebaseDatabase.getInstance().getReference("Genre");
        DatabaseReference dr2=FirebaseDatabase.getInstance().getReference("Author");
        final DatabaseReference dr4=FirebaseDatabase.getInstance().getReference("Author");
        if(userType.equals("ADMIN")) {
            mDatabaseReference.child("VISIBILITY").setValue("TRUE");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dp:dataSnapshot.getChildren())
                    {
                        if(sp.getSelectedItem().toString().equals(dp.getKey()))
                        {
                            dr1.child(dp.getKey()).setValue(dataSnapshot.child(dp.getKey()).getValue(String.class)+"Book"+bid+",");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            dr2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseReference dr3;
                    String temp1[],temmp="";
                    temp1=Author.split(",");
                    for(String temp:temp1)
                    {
                        temmp=temp;
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
                        {
                            flag1 = 0;
                            if (temp.equals(postSnapshot.getKey()))
                            {
                                flag1 = 1;
                                break;
                            }
                        }
                        if (flag1 == 0) {
                        dr3 = FirebaseDatabase.getInstance().getReference("Author/" + temmp);
                        dr3.setValue("yo");
                    }

                    }
                    dr4.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference dr5=FirebaseDatabase.getInstance().getReference("Author");
                            String[] temp2=Author.split(",");
                            for(String temp:temp2)
                            {
                                if(dataSnapshot.child(temp).getValue().equals("yo"))
                                    dr5.child(temp).setValue("Book"+bid+",");
                                else
                                    dr5.child(temp).setValue(dataSnapshot.child(temp).getValue()+"Book"+bid+",");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
                    }

        //Toast.makeText(this, "U::"+userType, Toast.LENGTH_SHORT).show();
        else
            mDatabaseReference.child("VISIBILITY").setValue("FALSE");
        //finish();





        authorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                a=dataSnapshot.child(Author).getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
     //   Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        //addAuthor(Author,a);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);

        //upload file
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            StorageReference path = mStorageReference.child(uri.getLastPathSegment());
            path.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UploadActivity.this, "UPLOADED", Toast.LENGTH_SHORT).show();
                    url = taskSnapshot.getDownloadUrl();
               //     Toast.makeText(UploadActivity.this, "URL: "+url.toString(), Toast.LENGTH_SHORT).show();
                    if(flag == 1)

                    writeFirebase();
                    else
                        Toast.makeText(UploadActivity.this, "Please Upload Pictures", Toast.LENGTH_SHORT).show();


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress ;

                    progress= (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    if(progress < 100.0)
                    textViewStatus.setText((int)progress + "% Uploading...");
                    else
                    {
                        textViewStatus.setText("Uploaded");
                        Intent i=new Intent(UploadActivity.this,DashBoardActivity.class);
                        i.putExtra("TYPE","USER");
                        i.putExtra("UID",userID);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }



        //upload images
        if(requestCode==1 && resultCode==RESULT_OK)
        {

            if(data.getClipData() !=null)
            {
                Toast.makeText(this, "MULTIPLE", Toast.LENGTH_SHORT).show();

                for(int i =0 ;i<data.getClipData().getItemCount();i++)
                {   c= data.getClipData().getItemCount();
                    Uri fileUri  = data.getClipData().getItemAt(i).getUri();
                    StorageReference path = mStorageReference.child(fileUri.getLastPathSegment());
                    path.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            c--;
                            if(c==0)
                            {
                                Toast.makeText(UploadActivity.this, "All Files are UPLOADED", Toast.LENGTH_SHORT).show();
                                btnUpload.setEnabled(true);
                            }
                            url1 =taskSnapshot.getDownloadUrl();
                            imageUrl = imageUrl + "," + url1.toString();
                            flag =1;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress ;

                            progress= (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            if(progress < 100.0)

                                textViewStatus.setText((int)progress + "% Uploading...");

                            else
                                textViewStatus.setText("Uploaded");


                        }
                    });


                }



            }
            else if(data.getData()!=null)
            {
                Toast.makeText(this, "Please Select at least 2 images", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //this method is uploading the file
    //the code is same as the previous tutorial
    //so we are not explaining it

    public void upload(View v)
    {
        if (etName.getText().toString().trim().isEmpty())
            etName.setError("Enter Book Name");
        else
        if (etAuthor.getText().toString().trim().isEmpty())
            etAuthor.setError("Enter Author Name");
        else
        if (etEdition.getText().toString().trim().isEmpty())
            etEdition.setError("Enter Edition");
        else
        if (etPublication.getText().toString().trim().isEmpty())
            etPublication.setError("Enter Publication");
        else
        if (etDesc.getText().toString().trim().isEmpty())
            etDesc.setError("Enter Description");

        else{
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.putExtra(Intent.CATEGORY_APP_GALLERY, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);

            etName.setEnabled(false);
            etAuthor.setEnabled(false);
            etEdition.setEnabled(false);
            etPublication.setEnabled(false);
            etDesc.setEnabled(false);
            sp.setEnabled(false);
        startActivityForResult(Intent.createChooser(intent, "Select picture"), 1);
    }

    }

}
