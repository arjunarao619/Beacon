package com.example.arjunrao.beacon;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailActivity extends AppCompatActivity {


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

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher; //validate email
    private final String EMAIL_MESSAGE = "Your Email Address Has been successfully set up! \n \n \n Your Email ID can be used in Case Of Any Emergency, To Contact Your Trusted Contacts  \n \n \n Now, You May Be Able To Send Email Alerts In case of an Emergency \n \n \n *This is just a test Email. Please Do not Reply* ";
    SQLiteDatabase db;
    private Cursor cursor;
    ImageButton email_validate;
    public static String SENDTOTHISEMAIL;
    private String[] titles;
    private ListView drawerList;
    final Context context = this;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    public String existing_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Email Settings");
        }
        if(getSupportActionBar() !=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        getSupportActionBar().setHomeButtonEnabled(true);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


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
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

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


        Beacon_Database emailSender = new Beacon_Database(this);
        db = emailSender.getReadableDatabase();
        cursor = db.query("USEREMAIL", new String[]{"USER_EMAIL"}, null, null, null, null, null);

        final TextInputLayout email = (TextInputLayout) findViewById(R.id.email_wrapper);
        email.setHint("Email");

        email_validate = (ImageButton) findViewById(R.id.verify_email);




        email_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                Boolean isvalid = validateEmail(email.getEditText().getText().toString()); //checking if email is valid

                if (!isvalid) {
                    email.setError("Email Address is not Valid");
                } else {
                    email.setErrorEnabled(false);
                    SENDTOTHISEMAIL = email.getEditText().getText().toString();
                    // Toast.makeText(EmailActivity.this,SENDTOTHISEMAIL,Toast.LENGTH_LONG).show();


                    cursor = db.query("USEREMAIL", new String[]{"USER_EMAIL"}, null, null, null, null, null);
                    if (cursor.moveToFirst()) {

                            cursor.moveToFirst();
                            existing_email = cursor.getString(0);

                        //ONE EMAIL ALDREADY EXISTS!

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmailActivity.this);
                        alertDialog.setTitle("Warning");
                        alertDialog.setMessage("This Will OverWrite Previous Email With New One.\n  Are Your Sure You Want to Continue?");
                        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                db.execSQL("DELETE FROM " + "USEREMAIL" + " WHERE " + "USER_EMAIL" + "= '" + existing_email + "'");
                                ContentValues values = new ContentValues();
                                values.put("USER_EMAIL", SENDTOTHISEMAIL);
                                db.insert("USEREMAIL", null, values);

                                /*Cursor debugCursor = db.query("USEREMAIL",new String[]{"USER_EMAIL"},null,null,null,null,null);
                                debugCursor.moveToFirst();
                                Toast.makeText(EmailActivity.this,debugCursor.getString(0),Toast.LENGTH_LONG).show();*/

                                sendEmail(SENDTOTHISEMAIL, "Welcome to Beacon!", EMAIL_MESSAGE);

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(EmailActivity.this);
                                        alertDialog.setTitle("Email Has Been Sent Succesfully");
                                        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();

                                            }
                                        });

                                        alertDialog.show();

                                        try {
                                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                            r.play();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 4500);


                            }
                        });
                        alertDialog.show();
                    } else if (!cursor.moveToFirst()) {
                        ContentValues values = new ContentValues();
                        values.put("USER_EMAIL", SENDTOTHISEMAIL);
                        db.insert("USEREMAIL", null, values);

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmailActivity.this);
                        alertDialog.setTitle("SUCCESS");
                        alertDialog.setMessage("EMAIL HAS BEEN SET UP. RETURNING...");
                        alertDialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(EmailActivity.this, UserLocation2.class);
                                startActivity(intent);
                            }
                        });
                        alertDialog.show();
                    }


                }
            }
        });


    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void sendEmail(String email, String subject, String message) {
        //Getting content for email


        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    public void selectItem(int position) {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:
                Intent intent = new Intent(EmailActivity.this, TrustedActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(EmailActivity.this, MainDashBoard.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(EmailActivity.this, UserLocation2.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(EmailActivity.this, HelpLineActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(EmailActivity.this, MessageTemplates.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(EmailActivity.this,EmailActivity.class);
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
                Intent intent = new Intent(EmailActivity.this, MainDashBoard.class);
                startActivity(intent);
        }
        return true;
    }



}