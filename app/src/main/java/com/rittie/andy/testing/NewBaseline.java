package com.rittie.andy.testing;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewBaseline extends AppCompatActivity {

    private User u;
    private EditText txtNewBaseline;
    private TextView lblBaselineAlreadyExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_baseline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        u = (User) intent.getParcelableExtra("user");

        txtNewBaseline = (EditText) findViewById(R.id.txtNewBaseline);
        lblBaselineAlreadyExists = (TextView) findViewById(R.id.lblBaselineAlreadyExisits);
        lblBaselineAlreadyExists.setVisibility(View.INVISIBLE);

        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblBaselineAlreadyExists.setVisibility(View.INVISIBLE);
                databaseOperations();
            }
        });
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

    private void databaseOperations() {
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllBaselineRecords();
        int iUserIDFK = c.getColumnIndex(DBAdapter.USER_ID_FK);
        int iBaselineName = c.getColumnIndex(DBAdapter.BASELINE_NAME);
        String newBaseline = txtNewBaseline.getText().toString().trim();
        boolean alreadyExists = false;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            if (u.getId() == c.getLong(iUserIDFK) &&
                    newBaseline.equals(c.getString(iBaselineName))) {
                alreadyExists = true;
            }
        }
        db.close();
        if (newBaseline.equals("")) {
            lblBaselineAlreadyExists.setVisibility(View.VISIBLE);
            lblBaselineAlreadyExists.setText("No Baseline Name Has Been Entered");
        } else {
            if (alreadyExists) {
                lblBaselineAlreadyExists.setVisibility(View.VISIBLE);
                lblBaselineAlreadyExists.setText("This Baseline Already Exists");
            } else {
                db.open();
                db.insertBaseline(u.getId(), newBaseline, 0);
                db.close();
                Intent in = new Intent(NewBaseline.this, HomeScreen.class);
                in.putExtra("user", u);
                startActivity(in);
                finish();
            }
        }
    }
}