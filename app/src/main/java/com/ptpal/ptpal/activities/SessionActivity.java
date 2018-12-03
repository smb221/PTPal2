package com.ptpal.ptpal.activities;

import android.annotation.TargetApi;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.*;

//package Android.Arduino.Bluetooth;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Date;
import android.util.Log;

import com.ptpal.ptpal.R;
import com.ptpal.ptpal.model.Patient;
import com.ptpal.ptpal.model.Session;
import com.ptpal.ptpal.model.Therapy;
import com.ptpal.ptpal.sql.PTPalDB;

import static android.icu.lang.UCharacter.toUpperCase;

public class SessionActivity extends AppCompatActivity implements View.OnClickListener
{
    private final AppCompatActivity activity = SessionActivity.this;

    private NestedScrollView nestedScrollViewS;

    private TextView textViewExerciseName;
    private TextView textViewDuration;
    private TextView textViewAccelX;
    private TextView textViewAccelY;
    private TextView textViewAccelZ;
    private TextView textViewDistance;
    private TextView textViewMagnitude;
    private TextView textViewSessionOverExtensions;
    private TextView textViewSessionOverExertions;
    private TextView textViewSessionPronations;

    private AppCompatButton appCompatButtonStart;
    private AppCompatButton appCompatButtonEnd;

    private AppCompatTextView appCompatTextViewSessToPat;

    private String patEmail;
    private String patExercise;

    private PTPalDB myDB;

    private Therapy therapy;
    private Patient patient;
    private Session session;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mmDevice;
    private BluetoothSocket mmSocket;
    private OutputStream mmOutputStream;
    private InputStream mmInputStream;
    volatile boolean stopWorker;
    private Thread workerThread;
    private  byte[] readBuffer;
    private int readBufferPosition;
    private int test;
    private String out;
    private int runs;

    private CountDownTimer countDownTimer;
    private long timeLeft;
    private boolean timerRunning;

    private double accelX;
    private double accelY;
    private double accelZ;
    private double distance;
    private double realAccelMag;
    private double initAccelX;
    private double initAccelY;
    private double initAccelZ;
    private double initAccelMag;
    private double initDist;
    private double heightM;
    private double ulnaCm;
    private String change;
    private int overExtentions;
    private int overExertions;
    private int pronations;

    private Map<Double,Double> menOlder;
    private Map<Double,Double> menYounger;
    private Map<Double,Double> womenOlder;
    private Map<Double,Double> womenYounger;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        getSupportActionBar().hide();

        patEmail = getIntent().getExtras().getString("EMAIL");
        patExercise = getIntent().getExtras().getString("EXERCISE");

        pronations = 0;
        overExertions = 0;
        overExtentions = 0;
        change = "000";

        initObjects();
        initViews();
        initListeners();

        timeLeft = (long) therapy.getSessionDuration()*60000;

        menOlder = new HashMap<Double,Double>();
        menOlder.put(1.87, 32.0);
        menOlder.put(1.86, 31.5);
        menOlder.put(1.85, 31.25);
        menOlder.put(1.84, 31.0);
        menOlder.put(1.83, 30.75);
        menOlder.put(1.82, 30.5);
        menOlder.put(1.81, 30.0);
        menOlder.put(1.80, 29.75);
        menOlder.put(1.79, 29.5);
        menOlder.put(1.78, 29.0);
        menOlder.put(1.77, 28.75);
        menOlder.put(1.76, 28.5);
        menOlder.put(1.75, 28.0);
        menOlder.put(1.74, 27.5);
        menOlder.put(1.73, 27.0);
        menOlder.put(1.72, 27.0);
        menOlder.put(1.71, 27.0);
        menOlder.put(1.70, 26.5);
        menOlder.put(1.69, 26.25);
        menOlder.put(1.68, 26.0);
        menOlder.put(1.67, 25.5);
        menOlder.put(1.66, 25.25);
        menOlder.put(1.65, 25.0);
        menOlder.put(1.64, 24.75);
        menOlder.put(1.63, 24.5);
        menOlder.put(1.62, 24.0);
        menOlder.put(1.61, 23.75);
        menOlder.put(1.60, 23.5);
        menOlder.put(1.59, 23.0);
        menOlder.put(1.58, 22.75);
        menOlder.put(1.57, 22.5);
        menOlder.put(1.56, 22.0);
        menOlder.put(1.55, 21.75);
        menOlder.put(1.54, 21.5);
        menOlder.put(1.53, 21.25);
        menOlder.put(1.52, 21.0);
        menOlder.put(1.51, 20.5);
        menOlder.put(1.50, 20.25);
        menOlder.put(1.49, 20.0);
        menOlder.put(1.48, 19.5);
        menOlder.put(1.47, 19.25);
        menOlder.put(1.46, 19.0);
        menOlder.put(1.45, 18.5);

        menYounger = new HashMap<Double,Double>();
        menYounger.put(1.94, 32.0);
        menYounger.put(1.93, 31.5);
        menYounger.put(1.92, 31.25);
        menYounger.put(1.91, 31.0);
        menYounger.put(1.90, 30.75);
        menYounger.put(1.89, 30.5);
        menYounger.put(1.88, 30.25);
        menYounger.put(1.87, 30.0);
        menYounger.put(1.86, 29.75);
        menYounger.put(1.85, 29.5);
        menYounger.put(1.84, 29.0);
        menYounger.put(1.83, 28.75);
        menYounger.put(1.82, 28.5);
        menYounger.put(1.81, 28.25);
        menYounger.put(1.80, 28.0);
        menYounger.put(1.79, 27.5);
        menYounger.put(1.78, 27.0);
        menYounger.put(1.77, 27.0);
        menYounger.put(1.76, 27.0);
        menYounger.put(1.75, 26.5);
        menYounger.put(1.74, 26.25);
        menYounger.put(1.73, 26.0);
        menYounger.put(1.72, 25.75);
        menYounger.put(1.71, 25.5);
        menYounger.put(1.70, 25.25);
        menYounger.put(1.69, 25.0);
        menYounger.put(1.68, 24.75);
        menYounger.put(1.67, 24.5);
        menYounger.put(1.66, 24.0);
        menYounger.put(1.65, 23.75);
        menYounger.put(1.64, 23.5);
        menYounger.put(1.63, 23.25);
        menYounger.put(1.62, 23.0);
        menYounger.put(1.61, 22.75);
        menYounger.put(1.60, 22.5);
        menYounger.put(1.59, 22.25);
        menYounger.put(1.58, 22.0);
        menYounger.put(1.57, 21.5);
        menYounger.put(1.56, 21.25);
        menYounger.put(1.55, 21.0);
        menYounger.put(1.54, 20.75);
        menYounger.put(1.53, 20.5);
        menYounger.put(1.52, 20.25);
        menYounger.put(1.51, 20.0);
        menYounger.put(1.50, 19.75);
        menYounger.put(1.49, 19.5);
        menYounger.put(1.48, 19.0);
        menYounger.put(1.47, 18.75);
        menYounger.put(1.46, 18.5);

        womenOlder = new HashMap<Double,Double>();
        womenOlder.put(1.84, 32.0);
        womenOlder.put(1.83, 31.5);
        womenOlder.put(1.82, 31.25);
        womenOlder.put(1.81, 31.0);
        womenOlder.put(1.80, 30.75);
        womenOlder.put(1.79, 30.5);
        womenOlder.put(1.78, 30.0);
        womenOlder.put(1.77, 29.75);
        womenOlder.put(1.76, 29.5);
        womenOlder.put(1.75, 29.0);
        womenOlder.put(1.74, 29.75);
        womenOlder.put(1.73, 28.5);
        womenOlder.put(1.72, 28.25);
        womenOlder.put(1.71, 28.0);
        womenOlder.put(1.70, 27.0);
        womenOlder.put(1.69, 27.0);
        womenOlder.put(1.68, 27.0);
        womenOlder.put(1.67, 26.75);
        womenOlder.put(1.66, 26.5);
        womenOlder.put(1.65, 26.0);
        womenOlder.put(1.64, 25.75);
        womenOlder.put(1.63, 25.5);
        womenOlder.put(1.62, 25.25);
        womenOlder.put(1.61, 25.0);
        womenOlder.put(1.60, 24.5);
        womenOlder.put(1.59, 24.25);
        womenOlder.put(1.58, 24.0);
        womenOlder.put(1.57, 23.75);
        womenOlder.put(1.56, 23.5);
        womenOlder.put(1.55, 23.0);
        womenOlder.put(1.54, 22.75);
        womenOlder.put(1.53, 22.5);
        womenOlder.put(1.52, 22.0);
        womenOlder.put(1.51, 21.75);
        womenOlder.put(1.50, 21.5);
        womenOlder.put(1.49, 21.25);
        womenOlder.put(1.48, 21.0);
        womenOlder.put(1.47, 20.5);
        womenOlder.put(1.46, 20.25);
        womenOlder.put(1.45, 20.0);
        womenOlder.put(1.44, 19.5);
        womenOlder.put(1.43, 19.25);
        womenOlder.put(1.42, 19.0);
        womenOlder.put(1.41, 18.75);
        womenOlder.put(1.40, 18.5);

        womenYounger = new HashMap<Double,Double>();
        womenYounger.put(1.84, 32.0);
        womenYounger.put(1.83, 31.75);
        womenYounger.put(1.82, 31.5);
        womenYounger.put(1.81, 31.0);
        womenYounger.put(1.80, 30.5);
        womenYounger.put(1.79, 30.0);
        womenYounger.put(1.78, 29.75);
        womenYounger.put(1.77, 29.5);
        womenYounger.put(1.76, 29.0);
        womenYounger.put(1.75, 28.5);
        womenYounger.put(1.74, 28.25);
        womenYounger.put(1.73, 28.0);
        womenYounger.put(1.72, 27.0);
        womenYounger.put(1.71, 27.0);
        womenYounger.put(1.70, 27.0);
        womenYounger.put(1.69, 26.5);
        womenYounger.put(1.68, 26.0);
        womenYounger.put(1.67, 25.75);
        womenYounger.put(1.66, 25.5);
        womenYounger.put(1.65, 25.0);
        womenYounger.put(1.64, 24.75);
        womenYounger.put(1.63, 24.5);
        womenYounger.put(1.62, 24.0);
        womenYounger.put(1.61, 23.5);
        womenYounger.put(1.60, 23.25);
        womenYounger.put(1.59, 23.0);
        womenYounger.put(1.58, 22.5);
        womenYounger.put(1.57, 22.25);
        womenYounger.put(1.56, 22.0);
        womenYounger.put(1.55, 21.5);
        womenYounger.put(1.54, 21.0);
        womenYounger.put(1.53, 20.75);
        womenYounger.put(1.52, 20.5);
        womenYounger.put(1.51, 20.0);
        womenYounger.put(1.50, 19.5);
        womenYounger.put(1.49, 19.25);
        womenYounger.put(1.48, 19.0);
        womenYounger.put(1.47, 18.5);
    }

    private void initObjects()
    {
        myDB = new PTPalDB(activity);
        patient = myDB.getPatient(patEmail);
        therapy = myDB.getSelectedTherapy(patEmail, patExercise);
    }

    private void initViews()
    {
        nestedScrollViewS = (NestedScrollView) findViewById(R.id.nestedScrollViewS);

        textViewExerciseName = (TextView) findViewById(R.id.textViewExerciseName);
        textViewExerciseName.setText(therapy.getExercise());

        textViewDuration = (TextView) findViewById(R.id.textViewDuration);
        String initTime = String.valueOf(therapy.getSessionDuration());
        if(initTime.contains("."))
        {
            initTime.replace(".",":");
        }
        textViewDuration.setText("Time Remaining: " + initTime);

        textViewAccelX = (TextView) findViewById(R.id.textViewAccelX);
        textViewAccelX.setVisibility(View.GONE);

        textViewAccelY = (TextView) findViewById(R.id.textViewAccelY);
        textViewAccelY.setVisibility(View.GONE);

        textViewAccelZ = (TextView) findViewById(R.id.textViewAccelZ);
        textViewAccelZ.setVisibility(View.GONE);

        textViewDistance = (TextView) findViewById(R.id.textViewDistance);
        textViewDistance.setVisibility(View.GONE);

        textViewMagnitude = (TextView) findViewById(R.id.textViewMagnitude);
        textViewMagnitude.setVisibility(View.GONE);

        textViewSessionOverExtensions = (TextView) findViewById(R.id.textViewSessionOverExtensions);
        textViewSessionOverExtensions.setVisibility(View.GONE);

        textViewSessionOverExertions = (TextView) findViewById(R.id.textViewSessionOverExertions);
        textViewSessionOverExertions.setVisibility(View.GONE);

        textViewSessionPronations = (TextView) findViewById(R.id.textViewSessionPronations);
        textViewSessionPronations.setVisibility(View.GONE);

        appCompatButtonStart = (AppCompatButton) findViewById(R.id.appCompatButtonStart);
        appCompatButtonEnd = (AppCompatButton) findViewById(R.id.appCompatButtonEnd);

        appCompatTextViewSessToPat = (AppCompatTextView) findViewById(R.id.appCompatTextViewSessToPat);
    }
    private void initListeners(){
        appCompatButtonStart.setOnClickListener(this);
        appCompatButtonEnd.setOnClickListener(this);
        appCompatTextViewSessToPat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonStart:
                startTimer();
                findBT();
                try {
                    openBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.appCompatButtonEnd:
                stopTimer();
                storeSession();
                try {
                    closeBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.appCompatTextViewSessToPat:
                finish();
                break;
        }
    }

    public void findBT()
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null)
        {
            Snackbar.make(nestedScrollViewS, getString(R.string.failed_find_bt), Snackbar.LENGTH_LONG).show();
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals("RNBT-80D7"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        Snackbar.make(nestedScrollViewS, getString(R.string.success_find_bt), Snackbar.LENGTH_LONG).show();
    }

    void openBT() throws IOException
    {
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        Snackbar.make(nestedScrollViewS, getString(R.string.open_bt), Snackbar.LENGTH_LONG).show();
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    //Log.i("logging", data + "");
                                    handler.post(new Runnable()
                                    {
                                        public void run()
                                        {
                                            runs = 1;
                                            if(test < 30)
                                            {
                                                out = out + data + "\n";
                                                test++;
                                                if(test == 10)
                                                {
                                                    //Log.d("myTag", out);

                                                    String values[] = out.split(":");

                                                    accelX = Double.parseDouble(values[0]);
                                                    accelY = Double.parseDouble(values[1]);
                                                    accelZ = Double.parseDouble(values[2]);
                                                    distance = Double.parseDouble(values[3]);
                                                    realAccelMag = Double.parseDouble(values[4]);
                                                    initAccelX = Double.parseDouble(values[5]);
                                                    initAccelY = Double.parseDouble(values[6]);
                                                    initAccelZ = Double.parseDouble(values[7]);
                                                    initAccelMag = Double.parseDouble(values[8]);
                                                    initDist = Double.parseDouble(values[9]);

                                                    test = 0;
                                                    out = "";
                                                    if(patient.getHeight() > 0)
                                                    {
                                                        heightM = Math.round((patient.getHeight()*2.54/100)*100)/100;

                                                        if(patient.getGender().equals("M"))
                                                        {
                                                            if(patient.getAge() < 65)
                                                            {
                                                                if(heightM <= 1.94 && heightM >= 1.46)
                                                                {
                                                                     ulnaCm = menYounger.get(heightM);
                                                                }
                                                                else if(heightM > 1.94)
                                                                {
                                                                    ulnaCm = 33.0;
                                                                }
                                                                else if(heightM < 1.46)
                                                                {
                                                                    ulnaCm = 18.0;
                                                                }
                                                            }
                                                            else
                                                            {
                                                                if(heightM <= 1.87 && heightM >= 1.45)
                                                                {
                                                                    ulnaCm = menOlder.get(heightM);
                                                                }
                                                                else if(heightM > 1.87)
                                                                {
                                                                    ulnaCm = 32.5;
                                                                }
                                                                else if(heightM < 1.45)
                                                                {
                                                                    ulnaCm = 18.0;
                                                                }
                                                            }
                                                        }
                                                        else if(patient.getGender().equals("F"))
                                                        {
                                                            if(patient.getAge() < 65)
                                                            {
                                                                if(heightM <= 1.84 && heightM >= 1.47)
                                                                {
                                                                    ulnaCm = womenYounger.get(heightM);
                                                                }
                                                                else if(heightM > 1.84)
                                                                {
                                                                    ulnaCm = 32.5;
                                                                }
                                                                else if(heightM < 1.47)
                                                                {
                                                                    ulnaCm = 18.0;
                                                                }
                                                            }
                                                            else
                                                            {
                                                                if(heightM <= 1.84 && heightM >= 1.40)
                                                                {
                                                                    ulnaCm = womenOlder.get(heightM);
                                                                }
                                                                else if(heightM > 1.84)
                                                                {
                                                                    ulnaCm = 32.5;
                                                                }
                                                                else if(heightM < 1.40)
                                                                {
                                                                    ulnaCm = 18.0;
                                                                }
                                                            }
                                                        }
                                                    }

                                                    if(Math.abs(realAccelMag) > (Math.abs(initAccelMag) + 1));
                                                    {
                                                        overExertions++;
                                                    }
                                                    if(Math.abs(accelY) > (Math.abs(initAccelY) + .5))
                                                    {
                                                        pronations++;
                                                    }

                                                    if(patExercise.equals("Horizontal Arm Extension"))
                                                    {
                                                        if(distance < initDist-40)
                                                        {
                                                            overExtentions++;
                                                        }
                                                    }
                                                    else if(patExercise.equals("Vertical Arm Extension"))
                                                    {
                                                       if(distance < initDist + ulnaCm - 40)
                                                       {
                                                           overExtentions++;
                                                           try {
                                                               sendData();
                                                           } catch (IOException e) {
                                                               e.printStackTrace();
                                                           }
                                                       }
                                                    }

                                                    textViewAccelX.setVisibility(View.VISIBLE);
                                                    textViewAccelX.setText("X Acceleration: " + String.valueOf(accelX));
                                                    textViewAccelY.setVisibility(View.VISIBLE);
                                                    textViewAccelY.setText("Y Acceleration: " + String.valueOf(accelY));
                                                    textViewAccelZ.setVisibility(View.VISIBLE);
                                                    textViewAccelZ.setText("Z Acceleration: " + String.valueOf(accelZ));
                                                    textViewDistance.setVisibility(View.VISIBLE);
                                                    textViewDistance.setText("Distane: " + String.valueOf(distance));
                                                    textViewMagnitude.setVisibility(View.VISIBLE);
                                                    textViewMagnitude.setText("Magnitude: " + String.valueOf(realAccelMag));
                                                    textViewSessionOverExertions.setVisibility(View.VISIBLE);
                                                    textViewSessionOverExertions.setText("Over Exertions: " + String.valueOf(overExertions));
                                                    textViewSessionOverExtensions.setVisibility(View.VISIBLE);
                                                    textViewSessionOverExtensions.setText("Over Extentions: " + String.valueOf(overExtentions));
                                                    textViewSessionPronations.setVisibility(View.VISIBLE);
                                                    textViewSessionPronations.setText("Pronations: " + String.valueOf(pronations));
                                                }
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    public void sendData() throws IOException
    {
        change = "555";
        mmOutputStream.write(change.getBytes());
        change = "000";
    }
    public void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();

        Snackbar.make(nestedScrollViewS, getString(R.string.close_bt), Snackbar.LENGTH_LONG).show();
    }

    public void startTimer()
    {
        countDownTimer = new CountDownTimer(timeLeft, 1000)
        {
            @Override
            public void onTick(long l) {
                timeLeft = l;
                updateTimer();
            }

            @Override
            public void onFinish() {

            }
        }.start();

        timerRunning = true;
    }

    public void stopTimer()
    {
        countDownTimer.cancel();
        timerRunning = false;
    }
    public void updateTimer()
    {
        int minutes = (int) timeLeft/60000;
        int seconds = (int) timeLeft%60000/1000;
        String timeLeftText = minutes + ":";
        if(seconds < 10)
        {
            timeLeftText+="0";
        }
        timeLeftText+=seconds;

        textViewDuration.setText("Time Remaining: " + timeLeftText);
    }
    public void storeSession()
    {
        session.setPatientEmail(patEmail);
        session.setExercise(patExercise);
        double sessionTime = therapy.getSessionDuration() - timeLeft/60000;
        session.setDuration(sessionTime);
        session.setPronations(pronations);
        session.setOverExertions(overExertions);
        session.setOverExtentions(overExtentions);
        Date now = new Date();
        String nowString = new SimpleDateFormat("yyyy-MM-dd").format(now);
        session.setCreatedDate(nowString);

        myDB.insertSession(session);
    }
}
