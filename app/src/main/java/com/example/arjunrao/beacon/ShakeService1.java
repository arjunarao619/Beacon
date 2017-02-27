package com.example.arjunrao.beacon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import static com.example.arjunrao.beacon.EmailActivity.SENDTOTHISEMAIL;


/**
 * Created by Arjun Rao on 2/26/2017.
 */

public class ShakeService1 extends Service {
    private SensorManager sensorManager;
    private long lastUpdate;
    SensorEventListener listen;

    private boolean EMAIL_SUCCESS = false;
    MediaPlayer mediaPlayer;
    private String SENDTOTHISEMAIL;
    private SQLiteDatabase db, contactDb;
    String address, city, state, country, postalCode, knownName;
    private Cursor cursor, contactCursor;
    private static final int PERMISSIONS_LOCATION = 0;
    double latitude,longitude;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_LONG).show();
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
        if (accelationSquareRoot >= 7) //
        {
            if (actualTime - lastUpdate < 2000) {
                return;
            }
            lastUpdate = actualTime;
            ///////////////////////////////////////////////////////////
           // cursor = db.query("USEREMAIL", new String[]{"USER_EMAIL"}, null, null, null, null, null);

            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
             latitude = location.getLatitude();
            Toast.makeText(ShakeService1.this,String.valueOf(latitude) + ", "+String.valueOf(longitude),Toast.LENGTH_LONG).show();

            final LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    Toast.makeText(ShakeService1.this,String.valueOf(latitude) + ", "+String.valueOf(longitude),Toast.LENGTH_LONG).show();

                }


                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);







            Vibrator v = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
            v.vibrate(1000);
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
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
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


}
