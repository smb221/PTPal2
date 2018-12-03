package com.ptpal.ptpal.activities;

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

import com.ptpal.ptpal.R;
import com.ptpal.ptpal.helper.InputValidation;
import com.ptpal.ptpal.model.Patient;
import com.ptpal.ptpal.sql.PTPalDB;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollViewR;

    private TextInputLayout textInputLayoutFirstName;
    private TextInputLayout textInputLayoutLastName;
    private TextInputLayout textInputLayoutEmailR;
    private TextInputLayout textInputLayoutGender;
    private TextInputLayout textInputLayoutHeight;
    private TextInputLayout textInputLayoutAge;
    private TextInputLayout textInputLayoutPasswordR;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextFirstName;
    private TextInputEditText textInputEditTextLastName;
    private TextInputEditText textInputEditTextEmailR;
    private TextInputEditText textInputEditTextGender;
    private TextInputEditText textInputEditTextHeight;
    private TextInputEditText textInputEditTextAge;
    private TextInputEditText textInputEditTextPasswordR;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private PTPalDB PTPalDB;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    private void initViews(){
        nestedScrollViewR = (NestedScrollView) findViewById(R.id.nestedScrollViewR);

        textInputLayoutFirstName = (TextInputLayout) findViewById(R.id.textInputLayoutFirstName);
        textInputLayoutLastName = (TextInputLayout) findViewById(R.id.textInputLayoutLastName);
        textInputLayoutEmailR = (TextInputLayout) findViewById(R.id.textInputLayoutEmailR);
        textInputLayoutGender = (TextInputLayout) findViewById(R.id.textInputLayoutGender);
        textInputLayoutHeight = (TextInputLayout) findViewById(R.id.textInputLayoutHeight);
        textInputLayoutAge = (TextInputLayout) findViewById(R.id.textInputLayoutAge);
        textInputLayoutPasswordR = (TextInputLayout) findViewById(R.id.textInputLayoutPasswordR);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextFirstName = (TextInputEditText) findViewById(R.id.textInputEditTextFirstName);
        textInputEditTextLastName = (TextInputEditText) findViewById(R.id.textInputEditTextLastName);
        textInputEditTextEmailR = (TextInputEditText) findViewById(R.id.textInputEditTextEmailR);
        textInputEditTextGender = (TextInputEditText) findViewById(R.id.textInputEditTextGender);
        textInputEditTextHeight = (TextInputEditText) findViewById(R.id.textInputEditTextHeight);
        textInputEditTextAge = (TextInputEditText) findViewById(R.id.textInputEditTextAge);
        textInputEditTextPasswordR = (TextInputEditText) findViewById(R.id.textInputEditTextPasswordR);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
    }

    private void initListeners()
    {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    private void initObjects()
    {
        inputValidation = new InputValidation(activity);
        PTPalDB = new PTPalDB(activity);
        patient = new Patient();
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void postDataToSQLite()
    {
        if(!inputValidation.isInputEditTextFilled(textInputEditTextFirstName, textInputLayoutFirstName, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextLastName, textInputLayoutLastName, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextEmailR, textInputLayoutEmailR, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextGender, textInputLayoutGender, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextHeight, textInputLayoutHeight, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextAge, textInputLayoutAge, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextEmail(textInputEditTextEmailR, textInputLayoutEmailR, getString(R.string.error_message_email)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextPasswordR, textInputLayoutPasswordR, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextFilled(textInputEditTextConfirmPassword, textInputLayoutConfirmPassword, getString(R.string.error_message)))
        {
            return;
        }
        if(!inputValidation.isInputEditTextMatches(textInputEditTextPasswordR, textInputEditTextConfirmPassword, textInputLayoutConfirmPassword, getString(R.string.unmatched_pass)))
        {
            return;
        }

        if(!PTPalDB.checkEmail(textInputEditTextEmailR.getText().toString().trim()))
        {
           patient.setFirstName(textInputEditTextFirstName.getText().toString().trim());
           patient.setLastName(textInputEditTextLastName.getText().toString().trim());
           patient.setEmail(textInputEditTextEmailR.getText().toString().trim());
           patient.setGender(textInputEditTextGender.getText().toString().trim());

           int height = Integer.parseInt(textInputEditTextHeight.getText().toString().trim());
           patient.setHeight(height);

           int age = Integer.parseInt(textInputEditTextAge.getText().toString().trim());
           patient.setAge(age);
           patient.setPassword(textInputEditTextPasswordR.getText().toString().trim());

           PTPalDB.insertPatient(patient);

           Snackbar.make(nestedScrollViewR, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
           emptyInputEditText();
        }
        else
        {
            Snackbar.make(nestedScrollViewR, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText()
    {
        textInputEditTextFirstName.setText(null);
        textInputEditTextLastName.setText(null);
        textInputEditTextEmailR.setText(null);
        textInputEditTextGender.setText(null);
        textInputEditTextHeight.setText(null);
        textInputEditTextPasswordR.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}
