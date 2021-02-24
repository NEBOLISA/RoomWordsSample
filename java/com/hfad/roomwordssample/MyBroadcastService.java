package com.hfad.roomwordssample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class MyBroadcastService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction != null){
            String toast =" unknown intent action";
            switch(intentAction){
                case Intent.ACTION_POWER_CONNECTED:
                    toast = "Power Connected";
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    toast = "Power Disconnected";
                    break;

            }
            Toast.makeText(context,toast,Toast.LENGTH_LONG).show();
        }
    }
}