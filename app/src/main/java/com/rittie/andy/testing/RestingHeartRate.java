package com.rittie.andy.testing;

import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RestingHeartRate extends AppCompatActivity {

    private Button startButton;
    private Button stopButton;
    private TextView hrValue;
    private Handler customHandler = new Handler();
    private double avgHeartRate;
    public double[] restingHeartRateValues; //set as public so average can be calculated in another class
    private DBAdapter db = new DBAdapter(this);
    private User u;
    long baselineID = 0;

    final double[] restingHR = new double[] {65.21,64.68,60.75,63.19,64.27,61.07,61.7,66.5,66.38,63.61,63.31,59.26,56.6,58.14,60.54,59.36,59.92,63.02,60.46,58.64,65.47,63.22,58.65,65.99,60.45,56.43,63.71,58.39,56.17,62.83,59.64,55.67,60.18,60.4,57.97,62.71,64.46,59.02,59.7,67.47,59.99,58.04,62.74,67.88,60.21,61.37,66.78,59.44,60.55,64.9,59.2,60.38,65.18,63.03,64.28,71.11,70.13,63.6,67.54,62.29,57.9,62.69,60.26,56.36,58.6,63.29,56.77,55.56,64.67,57.95,57.42,68.28,59.31,58.48,64.37,65.8,59.8,61.16,63.92,58.06,57.51,62.89,63.52,59.06,61.49,62.93,57.15,58.74,61.63,57.88,57.13,60.77,62.51,59.13,63.47,66.74,69.31,73.31,67.69,60.1,57.53,59.7,65.32,59.7,58.76,64.85,59.85,62.14,68.12,63.93,60.29,58.12,59.06,59.63,56.89,56.78,59.34,63.12,58.64,57.09,60.4,64.09,58.38,57.9,61.45,71.23,75.02,72.86,71.13,68.14,65.07,65.43,67.34,65.41,65.44,70.21,68.61,64.6,63.25,65.81,63.61,62.19,64.65,67.18,64.43,62.88,66.37,64.65,62.2,63.47,66.14,65.19,68.11,70.24,66.58,65.15,68.42,65.77,65.27,67.42,69.86,66.77,66.22,67.48,66.26,64.71,69.64,67.39,69.42,72.52,76.81,80.62,76.4,75.18,68.29,62.89,68.55,63.55,60.45,66.42,61.84,61.51,67.16,63.63,66.05,68.54,63.6,63.8,66.77,62.05,61.95,68.78,62.09,61.31,65.1,65.8,61.39,62.33,66.06,63.83,62.08,65.55,66.08,65.75,71.3,69.31,65.5,65.66,67.62,66.51,68.35,70.81,66.71,65.67,67.61,64.57,65.76,66.26,64.63,66.37,70.42,67.57,64.69,66.59,68.4,71.88,76.55,81.05,72.3,68.23,67.56,62.59,64.16,61.92,59.39,59.62,60.21,58.29,57.97,62.76,60.68,58.41,60.66,61.49,59.67,59.51,65.69,62.19,61.12,62.45,65.16,60.22,59.21,63.27,60.43,59.37,62.27,63.95,60.56,62.35,64.22,61.65,63.72,68.53,68.52,65.42,68.08,63.53,59.67,62.18,61.89,60,60.89,66.35,62.14,63,66.26,66.54,63.91,66.91,65.96,63.04,64.93,64.71,61.29,60.94,66.55,63.47,63.27,66.06,64.82,62.43,65.13,66.02,63.75,67.7,75.26,79.92,77.03,59.86,56.07,57.75,63.66,58.51,54.68,54.46,58.82,58.7,54.89,56.48,60.56,59.14,57.9,61.8,64.34,59.24,60.22,62.63,61.98,61.08};
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();

        u = (User) intent.getParcelableExtra("user");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resting_heart_rate);
        hrValue = (TextView) findViewById(R.id.heartRate);
        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customHandler.postDelayed(updateScreen,1000);
            }
        });

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hrValue.setText(String.format("Average: %.2f", u.calcAvg(restingHR)));
                customHandler.removeCallbacks(updateScreen);
            }
        });

        //gets heart rate data from database
        db.open();
        Cursor c = db.getBaselineWithUserIDRecord(u.getId());
        int iBaselineID = c.getColumnIndex(DBAdapter.BASELINE_ID);
        int iBaselineName = c.getColumnIndex(DBAdapter.BASELINE_NAME);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getString(iBaselineName).equals("Resting")) {
                baselineID = c.getLong(iBaselineID);
                break;
            }
        }
        Cursor d = db.getUsersHeartRateRecords(baselineID);
        int count = d.getCount();
        if (count > 0) {
            restingHeartRateValues = new double[count];
            int iHeartRateValue = d.getColumnIndex(DBAdapter.HEART_RATE);
            int x = 0;
            for (d.moveToFirst(); !d.isAfterLast(); d.moveToNext()) {
                restingHeartRateValues[x] = d.getDouble(iHeartRateValue);
                x++;
            }
        } else {
            //none exist in database yet
        }
        db.close();
    }

    private Runnable updateScreen = new Runnable() {
        int i = 0;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        public void run() {
            hrValue.setText(String.valueOf(restingHR[i]));
            db.open();
            //change restingHR[i] to input from band
            db.insertHeartRate(baselineID, restingHR[i], dateFormat.format(date));
            db.close();
            i++;
            if (i == restingHR.length) {
                hrValue.setText(String.format( "Average: %.2f",u.calcAvg(restingHR)));
                customHandler.removeCallbacks(this);
            }
            else{
                customHandler.postDelayed(this,1000);
            }
        }
    };

    @Override
    public void onBackPressed(){
        Intent in = new Intent(this, UserHomeActivity.class);
        in.putExtra("user", u);
        startActivity(in);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resting_heart_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
