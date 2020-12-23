package com.example.grybas.velykom_geras;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Gabrielius on 2018.03.18.
 */

public class PlayScreen extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Integer[] images = {
            R.drawable.egg,

    };

    Integer[] atss = {
            R.drawable.egg,
    };

    ImageView egg;
    ImageView reward;
    Button kitas;
    TextView message;
    TextView myText;
    TextView end;
    Button grizti;

    private SensorManager sm;

    private float acelVal;
    private float acelLast;
    private float shake;


   public int refreshmax() {
        int maxRanged = SettingsScreen.getNum(this);
        return  maxRanged;
    }

    int maxRange=48;
    int i = 0;
    int ind = 1;
    int j = 1;
    int[] rezultatai = new int[maxRange];
    List<Integer> solution = new ArrayList<>();

    Random t = new Random();
    //int t1 = t.nextInt(14-7) + 7;

    private HandlerThread mSensorThread;
    private Handler mSensorHandler;


    class SensorThread extends Thread {
        SensorManager mSensorManager;

        public void run() {
            Looper.prepare();
            Log.d("RunTag", Thread.currentThread().getName());
            Handler handler = new Handler();
            mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            MySensorListener ms1 = new MySensorListener();
            mSensorManager.registerListener(ms1, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL, handler);
            acelVal = SensorManager.GRAVITY_EARTH;
            acelLast = SensorManager.GRAVITY_EARTH;
            shake = 0.00f;
            Looper.loop();
        }

        private class MySensorListener implements SensorEventListener {

            public void onSensorChanged(SensorEvent sensorEvent) {

                grizti = (Button) findViewById(R.id.grizti);

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                acelLast = acelVal;
                acelVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = acelVal - acelLast;
                shake = shake * 0.9f + delta;


                final TextView myText = (TextView) findViewById(R.id.numbergen);

                Random r = new Random();
                int i1 = r.nextInt(20 - 12) + 12;
                final Random tr = new Random();

                Random t = new Random();
                int t1 = t.nextInt(16 - 7) + 7;

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (shake > i1) {
                    j++;
                    v.vibrate(50);
                    if (j > t1) {
                        v.cancel();
                        String myString = String.valueOf(solution.get(i));
                        myText.setText(myString);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Animation animation = AnimationUtils.loadAnimation(PlayScreen.this, R.anim.fadein);
                                Animation animation1 = AnimationUtils.loadAnimation(PlayScreen.this, R.anim.fadeout);
                                egg.startAnimation(animation1);
                                egg.setVisibility(View.INVISIBLE);
                                message.setVisibility(View.INVISIBLE);
                                myText.setVisibility(View.VISIBLE);
                                kitas.setEnabled(true);
                                kitas.setVisibility(View.VISIBLE);
                                grizti.setEnabled(false);
                                if (i+1==maxRange){
                                    kitas.setEnabled(false);
                                    kitas.setVisibility(View.INVISIBLE);
                                    end = (TextView) findViewById(R.id.end);
                                    end.setVisibility(View.VISIBLE);
                                    grizti.setVisibility(View.VISIBLE);
                                    grizti.setEnabled(true);
                                }
                            }
                        });

                    }

                }
            }

            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        }
    }

    class showText extends Thread {
        final TextView liko = (TextView) findViewById(R.id.likutis);

        @Override
        public void run() {
            String mySt = String.valueOf(ind+1) + "/" + String.valueOf(maxRange);
            liko.setText(mySt);
            ++ind;
            ++i;
            j = 1;
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playscreen);

        //refreshmax();
        maxRange = refreshmax();

        egg = (ImageView) findViewById(R.id.egg);

        TextView liko = (TextView) findViewById(R.id.likutis);
        String mySt = 1 + "/" + String.valueOf(maxRange);
        liko.setText(mySt);

        Thread thread = new Thread(new SensorThread());
        thread.start();

        View deoorView = getWindow().getDecorView();
        deoorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        for (int i = 1; i <= maxRange; i++) {
            solution.add(i);
        }
        Collections.shuffle(solution);

        kitas = (Button) findViewById(R.id.kitas);
        egg = (ImageView) findViewById(R.id.egg);
        message = (TextView) findViewById(R.id.message);
        myText = (TextView) findViewById(R.id.numbergen);
        final Random k = new Random();
        final Random tr = new Random();
        kitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread mythread = new Thread(new showText());
                mythread.start();
              //  egg.setImageResource(images[k.nextInt(images.length)]);
                int color= ((int)(Math.random()*16777215)) | (0xFF << 24);
                egg.setColorFilter(color);
                egg.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(PlayScreen.this, R.anim.fadein);
                Animation animation1 = AnimationUtils.loadAnimation(PlayScreen.this, R.anim.fadeout1);
                egg.startAnimation(animation);
                message.setVisibility(View.VISIBLE);
                myText.setVisibility(View.INVISIBLE);
                kitas.setVisibility(View.INVISIBLE);
                kitas.setEnabled(false);
            }
        });

        grizti = (Button) findViewById(R.id.grizti);
        grizti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(PlayScreen.this, MainActivity.class);
                startActivity(intent2);
            }
        });

    }


}





