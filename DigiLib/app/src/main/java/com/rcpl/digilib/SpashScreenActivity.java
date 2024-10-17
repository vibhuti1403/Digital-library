package com.rcpl.digilib;

import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class SpashScreenActivity extends AppCompatActivity{
    private GLSurfaceView glSurfaceView;

    String Colors[] = {"RED", "BLACK", "BLUE", "CYAN", "YELLOW"};
    String msg="NO",typeOfUSer="NO";
    SharedPreferences sp;
    Thread t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        //Toast.makeText(this, "Tap Screen to go to next Activity", Toast.LENGTH_SHORT).show();
        glSurfaceView = new GLSurfaceView(this);

        glSurfaceView.setRenderer(new MyGLRenderer(this));
        this.setContentView(glSurfaceView);


        File f = new File(
                "/data/data/" + getPackageName() + "/shared_prefs/" + "sharedFile.xml");
        if (f.exists()) {
            sp = getSharedPreferences("sharedFile", 0);
            msg = sp.getString("ISSIGNED", null);
            typeOfUSer=sp.getString("TYPE",null);
            msg=msg+" ";
            if(msg.contains("null"))
                msg="NO";
            else
                msg=msg.trim();


        } else {

            msg = "NO";
            typeOfUSer="NO";
        }

        //Toast.makeText(this, "MSG=" + msg+",USERTYPR:"+typeOfUSer, Toast.LENGTH_SHORT).show();


       t = new Thread()

        {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);//time in millisec


                    if (msg.equals("YES") && typeOfUSer.equals("USER")) {

                        Intent i = new Intent(SpashScreenActivity.this, DashBoardActivity.class);
                        i.putExtra("TYPE","USER");
                        i.putExtra("UID", sp.getString("UID", null));
                        startActivity(i);
                        finish();
                    }
                    else if(msg.equals("YES") && typeOfUSer.equals("ADMIN"))
                    {
                        Intent i = new Intent(SpashScreenActivity.this, AdminPage.class);
                        i.putExtra("TYPE", sp.getString("TYPE", null));
                        i.putExtra("UID", sp.getString("UID", null));
                        startActivity(i);
                        finish();

                    }
                    else {
                        Intent i = new Intent(SpashScreenActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();



       /* glSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                t.stop();
                if (msg.equals("YES")) {

                    Intent i = new Intent(SpashScreenActivity.this, DashBoardActivity.class);
                    i.putExtra("TYPE", sp.getString("TYPE", null));
                    i.putExtra("UID", sp.getString("UID", null));
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SpashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        });*/




    }




    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

}
