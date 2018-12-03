package com.ptpal.ptpal.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ListView;

import com.ptpal.ptpal.R;
import com.ptpal.ptpal.model.Session;
import com.ptpal.ptpal.model.SessionListAdapter;
import com.ptpal.ptpal.sql.PTPalDB;

import java.util.ArrayList;

public class TherapyHistoryActivity extends AppCompatActivity implements View.OnClickListener
{
    private final AppCompatActivity activity = TherapyHistoryActivity.this;

    private ListView slv;

    private AppCompatTextView appCompatTextViewHistToPat;

    private SessionListAdapter adapter;
    private PTPalDB myDB;
    private ArrayList<Session> sessionList;
    private String patEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapyhistory);

        getSupportActionBar().hide();
        patEmail = getIntent().getStringExtra("EMAIL");

        initObjects();
        initViews();
        initListeners();
    }

    public void initObjects()
    {
        myDB = new PTPalDB(activity);
        sessionList = myDB.getSessions(patEmail);
    }

    public void initViews()
    {
        adapter = new SessionListAdapter(this, R.layout.adapter_view_layout, sessionList);

        slv = (ListView) findViewById(R.id.listViewSLV);
        slv.setAdapter(adapter);
        appCompatTextViewHistToPat = (AppCompatTextView) findViewById(R.id.appCompatTextViewHistToPat);
    }
    public void initListeners()
    {
        appCompatTextViewHistToPat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatTextViewHistToPat:
                finish();
                break;
        }
    }
}
