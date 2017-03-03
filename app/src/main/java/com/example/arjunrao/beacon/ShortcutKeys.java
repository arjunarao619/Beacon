package com.example.arjunrao.beacon;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class ShortcutKeys extends AppCompatActivity {
boolean DISABLE_SHAKE = true;
    SQLiteDatabase db;
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


    private ListView drawerList;
    final Context context = this;
    public boolean is_follow,is_record,is_location;
    public DrawerLayout drawerLayout;
    private Cursor optionsCursor;
    public ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut_keys);

        Beacon_Database optionsHelper = new Beacon_Database(ShortcutKeys.this);
        db = optionsHelper.getReadableDatabase();
        final CheckBox isfollow = (CheckBox) findViewById(R.id.follow_me);
        isfollow.setVisibility(View.GONE);
        final CheckBox record = (CheckBox) findViewById(R.id.record);
        record.setVisibility(View.GONE);
        ToggleButton enabled = (ToggleButton) findViewById(R.id.switchEnabled);


        optionsCursor = db.query("OPTIONS",new String[]{"ISLOCATION","ISFOLLOW","ISRECORD"},null,null,null,null,null,null);
        optionsCursor.moveToFirst();
        if(optionsCursor.getInt(0) == 1){
            is_location = true;

            enabled.performClick();
            isfollow.setVisibility(View.VISIBLE);
            record.setVisibility(View.VISIBLE);
        }else {is_location = false;
            enabled.setChecked(false);
            isfollow.setVisibility(View.GONE);
            record.setVisibility(View.GONE);}

        if(optionsCursor.getInt(1) == 1){
            is_follow = true;
            enabled.setChecked(true);
            isfollow.setVisibility(View.VISIBLE);
            record.setVisibility(View.VISIBLE);

        }else is_follow = false;
        if(optionsCursor.getInt(2) == 1){
            is_record = true;

            enabled.setChecked(true);
        }else is_record = false;
        enabled.setChecked(false);


       enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if(isChecked){
                   isfollow.setVisibility(View.VISIBLE);
                   record.setVisibility(View.VISIBLE);
                   is_location = true;
                   int location,follow,record1;
                   ContentValues values = new ContentValues();
                   if(is_location)location = 1; else location = 0;
                   if(is_follow) follow = 1; else follow = 0;
                   if(is_record) record1 = 1; else record1 = 0;
                   values.put("ISLOCATION",location);
                   values.put("ISFOLLOW",follow);
                   values.put("ISRECORD",record1);

                   db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});
               }
               else{
                   is_location = false;
                   isfollow.setVisibility(View.GONE);
                   record.setVisibility(View.GONE);
                   int location,follow,record1;
                   ContentValues values = new ContentValues();
                   if(is_location)location = 1; else location = 0;
                   if(is_follow) follow = 1; else follow = 0;
                   if(is_record) record1 = 1; else record1 = 0;
                   values.put("ISLOCATION",location);
                   values.put("ISFOLLOW",follow);
                   values.put("ISRECORD",record1);

                   db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});
               }

           }
       });






        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Email Settings");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Navigation_Drawer drawerItems[] = new Navigation_Drawer[]{
                new Navigation_Drawer(R.drawable.nav_contacts1,"Trusted Contacts"),
                new Navigation_Drawer(R.drawable.nav_emergency1,"Emergency Settings"),
                new Navigation_Drawer(R.drawable.nav_location1,"Your Location"),
                new Navigation_Drawer(R.drawable.nav_helpline1,"Helplines and Tips"),
                new Navigation_Drawer(R.drawable.nav_notifications1,"Audio Settings"),
                new Navigation_Drawer(R.drawable.nav_message1,"Email and SMS"),
                new Navigation_Drawer(R.drawable.nav_developer,"Developer")

        };

        Drawer_adapter adapter = new Drawer_adapter(this,R.layout.listview_layout,drawerItems);
        drawerList = (ListView) findViewById(R.id.drawer);

        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new ShortcutKeys.DrawerItemClickListener());

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



        if(isfollow.isChecked()){
            is_follow = true;
            int location,follow,record1;
            ContentValues values = new ContentValues();
            if(is_location)location = 1; else location = 0;
            if(is_follow) follow = 1; else follow = 0;
            if(is_record) record1 = 1; else record1 = 0;
            values.put("ISLOCATION",location);
            values.put("ISFOLLOW",follow);
            values.put("ISRECORD",record1);

            db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});
        }
        if(record.isChecked()){
            is_record = true;
            int location,follow,record1;
            ContentValues values = new ContentValues();
            if(is_location)location = 1; else location = 0;
            if(is_follow) follow = 1; else follow = 0;
            if(is_record) record1 = 1; else record1 = 0;
            values.put("ISLOCATION",location);
            values.put("ISFOLLOW",follow);
            values.put("ISRECORD",record1);

            db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});
        }

        if(!is_location){
            enabled.setChecked(false);
            is_follow = false;
            is_record = false;
            int location,follow,record1;
            ContentValues values = new ContentValues();
            if(is_location)location = 1; else location = 0;
            if(is_follow) follow = 1; else follow = 0;
            if(is_record) record1 = 1; else record1 = 0;
            values.put("ISLOCATION",location);
            values.put("ISFOLLOW",follow);
            values.put("ISRECORD",record1);

            db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});
        }






    }














    public void selectItem(int position) {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:
                Intent intent = new Intent(ShortcutKeys.this, TrustedActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(ShortcutKeys.this, ShortcutKeys.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(ShortcutKeys.this, UserLocation2.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(ShortcutKeys.this, HelpLineActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(ShortcutKeys.this, Notifications.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(ShortcutKeys.this,EmailActivity.class);
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
                Intent intent = new Intent(ShortcutKeys.this, MainDashBoard.class);
                startActivity(intent);
        }
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        int location,follow,record1;
        ContentValues values = new ContentValues();
        if(is_location)location = 1; else location = 0;
        if(is_follow) follow = 1; else follow = 0;
        if(is_record) record1 = 1; else record1 = 0;
        values.put("ISLOCATION",location);
        values.put("ISFOLLOW",follow);
        values.put("ISRECORD",record1);

        db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        int location,follow,record1;
        ContentValues values = new ContentValues();
        if(is_location)location = 1; else location = 0;
        if(is_follow) follow = 1; else follow = 0;
        if(is_record) record1 = 1; else record1 = 0;
        values.put("ISLOCATION",location);
        values.put("ISFOLLOW",follow);
        values.put("ISRECORD",record1);

        db.update("OPTIONS",values,"_id = ?",new String[] {Integer.toString(1)});



    }

    @Override
    protected void onResume(){
        super.onResume();
        Beacon_Database optionsHelper = new Beacon_Database(ShortcutKeys.this);
        db = optionsHelper.getReadableDatabase();
        final CheckBox isfollow = (CheckBox) findViewById(R.id.follow_me);
        isfollow.setVisibility(View.GONE);
        final CheckBox record = (CheckBox) findViewById(R.id.record);
        record.setVisibility(View.GONE);
        ToggleButton enabled = (ToggleButton) findViewById(R.id.switchEnabled);

        optionsCursor = db.query("OPTIONS",new String[]{"ISLOCATION","ISFOLLOW","ISRECORD"},null,null,null,null,null,null);
        optionsCursor.moveToFirst();
        if(optionsCursor.getInt(0) == 1){
            is_location = true;

            enabled.performClick();
            isfollow.setVisibility(View.VISIBLE);
            record.setVisibility(View.VISIBLE);
        }else {is_location = false;
            enabled.setChecked(false);
            isfollow.setVisibility(View.GONE);
            record.setVisibility(View.GONE);}

        if(optionsCursor.getInt(1) == 1){
            is_follow = true;
            enabled.setChecked(true);
            isfollow.setVisibility(View.VISIBLE);
            record.setVisibility(View.VISIBLE);

        }else is_follow = false;
        if(optionsCursor.getInt(2) == 1){
            is_record = true;
            enabled.performClick();
            enabled.setChecked(true);
        }else is_record = false;
        enabled.setChecked(false);
    }


}
