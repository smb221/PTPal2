package com.ptpal.ptpal.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;

import com.ptpal.ptpal.model.Patient;
import com.ptpal.ptpal.model.ProfilePicture;
import com.ptpal.ptpal.model.Session;
import com.ptpal.ptpal.model.Therapy;

import java.util.ArrayList;
import android.graphics.Bitmap;

public class PTPalDB extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "PTPalDB.db";

    public static final String TBL_PATIENT = "Patient";
    public static final String PATIENT_ID = "Patient_ID";
    public static final String PATIENT_FN = "First_Name";
    public static final String PATIENT_LN = "Last_Name";
    public static final String PATIENT_EMAIL = "Email";
    public static final String PATIENT_PASSWORD = "Password";
    public static final String PATIENT_HEIGHT = "Height";
    public static final String PATIENT_AGE = "Age";
    public static final String PATIENT_GENDER = "Gender";

    public static final String TBL_THERAPIST = "Therapist";
    public static final String THERAPIST_ID = "Therapist_ID";
    public static final String THERAPIST_FN = "First_Name";
    public static final String THERAPIST_LN = "Last_Name";
    public static final String THERAPIST_EMAIL = "Email";
    public static final String THERAPIST_PASSWORD = "Password";
    public static final String THERAPIST_CREATED_DATE = "Created Date";

    public static final String TBL_COMPLIANCE = "Compliance";
    public static final String COMPLIANCE_TID = "Therapy_ID";
    public static final String COMPLIANCE_PID = "Patient_ID";
    public static final String COMPLIANCE_EID = "Exercise_ID";
    public static final String COMPLIANCE_DSR = "Daily_Sessions_Remaining";
    public static final String COMPLIANCE_TDC = "Total_Days_Completed";
    public static final String COMPLIANCE_CDC = "Consecutive_Days_Completed";
    public static final String COMPLIANCE_SD = "Start_Date";
    public static final String COMPLIANCE_ED = "End_Date";

    public static final String TBL_THERAPY = "Therapy";
    public static final String THERAPY_ID = "Therapy_ID";
    public static final String THERAPY_THERAPIST_EMAIL = "Therapist_Email";
    public static final String THERAPY_PATIENT_EMAIL = "PatientEmail";
    public static final String THERAPY_EXERCISE = "Exercise";
    public static final String THERAPY_SPD = "Sessions_Per_Day";
    public static final String THERAPY_DURATION = "Session_Duration";
    public static final String THERAPY_DPW = "Days_Per_Week";
    public static final String THERAPY_TW = "Total_Weeks";

    public static final String TBL_SESSION = "Session";
    public static final String SESSION_ID = "Session_ID";
    public static final String SESSION_PATIENT_EMAIL = "Patient_ID";
    public static final String SESSION_EXERCISE = "Exercise";
    public static final String SESSION_DURATION = "Duration";
    public static final String SESSION_OEXTENTIONS = "Over_Extensions";
    public static final String SESSION_OEXERTIONS = "Over_Exertions";
    public static final String SESSION_PRONATIONS = "Pronations";
    public static final String SESSION_DATE = "Session_Date";

    public static final String TBL_EXERCISES = "Exercises";
    public static final String EXERCISES_ID = "Exercise_ID";
    public static final String EXERCISES_DESCRIPTION = "Description";

    public static final String TBL_TPL = "Therapist_Patient_List";
    public static final String TPL_THERAPIST_ID = "Therapist_ID";
    public static final String TPL_PATIENT_ID = "PATIENT_ID";

    public static final String TBL_PP = "Profile_Pictures";
    public static final String PP_PATIENT_EMAIL = "Patient_Email";
    public static final String PP_DATA = "Data";

    SQLiteDatabase db;

    public PTPalDB(Context context)
    {
        super(context, DATABASE_NAME, null, 8);
        this.db = db;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + TBL_PATIENT + " (" +
                PATIENT_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                PATIENT_FN + " TEXT," +
                PATIENT_LN + " TEXT," +
                PATIENT_EMAIL + " TEXT UNIQUE," +
                PATIENT_PASSWORD + " TEXT," +
                PATIENT_HEIGHT + " INTEGER," +
                PATIENT_AGE + " INTEGER," +
                PATIENT_GENDER + " TEXT);");

        db.execSQL("create table " + TBL_THERAPIST + " (" +
                THERAPIST_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                THERAPIST_FN + " TEXT," +
                THERAPIST_LN + " TEXT," +
                THERAPIST_EMAIL + " TEXT UNIQUE," +
                THERAPIST_PASSWORD + " TEXT," +
                THERAPIST_CREATED_DATE + " ,TEXT);");

        db.execSQL("create table " + TBL_TPL + " (" +
                TPL_THERAPIST_ID + " INTEGER PRIMARY KEY," +
                TPL_PATIENT_ID + " INTEGER);");

        db.execSQL("create table " + TBL_COMPLIANCE + " (" +
                COMPLIANCE_TID + " INTEGER PRIMARY KEY," +
                COMPLIANCE_PID + " INTEGER," +
                COMPLIANCE_EID + " INTEGER," +
                COMPLIANCE_DSR + " INTEGER," +
                COMPLIANCE_TDC + " INTEGER," +
                COMPLIANCE_CDC + " INTEGER," +
                COMPLIANCE_SD + " TEXT," +
                COMPLIANCE_ED + " TEXT);");

        db.execSQL("create table " + TBL_THERAPY + " (" +
                THERAPY_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                THERAPY_THERAPIST_EMAIL + " String," +
                THERAPY_PATIENT_EMAIL + " String," +
                THERAPY_EXERCISE + " String UNIQUE," +
                THERAPY_DURATION + " DOUBLE," +
                THERAPY_SPD + " INTEGER," +
                THERAPY_DPW + " INTEGER," +
                THERAPY_TW + " INTEGER);");

        db.execSQL("create table " + TBL_SESSION + " (" +
                SESSION_ID + " INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT," +
                SESSION_PATIENT_EMAIL + " TEXT," +
                SESSION_EXERCISE + " TEXT," +
                SESSION_DURATION + " DOUBLE," +
                SESSION_OEXTENTIONS + " TEXT," +
                SESSION_OEXERTIONS + " INTEGER," +
                SESSION_PRONATIONS + " INTEGER," +
                SESSION_DATE + " TEXT);");

        db.execSQL("create table " + TBL_PP + " (" +
                PP_PATIENT_EMAIL + " TEXT UNIQUE PRIMARY KEY," +
                PP_DATA + " BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PATIENT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_THERAPIST);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMPLIANCE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_THERAPY);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_SESSION);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TPL);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PP);
        onCreate(db);
    }

    public boolean insertPatient(Patient patient)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PATIENT_FN, patient.getFirstName());
        values.put(PATIENT_LN, patient.getLastName());
        values.put(PATIENT_EMAIL, patient.getEmail());
        values.put(PATIENT_GENDER, patient.getGender());
        values.put(PATIENT_PASSWORD, patient.getPassword());
        values.put(PATIENT_HEIGHT, patient.getHeight());
        values.put(PATIENT_AGE, patient.getHeight());
        long result = db.insert(TBL_PATIENT, null, values);
        if(result == -1)
        {
            db.close();
            return false;
        }
        else{
            db.close();
            return true;
        }
    }

    public boolean insertTherapist(String FirstName, String LastName, String Email, String Password, String CreatedDate)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(THERAPIST_FN, FirstName);
        values.put(THERAPIST_LN, LastName);
        values.put(THERAPIST_EMAIL, Email);
        values.put(THERAPIST_PASSWORD, Password);
        values.put(THERAPIST_CREATED_DATE, CreatedDate);
        long result = db.insert(TBL_THERAPIST, null, values);
        if(result == -1)
        {
            db.close();
            return false;
        }
        else {
            db.close();
            return true;
        }
    }

    public boolean insertTherapy(Therapy therapy)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(THERAPY_THERAPIST_EMAIL, therapy.getTherapistEmail());
        values.put(THERAPY_PATIENT_EMAIL, therapy.getPatientEmail());
        values.put(THERAPY_EXERCISE, therapy.getExercise());
        values.put(THERAPY_DURATION, therapy.getSessionDuration());
        values.put(THERAPY_SPD, therapy.getSessionsPerDay());
        values.put(THERAPY_DPW, therapy.getDaysPerWeek());
        values.put(THERAPY_TW, therapy.getTotalWeeks());
        long result = db.insert(TBL_THERAPY, null, values);
        if(result == -1)
        {
            db.close();
            return false;
        }
        else{
            db.close();
            return true;
        }
    }

    public boolean insertSession(Session session){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SESSION_PATIENT_EMAIL, session.getPatientEmail());
        values.put(SESSION_EXERCISE, session.getExercise());
        values.put(SESSION_DURATION, session.getDuration());
        values.put(SESSION_PRONATIONS, session.getPronations());
        values.put(SESSION_OEXERTIONS, session.getOverExertions());
        values.put(SESSION_OEXTENTIONS, session.getOverExtentions());
        values.put(SESSION_DATE, session.getCreatedDate());
        long result = db.insert(TBL_SESSION, null, values);
        if(result == -1)
        {
            db.close();
            return false;
        }
        else{
            db.close();
            return true;
        }
    }

    public boolean insertCompliance(int therapyID, int patientID, int exerciseID, int dsc, int tdc , int cdc, String sd, String ed, String CreatedDate)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COMPLIANCE_TID, therapyID);
        values.put(COMPLIANCE_PID, patientID);
        values.put(COMPLIANCE_EID, exerciseID);
        values.put(COMPLIANCE_DSR, dsc);
        values.put(COMPLIANCE_TDC, tdc);
        values.put(COMPLIANCE_CDC, cdc);
        values.put(COMPLIANCE_SD, sd);
        values.put(COMPLIANCE_ED, ed);
        long result = db.insert(TBL_COMPLIANCE, null, values);
        if(result == -1)
        {
            db.close();
            return false;
        }
        else{
            db.close();
            return true;
        }
    }

    public boolean insertTPL(int therapistID, int patientID)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TPL_THERAPIST_ID, therapistID);
        values.put(TPL_PATIENT_ID, patientID);
        long result = db.insert(TBL_TPL, null, values);
        if(result == -1)
        {
            db.close();
            return false;
        }
        else{
            db.close();
            return true;
        }
    }

    public boolean insertExercises(int exerciseID, String Description) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EXERCISES_ID, exerciseID);
        values.put(EXERCISES_DESCRIPTION, Description);
        long result = db.insert(TBL_EXERCISES, null, values);
        if (result == -1)
        {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public boolean checkPatient(String email, String pass)
    {
        String[] columns = {
                PATIENT_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT * FROM Patient WHERE (Email = ? AND Password = ?)", new String[]{email, pass});
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            return true;
        }
        return false;
    }

    public boolean checkEmail(String email)
    {
        String[] columns = {
                PATIENT_ID
        };
        db = this.getReadableDatabase();

        Cursor cursor =  db.rawQuery("SELECT * FROM Patient WHERE Email = ?", new String[]{email});
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorCount > 0){
            return true;
        }
        return false;
    }

    public Patient getPatient(String email)
    {
        Patient p = new Patient();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Patient WHERE Email = ?", new String[]{email});
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                p.setFirstName(cursor.getString(cursor.getColumnIndex("First_Name")));
                p.setLastName(cursor.getString(cursor.getColumnIndex("Last_Name")));
                p.setEmail(email);
                p.setGender(cursor.getString(cursor.getColumnIndex("Gender")));
                p.setHeight(cursor.getInt(cursor.getColumnIndex("Height")));
                p.setAge(cursor.getInt(cursor.getColumnIndex("Age")));
                p.setPassword(cursor.getString(cursor.getColumnIndex("Password")));
            }
        }
        cursor.close();

        return p;
    }
    public Therapy getSelectedTherapy(String email, String exercise)
    {
        Therapy t = new Therapy();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Therapy WHERE (PatientEmail = ? AND Exercise=?)", new String[]{email, exercise});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                t.setTherapistEmail(cursor.getString(cursor.getColumnIndex("Therapist_Email")));
                t.setPatientEmail(email);
                t.setExercise(exercise);
                t.setSessionDuration(cursor.getInt(cursor.getColumnIndex("Session_Duration")));
                t.setSessionsPerDay(cursor.getInt(cursor.getColumnIndex("Sessions_Per_Day")));
                t.setDaysPerWeek(cursor.getInt(cursor.getColumnIndex("Days_Per_Week")));
                t.setTotalWeeks(cursor.getInt(cursor.getColumnIndex("Total_Weeks")));
            }
        }
        cursor.close();
        return t;
    }
    public String[] getPatientTherapies(String email)
    {
        ArrayList<String> exercises = new ArrayList<String>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Therapy WHERE PatientEmail = ?", new String[]{email});
        if (cursor != null) {
            try {
                while (cursor.moveToNext()) {
                    exercises.add(cursor.getString(cursor.getColumnIndex("Exercise")));
                }
            }finally{
                cursor.close();
            }
        }
        String[] ex = new String[exercises.size()];
        ex = exercises.toArray(ex);

        return ex;
    }

    public boolean updatePatient(Patient patient, String email) {

        Boolean exists = checkEmail(patient.getEmail());

        if (exists) {
            db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(PATIENT_FN, patient.getFirstName());
            values.put(PATIENT_LN, patient.getLastName());
            values.put(PATIENT_GENDER, patient.getGender());
            values.put(PATIENT_HEIGHT, patient.getHeight());
            values.put(PATIENT_AGE, patient.getAge());

            String where = "Email=?";
            String[] argument = new String[]{patient.getEmail()};
            long result = db.update(TBL_PATIENT, values, where, argument);
            if (result == -1) {
                db.close();
                return false;
            } else {
                db.close();
                return true;
            }
        }
        else {
           exists = insertPatient(patient);
            if (!exists) {
                db.close();
                return false;
            } else {
               String[] therapies = getPatientTherapies(email);
               if(therapies == null)
               {
                   db.close();
                   return true;
               }
               else{
                   Therapy therapy = new Therapy();
                   for(int i = 0; i<therapies.length; i++) {
                       therapy = getSelectedTherapy(email, therapies[i]);
                       exists = updateTherapy(therapy, email);
                       if(!exists)
                       {
                           db.close();
                           return false;
                       }
                   }

                   return true;
               }

            }
        }
    }
    public boolean updateTherapy(Therapy therapy, String email)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(THERAPY_PATIENT_EMAIL, therapy.getPatientEmail());
        String where = "PatientEmail=? AND Exercise=?";
        String[] argument = new String[]{email, therapy.getExercise()};
        long result = db.update(TBL_THERAPY, values, where, argument);
        if (result == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public boolean updatePP(ProfilePicture pp)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PP_DATA, pp.getData());
        System.out.println("LOOK"+pp.getData() + pp.getPatientEmail());
        String where = "Patient_Email=?";
        String[] argument = new String[]{pp.getPatientEmail()};
        long result = db.update(TBL_PP, values, where, argument);
        if (result == 0)
        {
            values.put(PP_PATIENT_EMAIL, pp.getPatientEmail());
            result = db.insert(TBL_PP, null, values);
            if(result == -1)
            {
                db.close();
                return false;
            }
            else{
                db.close();
                return true;
            }

        }
        if(result == -1)
        {
            db.close();
            return false;
        }
        else {
            db.close();
            return true;
        }
    }
    public ProfilePicture getPP(String email)
    {
        ProfilePicture pp = new ProfilePicture();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Profile_Pictures WHERE Patient_Email = ?", new String[]{email});
        if (cursor != null) {
            if(cursor.moveToFirst()) {
                pp.setData(cursor.getBlob(cursor.getColumnIndex("Data")));
                pp.setPatientEmail(email);
            }
        }
        cursor.close();

        return pp;
    }
}
