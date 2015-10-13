package com.rittie.andy.testing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeScreen extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen); //Sets the layout to activity_home_screen

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        u = (User) intent.getParcelableExtra("user");

        //Changes the username in the navigation drawer to whoever is logged in
        final TextView username = (TextView) findViewById(R.id.username);
        username.setText(String.valueOf(u));

        //Changes the email in the navigation drawer to whoever is logged in
        final TextView email = (TextView) findViewById(R.id.email);
        email.setText(String.valueOf(u.getEmail()));

        Button btnViewArousal = (Button)findViewById(R.id.btnRealViewArousal);
        btnViewArousal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeScreen.this, RealViewArousal.class);
                in.putExtra("user", u);
                startActivity(in);
            }
        });

        Button btnCalculateResting = (Button)findViewById(R.id.btnRealCalculateResting);
        btnCalculateResting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeScreen.this, RealCalculateResting.class);
                in.putExtra("user", u);
                startActivity(in);
            }
        });

        Button btnLogOut = (Button)findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, MainActivity.class));
                finish();
            }
        });

        Button btnDeleteUser = (Button) findViewById(R.id.btnDeleteUser);
        btnDeleteUser.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeScreen.this, DeleteUser.class);
                in.putExtra("user", u);
                startActivity(in);
            }
        });

        Button btnBaseline = (Button)findViewById(R.id.btnAddToBaseline);
        btnBaseline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeScreen.this, BaselineSettings.class);
                in.putExtra("user", u);
                startActivity(in);
            }
        });

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                drawerLayout.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    //I've made this button go to the homescreen activity
                    case R.id.home:
                        Intent in1 = new Intent(HomeScreen.this, HomeScreen.class);
                        in1.putExtra("user", u);
                        startActivity(in1);
                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.viewArousal:
                        Intent in2 = new Intent(HomeScreen.this, RealViewArousal.class);
                        in2.putExtra("user", u);
                        startActivity(in2);
                        return true;

                    case R.id.calculateResting:
                        Intent in3 = new Intent(HomeScreen.this, RealCalculateResting.class);
                        in3.putExtra("user", u);
                        startActivity(in3);
                        return true;

                    case R.id.nvBaselines:
                        Intent in4 = new Intent(HomeScreen.this, BaselineSettings.class);
                        in4.putExtra("user", u);
                        startActivity(in4);
                        return true;


                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
