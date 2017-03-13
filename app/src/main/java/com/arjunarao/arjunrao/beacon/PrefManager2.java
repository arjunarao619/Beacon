package com.arjunarao.arjunrao.beacon;

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager2 {
    SharedPreferences pref1;
    SharedPreferences.Editor editor1;
    Context _context1;

    int PRIVATE_MODE1 = 0;
     private static final String PREF_NAME = "login-screen";
    private static final String IS_FIRST_TIME_LAUNCH = "Virgin1";

    public PrefManager2(Context context){
        this._context1 = context;
        pref1 = _context1.getSharedPreferences(PREF_NAME,PRIVATE_MODE1);
        editor1 = pref1.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime){
        editor1.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime);
        editor1.commit();
    }

    public boolean isFirstTimeLaunch(){
        return pref1.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }
}
