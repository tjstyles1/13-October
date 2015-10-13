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
public class ConfirmBaselineDeletion extends AppCompatActivity {

    private DBAdapter db = new DBAdapter(this);
    private Baseline b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_baseline_deletion);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        b = (Baseline) intent.getParcelableExtra("baseline");

        //Generates customised warning message
        TextView lblWarning = (TextView) findViewById(R.id.lblConfirmBaselineDelete);
        db.open();
        Cursor c = db.getBaselineRecord(b.getId());
        int iUserIDFK = c.getColumnIndex(DBAdapter.USER_ID_FK);
        Cursor d = db.getUserRecord(c.getLong(iUserIDFK));
        int iUserName = d.getColumnIndex(DBAdapter.USER_NAME);
        String userName = d.getString(iUserName);
        db.close();
        lblWarning.setText(userName + ", are you sure you want to delete the '" +
                b.getName() + "' baseline?");

        Button btnConfirm = (Button) findViewById(R.id.btnBaselineDeleteYes);
        btnConfirm.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
                finish();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnBaselineDeleteNo);
        btnCancel.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void delete() {
        db.open();
        db.deleteBaseline(b.getId());
        db.close();
    }
}
