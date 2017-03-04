package com.example.arjunrao.beacon;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainDashBoard extends AppCompatActivity {
    


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

    private String[] titles;
    private ListView drawerList;
    final Context context = this;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dash_board);

        getSupportActionBar().setTitle("Settings");

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Settings");

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Navigation_Drawer drawerItems[] = new Navigation_Drawer[]{
                new Navigation_Drawer(R.drawable.nav_contacts1,"Trusted Contacts"),
                new Navigation_Drawer(R.drawable.nav_emergency1,"Emergency Settings"),
                new Navigation_Drawer(R.drawable.nav_location1,"Your Location"),
                new Navigation_Drawer(R.drawable.nav_helpline1,"Helplines and Tips"),
                new Navigation_Drawer(R.drawable.message_template,"Message Templates"),
                new Navigation_Drawer(R.drawable.nav_message1,"Email and SMS"),
                new Navigation_Drawer(R.drawable.nav_developer,"Developer")

        };

        Drawer_adapter adapter1 = new Drawer_adapter(this,R.layout.listview_layout,drawerItems);
        drawerList = (ListView) findViewById(R.id.drawer);

        drawerList.setAdapter(adapter1);
        drawerList.setOnItemClickListener(new MainDashBoard.DrawerItemClickListener());

        //navigation drawer



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




        RecyclerView mainRecycler = (RecyclerView) findViewById(R.id.main_recycler);

        String optionNames[] = new String[Card_data.data.length];


        for(int i = 0;i<optionNames.length;i++){
            optionNames[i] = Card_data.data[i].getName();
        }
        int images[] = new int[Card_data.data.length];
        for(int j = 0;j<optionNames.length;j++){
            images[j] = Card_data.data[j].getImageResourceId();
        }

        OptionsAdapter adapter = new OptionsAdapter(optionNames,images);
        mainRecycler.setAdapter(adapter);
        GridLayoutManager manager = new GridLayoutManager(MainDashBoard.this,2);
        mainRecycler.setLayoutManager(manager);

        mainRecycler.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        switch(position){
                            case 0:
                                Intent intent = new Intent(MainDashBoard.this,TrustedActivity.class);
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(MainDashBoard.this,ShortcutKeys.class);
                                startActivity(intent1);
                                break;
                            case 2:
                                Intent intent2 = new Intent(MainDashBoard.this,EmailActivity.class);
                                startActivity(intent2);
                                break;
                            case 3:
                                Intent intent3 = new Intent(MainDashBoard.this,MessageTemplates.class);
                                startActivity(intent3);
                                break;
                            case 4:
                                Intent intent4 = new Intent(MainDashBoard.this,UserLocation2.class);
                                startActivity(intent4);
                                break;
                            case 5:
                                LayoutInflater li = LayoutInflater.from(context);
                                View devView = li.inflate(R.layout.about_developer_dialog,null);

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setView(devView);




                                builder.setCancelable(false).setPositiveButton("GREAT!", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder.show();

                        }
                    }
                })
        );
    }


    public void selectItem(int position) {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:
                Intent intent = new Intent(MainDashBoard.this, TrustedActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(MainDashBoard.this, ShortcutKeys.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(MainDashBoard.this, UserLocation2.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(MainDashBoard.this, HelpLineActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(MainDashBoard.this, MessageTemplates.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(MainDashBoard.this,EmailActivity.class);
                startActivity(intent5);
                break;
            case 6:
                LayoutInflater li = LayoutInflater.from(context);
                View devView = li.inflate(R.layout.about_developer_dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(devView);


                builder.setCancelable(false).setPositiveButton("GREAT!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();

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
                Intent intent = new Intent(MainDashBoard.this, MainDashBoard.class);
                startActivity(intent);
        }
        return true;
    }


}
