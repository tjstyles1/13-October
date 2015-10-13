package com.rittie.andy.testing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {

    private User u;
    private TextView wrongPassword;
    private EditText pWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        u = (User) intent.getParcelableExtra("user");

        TextView userName = (TextView) findViewById(R.id.tvName);
        userName.setText(u.getName());

        wrongPassword = (TextView) findViewById(R.id.incorrectPassword);
        wrongPassword.setVisibility(View.INVISIBLE);

        Button btnLog = (Button) findViewById(R.id.btnLogin);
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pWord = (EditText) findViewById(R.id.etPassword);
                if (pWord.getText().toString().equals(u.getPassword())) {
                    Intent in = new Intent(Login.this, HomeScreen.class);
                    in.putExtra("user", u);
                    startActivity(in);
                    finish();
                } else {
                    wrongPassword.setVisibility(View.VISIBLE);
                }
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
}
