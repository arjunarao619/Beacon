package com.example.arjunrao.beacon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HelpLineActivity extends AppCompatActivity {

    private ListView drawerList;
    final Context context = this;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


            /* Do three things
            - change the activity
            - change the title in the action bar
            - close the drawer
             */

            selectItem(position);


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Email Settings");
        }
        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        getSupportActionBar().setHomeButtonEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //countries listview
        ListView countries = (ListView) findViewById(R.id.list_flags);

        Navigation_Drawer drawerItems1[] = new Navigation_Drawer[]{
                new Navigation_Drawer(R.drawable.help_india,"India"),
                new Navigation_Drawer(R.drawable.help_usa,"USA"),
                new Navigation_Drawer(R.drawable.help_france,"France"),
                new Navigation_Drawer(R.drawable.help_uk,"United Kingdom"),
                new Navigation_Drawer(R.drawable.help_china,"China"),
                new Navigation_Drawer(R.drawable.help_germany,"Germany"),
                new Navigation_Drawer(R.drawable.help_russia,"Russia"),
                new Navigation_Drawer(R.drawable.help_spain,"Spain"),


        };

        Drawer_adapter adapter1 = new Drawer_adapter(this,R.layout.help_listview_layout,drawerItems1);
        countries.setAdapter(adapter1);
        //TODO CREATE ALL HELPLINES!

        countries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HelpLineActivity.this,HelpLines.class);
                intent.putExtra("COUNTRY",String.valueOf(position));
                startActivity(intent);
            }
        });

        //navigation drawer

        //  titles = getResources().getStringArray(R.array.titles);
        Navigation_Drawer drawerItems[] = new Navigation_Drawer[]{

                new Navigation_Drawer(R.drawable.nav_contacts1,"Trusted Contacts"),

                new Navigation_Drawer(R.drawable.nav_emergency1,"Emergency Settings"),

                new Navigation_Drawer(R.drawable.nav_location1,"Your Location"),

                new Navigation_Drawer(R.drawable.nav_helpline1,"Helplines and Tips"),

                new Navigation_Drawer(R.drawable.message_template,"Message Templates"),

                new Navigation_Drawer(R.drawable.nav_message1,"Email and SMS"),



        };

        Drawer_adapter adapter = new Drawer_adapter(this,R.layout.listview_layout,drawerItems);
        drawerList = (ListView) findViewById(R.id.drawer);

        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new HelpLineActivity.DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerList.bringToFront();
                drawerLayout.requestLayout();
                invalidateOptionsMenu();
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);


    }

    public void selectItem(int position) {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:
                Intent intent = new Intent(HelpLineActivity.this, TrustedActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(HelpLineActivity.this, MainDashBoard.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(HelpLineActivity.this, UserLocation2.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(HelpLineActivity.this, HelpLineActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(HelpLineActivity.this, MessageTemplates.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(HelpLineActivity.this,EmailActivity.class);
                startActivity(intent5);
                break;


        }


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        menu.findItem(R.id.settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override

    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;

    }

    @Override

    public boolean onOptionsItemSelected(MenuItem menu) {

        if(drawerToggle.onOptionsItemSelected(menu)){
            return  true;
        }
        switch (menu.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(HelpLineActivity.this, MainDashBoard.class);
                startActivity(intent);
        }
        return true;
    }

}
