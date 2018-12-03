package com.ptpal.ptpal.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.ptpal.ptpal.model.ProfilePicture;
import com.ptpal.ptpal.sql.PTPalDB;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener
{
    private final AppCompatActivity activity = EditProfileActivity.this;

    private NestedScrollView nestedScrollViewE;

    private TextInputLayout textInputLayoutFirstNameE;
    private TextInputLayout textInputLayoutLastNameE;
    private TextInputLayout textInputLayoutEmailE;
    private TextInputLayout textInputLayoutGenderE;
    private TextInputLayout textInputLayoutHeightE;
    private TextInputLayout textInputLayoutAgeE;

    private TextInputEditText textInputEditTextFirstNameE;
    private TextInputEditText textInputEditTextLastNameE;
    private TextInputEditText textInputEditTextEmailE;
    private TextInputEditText textInputEditTextGenderE;
    private TextInputEditText textInputEditTextHeightE;
    private TextInputEditText textInputEditTextAgeE;


    private AppCompatButton appCompatButtonSaveChanges;
    private AppCompatButton appCompatButtonUpdatePicture;
    private AppCompatTextView appCompatTextViewProfToPatLink;

    private InputValidation inputValidation;
    private PTPalDB PTPalDB;
    private Patient patient;
    private ProfilePicture profPat;

    private String patEmail;

    public static final int IMAGE_GALLERY_REQUEST = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        getSupportActionBar().hide();
        patEmail = getIntent().getStringExtra("EMAIL");

        initObjects();
        initViews();
        initListeners();
    }
    private void initObjects()
    {
        inputValidation = new InputValidation(activity);
        PTPalDB = new PTPalDB(activity);
        patient = PTPalDB.getPatient(patEmail);
        profPat = PTPalDB.getPP(patEmail);
    }
    private void initViews(){
        nestedScrollViewE = (NestedScrollView) findViewById(R.id.nestedScrollViewE);

        textInputLayoutFirstNameE = (TextInputLayout) findViewById(R.id.textInputLayoutFirstNameE);
        textInputLayoutLastNameE = (TextInputLayout) findViewById(R.id.textInputLayoutLastNameE);
        textInputLayoutEmailE = (TextInputLayout) findViewById(R.id.textInputLayoutEmailE);
        textInputLayoutGenderE = (TextInputLayout) findViewById(R.id.textInputLayoutGenderE);
        textInputLayoutHeightE = (TextInputLayout) findViewById(R.id.textInputLayoutHeightE);

        textInputEditTextFirstNameE = (TextInputEditText) findViewById(R.id.textInputEditTextFirstNameE);
        textInputEditTextFirstNameE.setText(patient.getFirstName());

        textInputEditTextLastNameE = (TextInputEditText) findViewById(R.id.textInputEditTextLastNameE);
        textInputEditTextLastNameE.setText(patient.getLastName());

        textInputEditTextEmailE = (TextInputEditText) findViewById(R.id.textInputEditTextEmailE);
        textInputEditTextEmailE.setText(patient.getEmail());

        textInputEditTextGenderE = (TextInputEditText) findViewById(R.id.textInputEditTextGenderE);
        textInputEditTextGenderE.setText(patient.getGender());

        textInputEditTextHeightE = (TextInputEditText) findViewById(R.id.textInputEditTextHeightE);
        textInputEditTextHeightE.setText(String.valueOf(patient.getHeight()));

        textInputEditTextAgeE = (TextInputEditText) findViewById(R.id.textInputEditTextAgeE);
        textInputEditTextAgeE.setText(String.valueOf(patient.getAge()));

        appCompatButtonSaveChanges = (AppCompatButton) findViewById(R.id.appCompatButtonSaveChanges);
        appCompatButtonUpdatePicture = (AppCompatButton) findViewById(R.id.appCompatButtonUpdatePicture);

        appCompatTextViewProfToPatLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewProfToPatLink);
    }

    private void initListeners()
    {
        appCompatButtonSaveChanges.setOnClickListener(this);
        appCompatButtonUpdatePicture.setOnClickListener(this);
        appCompatTextViewProfToPatLink.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.appCompatButtonUpdatePicture:
                Intent pictureIntent = new Intent(Intent.ACTION_PICK);
                pictureIntent.setType("image/*");
                startActivityForResult(pictureIntent, IMAGE_GALLERY_REQUEST);
                break;
            case R.id.appCompatButtonSaveChanges:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewProfToPatLink:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("EMAIL", patient.getEmail());
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(resultCode == RESULT_OK)
        {
            if(requestCode == IMAGE_GALLERY_REQUEST) {
                Uri selectedImage = data.getData();
                System.out.println("LOOK" + data.getData());
                try {
                    InputStream inputstream = getContentResolver().openInputStream(selectedImage);
                    Bitmap image = BitmapFactory.decodeStream(inputstream);
                    System.out.println("LOOK" + image);
                    byte[] imageBytes = getBytes(image);
                    profPat.setData(imageBytes);
                    profPat.setPatientEmail(patEmail);
                    System.out.println("LOOK" + profPat.getData());
                    Boolean check = PTPalDB.updatePP(profPat);

                    if(check)
                    {
                        Snackbar.make(nestedScrollViewE, getString(R.string.success_pic), Snackbar.LENGTH_LONG).show();
                    }
                    else
                    {
                        Snackbar.make(nestedScrollViewE, getString(R.string.fail_pic), Snackbar.LENGTH_LONG).show();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void postDataToSQLite()
    {
        if(!inputValidation.isInputEditTextEmail(textInputEditTextEmailE, textInputLayoutEmailE, getString(R.string.error_message_email)))
        {
            return;
        }

        patient.setFirstName(textInputEditTextFirstNameE.getText().toString().trim());
        patient.setLastName(textInputEditTextLastNameE.getText().toString().trim());
        patient.setEmail(textInputEditTextEmailE.getText().toString().trim());
        patient.setGender(textInputEditTextGenderE.getText().toString().trim());

        int height = Integer.parseInt(textInputEditTextHeightE.getText().toString().trim());
        patient.setHeight(height);

        int age = Integer.parseInt(textInputEditTextAgeE.getText().toString().trim());
        patient.setAge(age);

        if(PTPalDB.updatePatient(patient, patEmail))
        {
            Snackbar.make(nestedScrollViewE, getString(R.string.update_success), Snackbar.LENGTH_LONG).show();
            updateInputEditText();
        }
        else
        {
            Snackbar.make(nestedScrollViewE, getString(R.string.error_update_patient), Snackbar.LENGTH_LONG).show();
        }
    }
    public void updateInputEditText()
    {
        profPat = PTPalDB.getPP(patEmail);
        textInputEditTextFirstNameE.setText(patient.getFirstName());
        textInputEditTextLastNameE.setText(patient.getLastName());
        textInputEditTextEmailE.setText(patient.getEmail());
        textInputEditTextGenderE.setText(patient.getGender());
        textInputEditTextHeightE.setText(String.valueOf(patient.getHeight()));
        textInputEditTextAgeE.setText(String.valueOf(patient.getAge()));
    }

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
}
