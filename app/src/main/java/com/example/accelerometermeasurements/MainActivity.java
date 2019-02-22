package com.example.accelerometermeasurements;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String TAG = "Jeff";
    private SeekBar HugeMoveThreshold;
    private Button btnGoogle;
    private Button btnYahoo;
    private Button btnBing;
    private Button btnShake;
    private WebView wbvShow;

    private static int SignificantMove = 200;
    private float CurrentX,CurrentY,CurrentZ;
    private float LastX,LastY,LastZ;
    private float deltaX,deltaY,deltaZ;
    private float Z_inital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnGoogle = (Button)findViewById(R.id.btnGoogle);
        btnYahoo = (Button)findViewById(R.id.btnYahoo);
        btnBing = (Button)findViewById(R.id.btnBing);
        btnShake=(Button)findViewById(R.id.btnShake);
        HugeMoveThreshold = (SeekBar)findViewById(R.id.HugeMoveThreshold);
        wbvShow = (WebView)findViewById(R.id.wbvShow);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wbvShow.loadUrl("http://google.com");
                Log.i(TAG,"Google loaded.");
            }
        });

        btnYahoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wbvShow.loadUrl("http://yahoo.com");
                Log.i(TAG,"Yahoo loaded.");
            }
        });

        btnBing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wbvShow.loadUrl("http://bing.com");
                Log.i(TAG,"Bing loaded.");
            }
        });

        btnShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wbvShow.loadUrl("https://jumpingjaxfitness.files.wordpress.com/2010/07/dizziness.jpg");
                Log.i(TAG,"Shaking");
            }
        });


        HugeMoveThreshold.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SignificantMove = progress+50;
                Log.i(TAG,"New Significant Move Threshold is set to " + (progress+50));
                Toast.makeText(getBaseContext(),
                        "New Significant Move Threshold is set to"+(progress+50),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void onStart(){
        super.onStart();
        Log.i(TAG,"onStart Triggered.");
        enableAccelerometerListening();
    }

    protected  void onStop(){
        super.onStop();
        Log.i(TAG,"onStop Triggered.");
        disableAccelerometerListening();
    }

    private void enableAccelerometerListening(){
        SensorManager sensorManager =(SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void disableAccelerometerListening(){
        SensorManager sensorManager = (SensorManager)this.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.unregisterListener(sensorEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }


    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            Z_inital = SensorManager.GRAVITY_EARTH;
            CurrentX = x*x;
            CurrentY = y*y;
            CurrentZ = (z-Z_inital)*(z-Z_inital);

            deltaX = CurrentX - LastX;
            deltaY = CurrentY - LastY;
            deltaZ = CurrentZ -LastZ;
            if(deltaX+deltaY+deltaZ<15*SignificantMove) {
                if (deltaX > SignificantMove && deltaX > deltaY && deltaX > deltaZ) {
                    wbvShow.loadUrl("http://google.com");
                    Log.i(TAG, "Significant Move in X direction!");
                    Toast.makeText(getBaseContext(), "Significant Move in X direction!", Toast.LENGTH_SHORT).show();
                }
                if (deltaY > SignificantMove && deltaY > deltaX && deltaY > deltaZ) {
                    wbvShow.loadUrl("http://yahoo.com");
                    Log.i(TAG, "Significant Move in Y direction!");
                    Toast.makeText(getBaseContext(), "Significant Move in Y direction!", Toast.LENGTH_SHORT).show();
                }
                if (deltaZ > SignificantMove && deltaZ > deltaX && deltaZ > deltaY) {
                    wbvShow.loadUrl("http://bing.com");
                    Log.i(TAG, "Significant Move in Z direction!");
                    Toast.makeText(getBaseContext(), "Significant Move in Z direction!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                wbvShow.loadUrl("https://jumpingjaxfitness.files.wordpress.com/2010/07/dizziness.jpg");
            }

        LastX = x*x;
        LastY = y*y;
        LastZ = (z-Z_inital)*(z-Z_inital);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

}