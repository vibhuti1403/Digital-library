package com.rcpl.digilib;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class viewPdfImg extends AppCompatActivity implements SensorEventListener {
    ImageView iv[];
    LinearLayout ll;
    SensorManager sm;
    String images[];
    Menu menu;
    int i=0,flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf_img);
        ll= (LinearLayout) findViewById(R.id.myLayout);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String img = bundle.getString("IMAGE URL");



        images=img.split(",");

        iv = new ImageView[10];
        iv[0] = (ImageView) findViewById(R.id.img1);
        iv[1] = (ImageView) findViewById(R.id.img2);
        iv[2] = (ImageView) findViewById(R.id.img3);
        iv[3] = (ImageView) findViewById(R.id.img4);
        iv[4] = (ImageView) findViewById(R.id.img5);
        iv[5] = (ImageView) findViewById(R.id.img6);
        iv[6] = (ImageView) findViewById(R.id.img7);
        iv[7] = (ImageView) findViewById(R.id.img8);
        iv[8] = (ImageView) findViewById(R.id.img9);
        iv[9] = (ImageView) findViewById(R.id.img10);

        for ( i=0;i<images.length;i++)
        Picasso.with(this).load(images[i])
                .into(iv[i]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.images_view,menu);
        this.menu=menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        MenuItem m = menu.findItem(R.id.action_settings);
        if(id==R.id.action_settings && flag==0)
        {
            Toast.makeText(this, "Please Use Proximity Sensor", Toast.LENGTH_SHORT).show();
            ll.setOrientation(LinearLayout.HORIZONTAL);
            sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
            int rollNoOfSensor = Sensor.TYPE_PROXIMITY;
            Sensor s = sm.getDefaultSensor(rollNoOfSensor);
            sm.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);
            i=0;

            m.setTitle("Line");
            flag=1;
        }
        else if(id==R.id.action_settings && flag==1)
        {   ll.setOrientation(LinearLayout.VERTICAL);
            m.setTitle("Page");
            flag=0;
            sm.unregisterListener(this);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float f = sensorEvent.values[0];  //values is an float type array
        //hand removed changes val
        //proximity sensor return only one value
        //Toast.makeText(this, "value of sensor: "+f, Toast.LENGTH_SHORT).show();


        if(f == 0.0)
        {
            Picasso.with(this).load(images[i])
                    .into(iv[0]);
            i++;
            if(i==images.length)
                i=0;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(flag==1)
        sm.unregisterListener(this);            //separating code and h/w

    }
}
