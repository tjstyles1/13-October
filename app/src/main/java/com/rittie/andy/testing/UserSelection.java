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
 * Created by James on 10/09/2015.
 * Lasted Edited on 4/10/2015.
 */
public class UserSelection extends AppCompatActivity {

    private ArrayList<User> users;
    private DBAdapter db = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        users = new ArrayList<User>();
        db.open();
        Cursor c = db.getAllUserRecords();
        int iUserID = c.getColumnIndex(DBAdapter.USER_ID);
        int iUserName = c.getColumnIndex(DBAdapter.USER_NAME);
        int iUserEmail = c.getColumnIndex(DBAdapter.USER_EMAIL);
        int iUserPassword = c.getColumnIndex(DBAdapter.USER_PASSWORD);
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            users.add(new User(c.getLong(iUserID), c.getString(iUserName), c.getString(iUserEmail), c.getString(iUserPassword)));
        }
        db.close();

        ArrayAdapter<User> itemsAdapter =
                new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, users);
        final ListView listView = (ListView) findViewById(R.id.userListView);
        listView.setAdapter(itemsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final User u = (User) parent.getItemAtPosition(position);
                if (u.getPassword().equals("")) {
                    Intent in = new Intent(UserSelection.this, HomeScreen.class);
                    in.putExtra("user", u);
                    startActivity(in);
                    finish();
                } else {
                    Intent in = new Intent(UserSelection.this, Login.class);
                    in.putExtra("user", u);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayAdapter<User> itemsAdapter =
                new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, users);
        ListView listView = (ListView) findViewById(R.id.userListView);
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
