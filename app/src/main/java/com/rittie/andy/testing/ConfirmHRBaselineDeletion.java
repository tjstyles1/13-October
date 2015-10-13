package com.rittie.andy.testing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by James on 4/10/2015.
 * Last Edited on 4/10/2015.
 */
public class ConfirmHRBaselineDeletion extends AppCompatActivity {

    private DBAdapter db = new DBAdapter(this);
    private HeartRate hr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_hr_baseline_deletion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        hr = (HeartRate) intent.getParcelableExtra("heartRate");

        //Generates customised warning message
        TextView lblWarning = (TextView) findViewById(R.id.lblConfirmHRBaselineDelete);
        String baselineName;
        db.open();
        Cursor c = db.getBaselineRecord(hr.getBaselineIDFK());
        int iUserIDFK = c.getColumnIndex(DBAdapter.USER_ID_FK);
        Cursor d = db.getUserRecord(c.getLong(iUserIDFK));
        int iUserName = d.getColumnIndex(DBAdapter.USER_NAME);
        String userName = d.getString(iUserName);
        int iBaselineName = c.getColumnIndex(DBAdapter.BASELINE_NAME);
        baselineName = c.getString(iBaselineName);
        db.close();
        lblWarning.setText(userName + ", are you sure you want to delete the heart rate values recorded on " +
                hr.getStartTime() + " from the '" + baselineName + "' baseline?");

        Button btnConfirm = (Button) findViewById(R.id.btnHRBaselineDeleteYes);
        btnConfirm.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnHRBaselineDeleteNo);
        btnCancel.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void delete() {
        db.open();
        Cursor c = db.getHeartRateWithBaselineIDRecord(hr.getBaselineIDFK());
        int iHeartRateID = c.getColumnIndex(DBAdapter.HEART_RATE_ID);
        int iStartTime = c.getColumnIndex(DBAdapter.START_TIME);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(iStartTime).equals(hr.getStartTime())) {
                db.deleteHeartRate(c.getLong(iHeartRateID));
            }}
        db.close();
    }
}
