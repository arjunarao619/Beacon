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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TrustedActivity extends AppCompatActivity {

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

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
   final Context context = this;
    static final int PICK_CONTACT=1;
    SQLiteDatabase db,db1;
    TextView noContacts;
    private Cursor contactNameCursor;
    private Cursor numberCursor;
    String[] contact_names;
    public FloatingActionButton fab;
    String[] contact_numbers;
    int images[];
    ImageView contact_pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted);

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
                new Navigation_Drawer(R.drawable.message_template,"Message Templates"),
                new Navigation_Drawer(R.drawable.nav_message1,"Email and SMS"),
                new Navigation_Drawer(R.drawable.nav_developer,"Developer")

        };

        Drawer_adapter adapter1 = new Drawer_adapter(this,R.layout.listview_layout,drawerItems);
        drawerList = (ListView) findViewById(R.id.drawer);

        drawerList.setAdapter(adapter1);
        drawerList.setOnItemClickListener(new TrustedActivity.DrawerItemClickListener());

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

        Beacon_Database beacon_database = new Beacon_Database(this);
        db = beacon_database.getReadableDatabase();

        RecyclerView contactRecycler = (RecyclerView) findViewById(R.id.contact_list);
        contactRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(TrustedActivity.this);
        contactRecycler.setLayoutManager(layoutManager);
        contactRecycler.setItemAnimator(new DefaultItemAnimator());





        // Initiating a cursor to the database*/

       contactNameCursor = db.query("CONTACTS",new String[] {"NAME","NUMBER"},null,null,null,null,null);




        if(contactNameCursor.moveToFirst()){

            //* initialising the contact name and number array if there is data in the cursor *//*
            contactNameCursor.moveToFirst();
            contact_names = new String[contactNameCursor.getCount()];
            contact_numbers = new String[contactNameCursor.getCount()];
            images = new int[contactNameCursor.getCount()];


            noContacts = (TextView) findViewById(R.id.nocontacts);
            noContacts.setVisibility(View.GONE);

            for(int i = 0;i<contactNameCursor.getCount();i++){
                contact_names[i] = contactNameCursor.getString(0);
                contact_numbers[i] = contactNameCursor.getString(1);

                contactNameCursor.moveToNext();
               // numberCursor.moveToNext();
            }



          for(int i = 0;i<contactNameCursor.getCount();i++){
                images[i] = Hardcoded_contacts.contacts[i].getImageResourceId();
            }



            MyAdapter adapter = new MyAdapter(contact_names,contact_numbers,images);
            contactRecycler.setAdapter(adapter);
          /*  adapter.setListener(new MyAdapter.Listener() {
                @Override
                public void onLongClick(int position) {
                    fab = (FloatingActionButton) findViewById(R.id.create_contact);
                    fab.setVisibility(View.GONE);
                    FloatingActionButton delete_contact = (FloatingActionButton) findViewById(R.id.delete_contact);
                    delete_contact.setVisibility(View.VISIBLE);

                    delete_contact.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //TODO
                        }
                    });

                }
            });*/

        }
        if (!contactNameCursor.moveToFirst()){
            //user does not have any contacts stored yet
        }











         fab = (FloatingActionButton) findViewById(R.id.create_contact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.new_contact_dialog,null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(promptsView);

                final EditText contactName = (EditText) promptsView.findViewById(R.id.name);
                final EditText phone_number = (EditText) promptsView.findViewById(R.id.number);

                builder.setCancelable(true).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String contact_text = contactName.getText().toString();
                        String number_text = phone_number.getText().toString();



                        //checking if edittext fields are empty

                        if(contact_text.equals("") || number_text.equals("")){
                            AlertDialog.Builder alertdialog = new AlertDialog.Builder(TrustedActivity.this);
                            alertdialog.setTitle("Error");
                            alertdialog.setMessage("Please Enter All Fields");
                                alertdialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();





                                    }
                                });
                            alertdialog.show();
                        }
                        //done checking


                        //adding contact_text and number_text to database
                        else if (!contact_text.isEmpty() || !number_text.isEmpty()){


                            ContentValues values = new ContentValues();
                            values.put("NAME",contact_text);
                            values.put("NUMBER",number_text);

                            db.insert("CONTACTS",null,values);

                            Intent intent = new Intent(TrustedActivity.this,TrustedActivity.class);
                            startActivity(intent);

                        }







                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });


                builder.show();

            }
        });
    }

    public void selectItem(int position) {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:
                Intent intent = new Intent(TrustedActivity.this, TrustedActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(TrustedActivity.this, ShortcutKeys.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(TrustedActivity.this, UserLocation2.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(TrustedActivity.this, HelpLineActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(TrustedActivity.this, MessageTemplates.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(TrustedActivity.this,EmailActivity.class);
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
                Intent intent = new Intent(TrustedActivity.this, MainDashBoard.class);
                startActivity(intent);
        }
        return true;
    }



}
