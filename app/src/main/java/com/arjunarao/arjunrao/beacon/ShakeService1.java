package com.arjunarao.arjunrao.beacon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;

import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;


/**
 * Created by Arjun Rao on 2/26/2017.
 */

public class ShakeService1 extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener{
    private SensorManager sensorManager;
    private long lastUpdate;
    SensorEventListener listen;

    MediaPlayer mediaPlayer;
    private String SENDTOTHISEMAIL;
    boolean is_alert,is_alarm,is_record;
    private SQLiteDatabase db, contactDb;
    String address, city, state, country, postalCode, knownName;
    private Cursor cursor, contactCursor;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final int PERMISSIONS_LOCATION = 0;
    double latitude,longitude,altitude,speed;
    protected LocationManager locationManager;
    String[] numbers = new String[10];
    private AudioManager myAudioManager;
    String user_name;



    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;
    public static final String BROADCAST = "com.example.arjunrao.beacon.android.action.ShutDownReciever";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        // TODO Auto-generated method stub
        sensorManager = (SensorManager) getApplicationContext()
                .getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        listen = new SensorListen();
        Sensor accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(listen, accel, SensorManager.SENSOR_DELAY_NORMAL);
        return START_STICKY;

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub

        Toast.makeText(getApplicationContext(), "The Beacon Service Has Started", Toast.LENGTH_LONG).show();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
      is_alert = preferences.getBoolean("location",false);
        is_alarm = preferences.getBoolean("alarm",false);
       /* is_record = preferences.getBoolean("record",false);*/
         user_name = preferences.getString("user_name","Someone");



        super.onCreate();
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot >= 40) //
        {
            if (actualTime - lastUpdate < 3000) {
                return;
            }
            lastUpdate = actualTime;
            ///////////////////////////////////////////////////////////





                if(is_alert){
                    mGoogleApiClient.connect();
                    Vibrator v = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                    v.vibrate(2000);

                }
            if(is_alarm){
                mediaPlayer = MediaPlayer.create(ShakeService1.this,R.raw.panic);
                myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                myAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, myAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                mediaPlayer.start();
            }



            ///////////////////////////////////////////////////////
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        sensorManager.unregisterListener(listen);
        Toast.makeText(this, "Destroyed Beacon Service", Toast.LENGTH_SHORT).show();
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public class SensorListen implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                getAccelerometer(event);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

    }



    @Override
    public void onConnected(Bundle bundle){
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        checkPermission(this);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

    }

    @Override
    public void onConnectionSuspended(int i) {


    }

    @Override
    public void onLocationChanged(Location location){




            latitude = location.getLatitude();
            longitude = location.getLongitude();
            altitude = location.getAltitude();
            speed = location.getSpeed();




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
                Toast.makeText(ShakeService1.this,"Error Determining Address. Switching to GPS",Toast.LENGTH_LONG).show();
                //startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }

            final String EMAIL_MESSAGE = user_name + " Has Activated Emergency. Location Has Been Captured" + "\n" +  "http://maps.google.com/maps?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + " \n" + "Location Details : " + "Address : " + address + "\n Altitude : " + altitude + " Speed : " + speed;
            final String SMS_MESSAGE = user_name + " Has Activated Emergency \n" + "http://maps.google.com/maps?q=" + String.valueOf(latitude) + "," + String.valueOf(longitude) +  " \nCheck Email For Address";



        Beacon_Database dbHelper = new Beacon_Database(this);
            db = dbHelper.getReadableDatabase();

            Beacon_Database helper = new Beacon_Database(this);
            contactDb = helper.getReadableDatabase();




                //SmsManager sm = SmsManager.getDefault();
                contactCursor = contactDb.query("CONTACTS",new String[] {"NAME","NUMBER"},null,null,null,null,null);

                Cursor countCursor;
                countCursor = contactDb.query("CONTACTS",new String[]{"NUMBER","COUNT (_id) AS count"},null,null,null,null,null);
                if(contactCursor.moveToFirst()) {
                    contactCursor.moveToFirst();
                    countCursor.moveToFirst();
                    final int no_of_contacts = Integer.valueOf(countCursor.getString(1)); //number of rows in the cursor


                    //retrieving each contact number
                    for (int i = 0; i < no_of_contacts; i++) {
                        numbers[i] = contactCursor.getString(1);
                        SmsManager sm = SmsManager.getDefault();
                        sm.sendTextMessage(numbers[i], null, SMS_MESSAGE, null, null);
                        contactCursor.moveToNext();
                    }
                }

        cursor = db.query("USEREMAIL", new String[]{"USER_EMAIL"}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            SENDTOTHISEMAIL = cursor.getString(0);
            sendEmail(SENDTOTHISEMAIL, "EMERGENCY MODE ACTIVATED", EMAIL_MESSAGE);
        }





        mGoogleApiClient.disconnect();


    }

    public static boolean checkPermission(final Context context) {
        return ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    private void sendEmail(String email,String subject,String message) {
        //Getting content for email


        //Creating SendMail object
        SendMail1 sm = new SendMail1(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }



}
