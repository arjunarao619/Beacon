package com.arjunarao.arjunrao.beacon;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

public class MainDashBoard extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {


    private String[] titles;
    private ListView drawerList;
    final Context context = this;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference myPref = (Preference) findPreference("contacts");
        myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(MainDashBoard.this, TrustedActivity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference myPref1 = (Preference) findPreference("email");
        myPref1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(MainDashBoard.this, EmailActivity.class);
                startActivity(intent);
                return true;
            }
        });

        Preference myPref10 = (Preference) findPreference("about_dev");
        myPref10.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainDashBoard.this);
                builder.setTitle("Developer");
                builder.setMessage("NAME : Arjun Rao \n EMAIL : arjunarao619@gmail.com");
                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                return true;
            }
        });





        ListPreference styles = (ListPreference) findPreference("map_style");

            styles.setValueIndex(5);




    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("user_name")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }

        if (key.equals("map_style")) {
            Preference connectionPref = findPreference(key);

            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }
        if (key.equals("location")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }

        if (key.equals("alarm")) {
            Preference connectionPref = findPreference(key);
            connectionPref.setDefaultValue("2");
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }

        if (key.equals("record")) {
            Preference connectionPref = findPreference(key);
            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(sharedPreferences.getString(key, ""));
        }

    }

    public void promptRestart(){
        AlertDialog.Builder alertDialog =   new AlertDialog.Builder(MainDashBoard.this);
        alertDialog.setTitle("RESTART APP");
        alertDialog.setMessage("Restart Application to Save Changes?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent mStartActivity = new Intent(context, SplashScreenActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MainDashBoard.this,UserLocation2.class));
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }
    @Override
    public void onPause(){
       super.onPause();

    }

    @Override
    public void onBackPressed() {
        promptRestart();

    }
}