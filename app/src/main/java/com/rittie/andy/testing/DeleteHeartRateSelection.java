package com.rittie.andy.testing;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by James on 4/10/2015.
 * Lasted Edited on 4/10/2015.
 */
public class DeleteHeartRateSelection extends AppCompatActivity {

    private ArrayList<HeartRate> heartRates;
    private DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_heart_rate_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Baseline b = (Baseline) intent.getParcelableExtra("baseline");

        heartRates = new ArrayList<HeartRate>();
        db.open();
        Cursor c = db.getHeartRateWithBaselineIDRecord(b.getId());
        int iStartTime = c.getColumnIndex(DBAdapter.START_TIME);
        int iBaselineIDFK = c.getColumnIndex(DBAdapter.BASELINE_ID_FK);
        String[] startTimes = new String[c.getCount()];
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            boolean alreadyExists = false;
            for (int x = 0; x < startTimes.length; x++) {
                if (startTimes[x] != null && c.getString(iStartTime).equals(startTimes[x])) {
                    alreadyExists = true;
                    break;
                } else if (startTimes[x] == null) {
                    startTimes[x] = c.getString(iStartTime);
                    break;
                }
            }
            if (!alreadyExists) {
                heartRates.add(new HeartRate(c.getString(iStartTime), c.getLong(iBaselineIDFK)));
            }
        }
        db.close();

        ArrayAdapter<HeartRate> itemsAdapter =
                new ArrayAdapter<HeartRate>(this, android.R.layout.simple_list_item_1, heartRates);
        final ListView listView = (ListView) findViewById(R.id.heartRateListView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final HeartRate hr = (HeartRate) parent.getItemAtPosition(position);
                Intent in = new Intent(DeleteHeartRateSelection.this, ConfirmHRBaselineDeletion.class);
                in.putExtra("heartRate", hr);
                startActivity(in);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<HeartRate> itemsAdapter =
                new ArrayAdapter<HeartRate>(this, android.R.layout.simple_list_item_1, heartRates);
        ListView listView = (ListView) findViewById(R.id.heartRateListView);
        listView.setAdapter(itemsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
