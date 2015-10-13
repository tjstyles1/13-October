package com.rittie.andy.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by James on 4/10/2015.
 * Last Edited on 4/10/2015.
 */
public class DeleteUser extends AppCompatActivity {

    private User u;
    private boolean hasPassword;
    private TextView lblIncorrectPassword;
    private EditText txtEnterPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        u = (User) intent.getParcelableExtra("user");

        //Generates customised warning message
        TextView lblWarningMessage = (TextView) findViewById(R.id.lblConfirmDelete);
        lblWarningMessage.setText(u.getName() + ", are you sure you want to delete your account?");

        lblIncorrectPassword = (TextView) findViewById(R.id.lblPasswordError);
        lblIncorrectPassword.setVisibility(View.INVISIBLE);
        TextView lblEnterPass = (TextView) findViewById(R.id.lblEnterPassword);
        txtEnterPass = (EditText) findViewById(R.id.txtPassword);
        if (u.getPassword() != null && !u.getPassword().equals("")) {
            lblEnterPass.setVisibility(View.VISIBLE);
            txtEnterPass.setVisibility(View.VISIBLE);
            hasPassword = true;
        } else {
            lblEnterPass.setVisibility(View.INVISIBLE);
            txtEnterPass.setVisibility(View.INVISIBLE);
            hasPassword = false;
        }

        Button btnConfirmed = (Button) findViewById(R.id.btnBaselineDeleteYes);
        btnConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hasPassword) {
                    delete();
                } else if (txtEnterPass.getText().toString().equals(u.getPassword())) {
                    delete();
                } else {
                    lblIncorrectPassword.setVisibility(View.VISIBLE);
                }
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnBaselineDeleteNo);
        btnCancel.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(DeleteUser.this, HomeScreen.class);
                in.putExtra("user", u);
                startActivity(in);
                finish();
            }
        });
    }

    private void delete() {
        DBAdapter db = new DBAdapter(this);
        db.open();
        db.deleteUser(u.getId());
        db.close();
        startActivity(new Intent(DeleteUser.this, MainActivity.class));
        finish();
    }
}
