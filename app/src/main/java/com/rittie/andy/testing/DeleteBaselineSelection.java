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
public class DeleteBaselineSelection extends AppCompatActivity {

    private ArrayList<Baseline> baselines;
    private DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_baseline_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        User u = (User) intent.getParcelableExtra("user");

        baselines = new ArrayList<Baseline>();
        db.open();
        Cursor c = db.getAllBaselineRecords();
        int iBaselineID = c.getColumnIndex(DBAdapter.BASELINE_ID);
        int iBaselineName = c.getColumnIndex(DBAdapter.BASELINE_NAME);
        int iUserIDFK = c.getColumnIndex(DBAdapter.USER_ID_FK);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (c.getLong(iUserIDFK) == u.getId()) {
                if (!c.getString(iBaselineName).equals("Resting")) {
                    baselines.add(new Baseline(c.getLong(iBaselineID), c.getString(iBaselineName), c.getLong(iUserIDFK)));
                }
            }
        }
        db.close();

        ArrayAdapter<Baseline> itemsAdapter =
                new ArrayAdapter<Baseline>(this, android.R.layout.simple_list_item_1, baselines);
        final ListView listView = (ListView) findViewById(R.id.baselineListView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Baseline b = (Baseline) parent.getItemAtPosition(position);
                Intent in = new Intent(DeleteBaselineSelection.this, ConfirmBaselineDeletion.class);
                in.putExtra("baseline", b);
                startActivity(in);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<Baseline> itemsAdapter =
                new ArrayAdapter<Baseline>(this, android.R.layout.simple_list_item_1, baselines);
        ListView listView = (ListView) findViewById(R.id.baselineListView);
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
