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

public class NewUser extends AppCompatActivity {

    private TextView userAlreadyInDB;
    private TextView invalidEmail;
    private TextView invalidPWord;
    private TextView noName;
    private DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        userAlreadyInDB = (TextView)findViewById(R.id.userAlreadyExists);
        userAlreadyInDB.setVisibility(View.INVISIBLE);
        invalidEmail = (TextView)findViewById(R.id.invalidEmalAddress);
        invalidEmail.setVisibility(View.INVISIBLE);
        invalidPWord = (TextView)findViewById(R.id.invalidPassword);
        invalidPWord.setVisibility(View.INVISIBLE);
        noName = (TextView)findViewById(R.id.noName);
        noName.setVisibility(View.INVISIBLE);

        Button btnCreateUser = (Button) findViewById(R.id.btnCreateUser);
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAlreadyInDB.setVisibility(View.INVISIBLE);
                invalidEmail.setVisibility(View.INVISIBLE);
                invalidPWord.setVisibility(View.INVISIBLE);
                noName.setVisibility(View.INVISIBLE);

                EditText nameTxt = (EditText)findViewById(R.id.lblNewBaselineName);
                EditText emailTxt = (EditText)findViewById(R.id.etCreateEmail);
                EditText passwordTxt = (EditText)findViewById(R.id.etCreatePassword);

                boolean validUserName = false;
                boolean validEmail = false;
                boolean validPassword = false;

                //Verification of entered details
                if (nameTxt != null && !nameTxt.getText().toString().trim().equals("")) {
                    int userNameCount = 0;
                    db.open();
                    Cursor c = db.getAllUserRecords();
                    int iUserName = c.getColumnIndex(DBAdapter.USER_NAME);
                    for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                        if (c.getString(iUserName).equals(nameTxt.getText().toString())) {
                            userNameCount = userNameCount + 1;
                        }
                        if (userNameCount == 1) {
                            break;
                        }
                    }
                    db.close();
                    if (userNameCount == 0) {
                        validUserName = true;
                    } else {
                        userAlreadyInDB.setVisibility(View.VISIBLE);
                    }
                } else {
                    noName.setVisibility(View.VISIBLE);
                }
                if (true) {
                    validEmail = true;
                } else {
                    invalidEmail.setVisibility(View.VISIBLE);
                }
                if (true) {
                    validPassword = true;
                } else {
                    invalidPWord.setVisibility(View.VISIBLE);
                }

                if (validUserName && validEmail && validPassword) {
                    db.open();
                    long id = db.insertUser(nameTxt.getText().toString().trim(), emailTxt.getText().toString().trim(), passwordTxt.getText().toString().trim());
                    db.insertBaseline(id, "Resting", 0);
                    db.close();
                    Intent in = new Intent(NewUser.this, UserSelection.class);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
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
