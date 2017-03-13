package com.arjunarao.arjunrao.beacon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Arjun Rao on 3/9/2017.
 */

public class ShutDownReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
        //TODO SEND LOCATION HERE
        }

}
