package com.ptpal.ptpal.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ptpal.ptpal.R;
import com.ptpal.ptpal.helper.InputValidation;
import com.ptpal.ptpal.model.Therapy;
import com.ptpal.ptpal.sql.PTPalDB;

public class TherapyActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = TherapyActivity.this;

    private NestedScrollView nestedScrollViewT;

    private TextInputLayout textInputLayoutTherapist;
    private TextInputLayout textInputLayoutSD;
    private TextInputLayout textInputLayoutSPD;
    private TextInputLayout textInputLayoutDPW;
    private TextInputLayout textInputLayoutTW;

    private TextInputEditText textInputEditTextTherapist;
    private TextInputEditText textInputEditTextSD;
    private TextInputEditText textInputEditTextSPD;
    private TextInputEditText textInputEditTextDPW;
    private TextInputEditText textInputEditTextTW;

    private Spinner mySpinner;
    private AppCompatButton appCompatButtonAddTherapy;

    private AppCompatTextView appCompatTextViewPatientLink;

    private InputValidation inputValidation;
    private PTPalDB PTPalDB;
    private Therapy therapy;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapy);
        getSupportActionBar().hide();

        email = getIntent().getStringExtra("EMAIL");
        initViews();
        initListeners();
        initObjects();
    }

    private void initViews() {
        nestedScrollViewT = (NestedScrollView) findViewById(R.id.nestedScrollViewT);

        textInputLayoutTherapist = (TextInputLayout) findViewById(R.id.textInputLayoutTherapist);
        textInputLayoutSD = (TextInputLayout) findViewById(R.id.textInputLayoutSD);
        textInputLayoutSPD = (TextInputLayout) findViewById(R.id.textInputLayoutSPD);
        textInputLayoutDPW = (TextInputLayout) findViewById(R.id.textInputLayoutDPW);
        textInputLayoutTW = (TextInputLayout) findViewById(R.id.textInputLayoutTW);

        textInputEditTextTherapist = (TextInputEditText) findViewById(R.id.textInputEditTextTherapist);
        textInputEditTextSD = (TextInputEditText) findViewById(R.id.textInputEditTextSD);
        textInputEditTextSPD = (TextInputEditText) findViewById(R.id.textInputEditTextSPD);
        textInputEditTextDPW = (TextInputEditText) findViewById(R.id.textInputEditTextDPW);
        textInputEditTextTW = (TextInputEditText) findViewById(R.id.textInputEditTextTW);

        appCompatButtonAddTherapy = (AppCompatButton) findViewById(R.id.appCompatButtonAddTherapy);

        appCompatTextViewPatientLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewPatientLink);

        mySpinner = (Spinner) findViewById(R.id.exerciseSpinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(TherapyActivity.this, R.layout.spinner_item, getResources().getStringArray(R.array.exercises));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
    }

    private void initListeners() {
        appCompatButtonAddTherapy.setOnClickListener(this);
        appCompatTextViewPatientLink.setOnClickListener(this);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        PTPalDB = new PTPalDB(activity);
        therapy = new Therapy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonAddTherapy:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewPatientLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextTherapist, textInputLayoutTherapist, getString(R.string.error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextSD, textInputLayoutSD, getString(R.string.error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextSPD, textInputLayoutSPD, getString(R.string.error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextDPW, textInputLayoutDPW, getString(R.string.error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextTW, textInputLayoutTW, getString(R.string.error_message))) {
            return;
        }
        therapy.setTherapistEmail(textInputEditTextTherapist.getText().toString().trim());
        therapy.setPatientEmail(email);
        therapy.setExercise(mySpinner.getSelectedItem().toString());
        int sd = Integer.parseInt(textInputEditTextSD.getText().toString().trim());
        therapy.setSessionDuration(sd);

        int spd = Integer.parseInt(textInputEditTextSPD.getText().toString().trim());
        therapy.setSessionsPerDay(spd);

        int dpw = Integer.parseInt(textInputEditTextDPW.getText().toString().trim());
        therapy.setDaysPerWeek(dpw);

        int tw = Integer.parseInt(textInputEditTextTW.getText().toString().trim());
        therapy.setTotalWeeks(tw);


        PTPalDB.insertTherapy(therapy);

        Snackbar.make(nestedScrollViewT, getString(R.string.success_therapy_message), Snackbar.LENGTH_LONG).show();
        emptyInputEditText();
    }

    private void emptyInputEditText() {
        textInputEditTextTherapist.setText(null);
        textInputEditTextSD.setText(null);
        textInputEditTextSPD.setText(null);
        textInputEditTextDPW.setText(null);
        textInputEditTextTW.setText(null);
    }
}

