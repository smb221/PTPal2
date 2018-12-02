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
import java.util.Set;
import java.util.UUID;
import android.util.Log;

import com.ptpal.ptpal.R;
import com.ptpal.ptpal.model.Patient;
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
    private double demispan;
    private int overExtensions;
    private int overExertions;
    private int pronations;



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

        initObjects();
        initViews();
        initListeners();

        patient = myDB.getPatient(patEmail);

        therapy = myDB.getSelectedTherapy(patEmail, patExercise);
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
        textViewDuration.setText(String.valueOf(therapy.getSessionDuration()) + " Seconds");

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
                findBT();
                try {
                    openBT();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.appCompatButtonEnd:
                endConnection();
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

                                                    textViewAccelX.setVisibility(View.VISIBLE);
                                                    textViewAccelX.setText("X Acceleration: " + String.valueOf(accelX));
                                                    textViewAccelY.setVisibility(View.VISIBLE);
                                                    textViewAccelY.setText("Y Acceleration: " + String.valueOf(accelY));
                                                    textViewAccelZ.setVisibility(View.VISIBLE);
                                                    textViewAccelZ.setText("Z Acceleration: " + String.valueOf(accelZ));
                                                    textViewDistance.setVisibility(View.VISIBLE);
                                                    textViewDistance.setText("Distane: " + String.valueOf(distance));
                                                    textViewMagnitude.setVisibility(View.VISIBLE);
                                                    textViewAccelX.setText("Magnitude: " + String.valueOf(realAccelMag));

                                                    test = 0;
                                                    out = "";
                                                    if(patient.getHeight() > 0)
                                                    {
                                                        if(patient.getGender().equals("M"))
                                                        {
                                                          
                                                        }
                                                        else if(patient.getGender().equals("F"))
                                                        {
                                                         
                                                        }
                                                    }
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
}
