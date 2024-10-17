package com.rcpl.digilib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import ru.dimorinny.showcasecard.position.BottomRight;
import ru.dimorinny.showcasecard.position.Position;
import ru.dimorinny.showcasecard.position.TopRight;
import ru.dimorinny.showcasecard.step.ShowCaseStep;
import ru.dimorinny.showcasecard.step.ShowCaseStepDisplayer;

public class DashBoardActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    String userType,userID,phoneNum,showTour,signed;
    SharedPreferences sp;
    View v;
    Menu menu;
    MenuItem menuItem;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        userType = b.getString("TYPE");
        userID = b.getString("UID");

        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTour();
            }
        });


        FirebaseAuth.getInstance().signInAnonymously();


        File f = new File(
                "/data/data/" + getPackageName() +  "/shared_prefs/" + "sharedFile.xml");
        if (f.exists())
        {
            sp = getSharedPreferences("sharedFile",0);
            phoneNum = sp.getString("UID",null);
            showTour=sp.getString("FIRST",null);
            signed=sp.getString("ISSIGNED",null);

        }
        else

        phoneNum = b.getString("PHONE");


        if(showTour.equals("1"))
        {   SharedPreferences.Editor editor =sp.edit();
            editor.putString("FIRST", "0");
            editor.commit();
            myTour();
        }

        if(userType.equals("GUEST"))
            myTour();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        if(userType.equals("GUEST"))
            getMenuInflater().inflate(R.menu.menu_guest, menu);
        else
        {
            getMenuInflater().inflate(R.menu.menu_dash_board, menu);
            this.menu=menu;
            notifyUser();
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent i;

        switch (id)
        {
            case R.id.action_register:
                i = new Intent(DashBoardActivity.this,MainActivity.class);
               startActivity(i);
                finish();
            case R.id.action_settings:
                signout();
                break;
            case R.id.action_edit:
                i = new Intent(DashBoardActivity.this,EditProfile.class);
                i.putExtra("TYPE",userType);
                i.putExtra("UID",userID);
                startActivity(i);

                break;
            case R.id.action_upload:
                i = new Intent(DashBoardActivity.this,UploadActivity.class);
                i.putExtra("TYPE",userType);
                i.putExtra("UID",userID);
                startActivity(i);

                break;
            case R.id.action_search:

                i = new Intent(DashBoardActivity.this,TestActivity.class);
                i.putExtra("TAB",tabLayout.getSelectedTabPosition());
                startActivity(i);

                break;
            case R.id.action_request:
                if(userType.equals("GUEST"))
                    Toast.makeText(this, "You Need To Register First", Toast.LENGTH_SHORT).show();
                else
                {
                i = new Intent(DashBoardActivity.this,RequestActivity.class);
                startActivity(i);
                    }
                break;
            case R.id.action_favourites:
                i=new Intent(DashBoardActivity.this,FavActivity.class);
                i.putExtra("uid",userID);
                startActivity(i);
                break;

            case R.id.action_notification:
                final DatabaseReference dr=FirebaseDatabase.getInstance().getReference("USERS/"+userID);
                dr.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("NOTIFICATION").getValue().equals("UNSEEN"))
                        {

                            View viewLayout;
                            LayoutInflater layoutInflater=getLayoutInflater();
                            viewLayout=layoutInflater.inflate(R.layout.custom_toast,(ViewGroup)findViewById(R.id.custom_lay));
                            Toast toast1=Toast.makeText(DashBoardActivity.this, "Toast:Gravity.TOP", Toast.LENGTH_LONG);
                            toast1.setGravity(Gravity.CENTER,0,0);
                            toast1.setView(viewLayout);
                            toast1.show();
                            dr.child("NOTIFICATION").setValue("SEEN");
                            notifyUser();
                        }
                        else
                            Toast.makeText(DashBoardActivity.this, "No New Notification", Toast.LENGTH_SHORT).show();

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }

        return super.onOptionsItemSelected(item);
    }


    public void notifyUser()
    {
        menuItem=menu.findItem(R.id.action_notification);

    DatabaseReference dr=FirebaseDatabase.getInstance().getReference("USERS/"+userID);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

             if (dataSnapshot.child("NOTIFICATION").getValue().equals("UNSEEN"))
             {
                menuItem.setIcon(R.drawable.newbellicon);
             }
             else
                 menuItem.setIcon(R.drawable.bellicon);

   }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0: Tab1genre tab1=new Tab1genre();
                    return tab1;
                case 1: Tab2Book tab2=new Tab2Book();
                    return tab2;
                case 2: Tab3author tab3=new Tab3author();
                    return tab3;

                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "GENRES";
                case 1:
                    return "BOOKS";
                case 2:
                    return "AUTHORS";
            }
            return null;
        }
    }


    public void signout()
    {
        FirebaseDatabase fdb = FirebaseDatabase.getInstance();

  //      Toast.makeText(this, "PHONENUM:"+phoneNum, Toast.LENGTH_SHORT).show();

        DatabaseReference DBref = fdb.getReference("USERS/"+phoneNum);
        DBref.child("SIGNIN").setValue("NO");

        SharedPreferences sp = getSharedPreferences("sharedFile",0);
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("ISSIGNED", "NO");
        editor.commit();

       // Intent intent = new Intent(DashBoardActivity.this,SpashScreenActivity.class);
        //startActivity(intent);
        finish();
    }

    public void myTour()
    {
        PointF req=new PointF(300,50);
        PointF search=new PointF(380,50);
        PointF genre=new PointF(70,200);
        PointF books=new PointF(250,200);
        PointF auth=new PointF(430,200);

        if(userType.equals("GUEST"))
        {new ShowCaseStepDisplayer.Builder(DashBoardActivity.this)
                .addStep(new ShowCaseStep(new TopRight(),"Register"))
                .addStep(new ShowCaseStep(new Position(search),"Search"))
                .addStep(new ShowCaseStep(new Position(genre),"This tab displays genres"))
                .addStep(new ShowCaseStep(new Position(books),"This tab displays books"))
                .addStep(new ShowCaseStep(new Position(auth),"This tab displays authors"))
                .addStep(new ShowCaseStep(new BottomRight(),"Revisit the tour"))
                .build().start();}
        else
        {new ShowCaseStepDisplayer.Builder(DashBoardActivity.this)
                .addStep(new ShowCaseStep(new TopRight(),"Edit Profile,Upload Files etc."))
                .addStep(new ShowCaseStep(new Position(search),"Notification and Search"))
                .addStep(new ShowCaseStep(new Position(genre),"This tab displays genres"))
                .addStep(new ShowCaseStep(new Position(books),"This tab displays books"))
                .addStep(new ShowCaseStep(new Position(auth),"This tab displays authors"))
                .addStep(new ShowCaseStep(new BottomRight(),"Revisit the tour"))
                .build().start();}



    }
}
