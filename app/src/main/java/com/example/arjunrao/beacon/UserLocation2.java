package com.example.arjunrao.beacon;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import java.util.List;
import java.util.Locale;



public class UserLocation2 extends AppCompatActivity  {



    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            mediaPlayer.stop();
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
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int countClick = 0;
    private MapView mapView;
    private MapboxMap map;
    private FloatingActionButton floatingActionButton;
    private LocationServices locationServices;
    double latitude,longitude;
    private Cursor cursor,contactCursor;
    private SensorManager sensorManager;
    String address,city,state,country,postalCode,knownName;
    private SQLiteDatabase db,contactDb;
    int PLACE_PICKER_REQUEST = 1;
    Button panicStop;
    private AudioManager myAudioManager;
    public String strAdd;
    ImageButton hospitals,sendLocation,policeStation;
    String[] numbers = new String[10];
    private static final int PERMISSIONS_LOCATION = 0;
    private String SENDTOTHISEMAIL;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private boolean SMS_SUCCESS = false;
    private boolean EMAIL_SUCCESS = false;
     MediaPlayer mediaPlayer;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // setupViewPager(viewPager);
        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        try {
            MapboxAccountManager.start(this, getString(R.string.access_token));
        } catch (Exception exc) {
            Toast.makeText(UserLocation2.this, "Error Determining Address. Using GPS", Toast.LENGTH_LONG).show();

        }



        setContentView(R.layout.activity_user_location2); ///////////////////////////////////////////////////////////////////////////////////////////

        Intent serviceIntent = new Intent(UserLocation2.this,ShakeService1.class);
        startService(serviceIntent);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                Toast.makeText(UserLocation2.this,"Shake Shake",Toast.LENGTH_LONG).show();
            }
        });

         mediaPlayer = MediaPlayer.create(UserLocation2.this,R.raw.panic);
        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Location Settings");
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Navigation_Drawer drawerItems[] = new Navigation_Drawer[]{
                new Navigation_Drawer(R.drawable.nav_contacts1, "Trusted Contacts"),
                new Navigation_Drawer(R.drawable.nav_emergency1, "Emergency Settings"),
                new Navigation_Drawer(R.drawable.nav_location1, "Your Location"),
                new Navigation_Drawer(R.drawable.nav_helpline1, "Helplines and Tips"),
                new Navigation_Drawer(R.drawable.nav_notifications1, "Audio Settings"),
                new Navigation_Drawer(R.drawable.nav_message1, "Email and SMS"),
                new Navigation_Drawer(R.drawable.nav_developer, "Developer"),

        };

        Drawer_adapter adapter = new Drawer_adapter(this, R.layout.listview_layout, drawerItems);
        drawerList = (ListView) findViewById(R.id.drawer);

        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new UserLocation2.DrawerItemClickListener());
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

        drawerLayout.setDrawerListener(drawerToggle);

        // floatingActionButton.performClick();

        panicStop = (Button) findViewById(R.id.panicStop);
        panicStop.setVisibility(View.GONE);

        Beacon_Database beacon_database = new Beacon_Database(this);
        db = beacon_database.getReadableDatabase();



        sendLocation = (ImageButton) findViewById(R.id.send_location);



        sendLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!haveNetworkConnection()) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UserLocation2.this);
                    dialog.setTitle("Error");
                    dialog.setMessage("Please Enable Network Connection");
                    dialog.setCancelable(false);
                    dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();

                } else {


                    cursor = db.query("USEREMAIL", new String[]{"USER_EMAIL"}, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        SENDTOTHISEMAIL = cursor.getString(0);




                        final String EMAIL_MESSAGE = "http://maps.google.com/maps?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "\n \n" + "Location Details : " + "Address : " + address;
                        if (EMAIL_MESSAGE.equals("http://maps.google.com/maps?q=0.0,0.0\n" +
                                "\n" +
                                "Location Details : Address : null")) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLocation2.this);
                            alertDialog.setTitle("ERROR");
                            alertDialog.setMessage("Check Your Network Connection or click on the Locate button");
                            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }

                       // sendEmail(SENDTOTHISEMAIL, "Location Notification", EMAIL_MESSAGE);

                        final SmsManager smsManager = SmsManager.getDefault();



                        Beacon_Database contactHelper = new Beacon_Database(UserLocation2.this);//for SMS
                        contactDb = contactHelper.getReadableDatabase();



                        contactCursor = contactDb.query("CONTACTS",new String[] {"NAME","NUMBER"},null,null,null,null,null);

                        Cursor countCursor;
                        countCursor = contactDb.query("CONTACTS",new String[]{"NUMBER","COUNT (_id) AS count"},null,null,null,null,null);
                        if(contactCursor.moveToFirst()){
                            contactCursor.moveToFirst();
                            countCursor.moveToFirst();
                            final int no_of_contacts = Integer.valueOf(countCursor.getString(1)); //number of rows in the cursor

                        //retrieving each contact number
                            for(int i=0;i<no_of_contacts;i++){
                                numbers[i] = contactCursor.getString(1);
                                contactCursor.moveToNext();
                            }



                            if (ContextCompat.checkSelfPermission(UserLocation2.this,
                                    android.Manifest.permission.SEND_SMS)
                                    != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(UserLocation2.this,
                                        android.Manifest.permission.SEND_SMS)) {
                                } else {
                                    ActivityCompat.requestPermissions(UserLocation2.this,
                                            new String[]{android.Manifest.permission.SEND_SMS},
                                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                                }
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <no_of_contacts; i++) {
                                       smsManager.sendTextMessage(numbers[i], null, EMAIL_MESSAGE, null, null);
                                    }
                                }},4500);


                            sendEmail(SENDTOTHISEMAIL, "Location Notification", EMAIL_MESSAGE);

                        }
                        else if(!contactCursor.moveToFirst()){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLocation2.this);
                            alertDialog.setTitle("Choose Trusted Contacts");
                            alertDialog.setMessage("Looks Like You Forgot To Add Trusted Contacts.");
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UserLocation2.this,TrustedActivity.class);
                                    startActivity(intent);

                                }
                            }).show();

                            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                        }




                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(UserLocation2.this);
                                alertDialog.setTitle("Location Has Been Sent Successfully");
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

                    } else {
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(UserLocation2.this);
                        alertdialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(UserLocation2.this, EmailActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertdialog.setTitle("EMAIL ERROR");
                        alertdialog.setMessage("LOOKS LIKE YOU FORGOT TO REGISTER A TRUSTED EMAIL WITH US");
                        alertdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        alertdialog.show();

                    }


                }
            }
        });

        final Button panicButton = (Button) findViewById(R.id.panicButton);
        panicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mediaPlayer.start();
                panicButton.setVisibility(View.GONE);
                panicStop.setVisibility(View.VISIBLE);
                panicStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mediaPlayer.stop();
                        panicStop.setVisibility(View.GONE);
                        panicButton.setVisibility(View.VISIBLE);
                    }
                });

                //////////////////////////////////////////////////////////////////////////////////////////////


                if (!haveNetworkConnection()) {
                    mediaPlayer.stop();
                    panicStop.setVisibility(View.GONE);
                    panicButton.setVisibility(View.VISIBLE);
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UserLocation2.this);
                    dialog.setTitle("Error");
                    dialog.setMessage("Please Enable Network Connection");

                    dialog.setCancelable(false);
                    dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        }
                    }).show();

                } else {


                    cursor = db.query("USEREMAIL", new String[]{"USER_EMAIL"}, null, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        cursor.moveToFirst();
                        SENDTOTHISEMAIL = cursor.getString(0);
                        EMAIL_SUCCESS = true;




                        final String EMAIL_MESSAGE = "http://maps.google.com/maps?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "\n \n" + "Location Details : " + "Address : " + address;
                        if (EMAIL_MESSAGE.equals("http://maps.google.com/maps?q=0.0,0.0\n" +
                                "\n" +
                                "Location Details : Address : null")) {
                            mediaPlayer.stop();
                            panicStop.setVisibility(View.GONE);
                            panicButton.setVisibility(View.VISIBLE);
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLocation2.this);
                            alertDialog.setTitle("ERROR");
                            alertDialog.setMessage("Check Your Network Connection or click on the Locate button");
                            panicStop.setVisibility(View.GONE);
                            alertDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alertDialog.show();
                        }

                        // sendEmail(SENDTOTHISEMAIL, "Location Notification", EMAIL_MESSAGE);

                        final SmsManager smsManager = SmsManager.getDefault();



                        Beacon_Database contactHelper = new Beacon_Database(UserLocation2.this);//for SMS
                        contactDb = contactHelper.getReadableDatabase();



                        contactCursor = contactDb.query("CONTACTS",new String[] {"NAME","NUMBER"},null,null,null,null,null);

                        Cursor countCursor;
                        countCursor = contactDb.query("CONTACTS",new String[]{"NUMBER","COUNT (_id) AS count"},null,null,null,null,null);
                        if(contactCursor.moveToFirst()){
                            contactCursor.moveToFirst();
                            countCursor.moveToFirst();
                            final int no_of_contacts = Integer.valueOf(countCursor.getString(1)); //number of rows in the cursor

                            //retrieving each contact number
                            for(int i=0;i<no_of_contacts;i++){
                                numbers[i] = contactCursor.getString(1);
                                contactCursor.moveToNext();
                            }



                            if (ContextCompat.checkSelfPermission(UserLocation2.this,
                                    android.Manifest.permission.SEND_SMS)
                                    != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(UserLocation2.this,
                                        android.Manifest.permission.SEND_SMS)) {
                                } else {
                                    ActivityCompat.requestPermissions(UserLocation2.this,
                                            new String[]{android.Manifest.permission.SEND_SMS},
                                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                                }
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i = 0; i <no_of_contacts; i++) {
                                        smsManager.sendTextMessage(numbers[i], null, EMAIL_MESSAGE, null, null);
                                        SMS_SUCCESS = true;
                                    }
                                }},6500);


                            sendEmail(SENDTOTHISEMAIL, "Location Notification", EMAIL_MESSAGE);
                            EMAIL_SUCCESS = true;

                        }
                        else if(!contactCursor.moveToFirst()){
                            mediaPlayer.stop();
                            panicStop.setVisibility(View.GONE);
                            panicButton.setVisibility(View.VISIBLE);
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLocation2.this);
                            alertDialog.setTitle("Choose Trusted Contacts");
                            alertDialog.setMessage("Looks Like You Forgot To Add Trusted Contacts.");
                            panicStop.setVisibility(View.GONE);
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(UserLocation2.this,TrustedActivity.class);
                                    startActivity(intent);

                                }
                            }).show();

                            alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();

                        }




                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(UserLocation2.this);
                                alertDialog.setTitle("Location Has Been Sent Successfully");
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

                    } else {
                        mediaPlayer.stop();
                        panicStop.setVisibility(View.GONE);
                        panicButton.setVisibility(View.VISIBLE);
                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(UserLocation2.this);
                        alertdialog.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(UserLocation2.this, EmailActivity.class);
                                startActivity(intent);
                            }
                        });
                        alertdialog.setTitle("EMAIL ERROR");
                        alertdialog.setMessage("LOOKS LIKE YOU FORGOT TO REGISTER A TRUSTED EMAIL WITH US");
                        alertdialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

                        alertdialog.show();

                    }


                }
                ///////////////////////////////////////////////////////////////////////////////////////////////////////////


            }
        });

        locationServices = LocationServices.getLocationServices(UserLocation2.this);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                map = mapboxMap;
            }
        });

         floatingActionButton = (FloatingActionButton) findViewById(R.id.location_toggle_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null) {

                    toggleGps(!map.isMyLocationEnabled());


                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();




    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void toggleGps(boolean enableGps) {
        if (enableGps) {
            // Check if user has granted location permission
            if (!locationServices.areLocationPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
            } else {
                enableLocation(true);
            }
        } else {
            enableLocation(true); //change made
        }
    }

    private void enableLocation(boolean enabled) {
        if (enabled) {
            // If we have the last location of the user, we can move the camera to that position.
            Location lastLocation = locationServices.getLastLocation();
            if (lastLocation != null) {

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 13));
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
                if (latitude == 0.0){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserLocation2.this);
                    alertDialog.setTitle("No Network Connection");
                    alertDialog.setMessage("Your Are Not Connected. Try Again..");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();


                }

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(this, Locale.getDefault());
                try{


                    addresses = geocoder.getFromLocation(latitude, longitude, 1);

                 // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                 address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                 city = addresses.get(0).getLocality();
                 state = addresses.get(0).getAdminArea();
                 country = addresses.get(0).getCountryName();
                 postalCode = addresses.get(0).getPostalCode();
                 knownName = addresses.get(0).getFeatureName();

                }catch(Exception exc){
                    Toast.makeText(UserLocation2.this,"Error Determining Address. Switching to GPS",Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }





                CameraPosition position = new CameraPosition.Builder()
                        .target(new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude())) // Sets the new camera position
                        .zoom(15) // Sets the zoom
                        .bearing(180) // Rotate the camera
                        .tilt(30) // Set the camera tilt
                        .build(); // Creates a CameraPosition from the builder

                map.animateCamera(CameraUpdateFactory
                        .newCameraPosition(position), 7000);
            }

            locationServices.addLocationListener(new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        // Move the map camera to where the user location is and then remove the
                        // listener so the camera isn't constantly updating when the user location
                        // changes. When the user disables and then enables the location again, this
                        // listener is registered again and will adjust the camera once again.


                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location),13));
                        locationServices.removeLocationListener(this);
                    }
                }
            });
            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);
        } else {
            floatingActionButton.setImageResource(R.drawable.ic_my_location_24dp);
        }
        // Enable or disable the location layer on the map
        map.setMyLocationEnabled(enabled);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation(true);
            }
        }
    }


    private void sendEmail(String email,String subject,String message) {
        //Getting content for email


        //Creating SendMail object
        SendMail1 sm = new SendMail1(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
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
                Intent intent = new Intent(UserLocation2.this, MainDashBoard.class);
                startActivity(intent);
        }
        return true;
        }






    public void selectItem(int position) {

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(drawerList);
        switch (position) {
            case 0:
                Intent intent = new Intent(UserLocation2.this, TrustedActivity.class);
                startActivity(intent);
                break;
            case 1:
                Intent intent1 = new Intent(UserLocation2.this, ShortcutKeys.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2 = new Intent(UserLocation2.this, UserLocation2.class);
                startActivity(intent2);
                break;
            case 3:
                Intent intent3 = new Intent(UserLocation2.this, HelpLineActivity.class);
                startActivity(intent3);
                break;
            case 4:
                Intent intent4 = new Intent(UserLocation2.this, Notifications.class);
                startActivity(intent4);
                break;
            case 5:
                Intent intent5 = new Intent(UserLocation2.this,EmailActivity.class);
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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }













}

