package com.anns.appnghenhacso.Screen;

import android.content.*;
import android.os.IBinder;
import android.widget.Toast;

import com.anns.appnghenhacso.API.MusicService;

import static android.content.Context.MODE_PRIVATE;

public class NotificationReceiver extends BroadcastReceiver {
    SharedPreferences pref;
    String value;

    @Override
    public void onReceive(Context context, Intent intent) {
        pref = context.getSharedPreferences("PREF", MODE_PRIVATE);
        value = pref.getString("@Arr_Mini", null);

        Intent serviceStartIntent = new Intent(context, MusicService.class);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case "ActionPrev":
                    serviceStartIntent.putExtra("myActionName", intent.getAction());
                    context.startService(serviceStartIntent);
                    break;
                case "ActionPlay":
                    serviceStartIntent.putExtra("myActionName", intent.getAction());
                    context.startService(serviceStartIntent);
                    break;
                case "ActionPause":
                    serviceStartIntent.putExtra("myActionName", intent.getAction());
                    context.startService(serviceStartIntent);
                    break;
                case "ActionNext":
                    serviceStartIntent.putExtra("myActionName", intent.getAction());
                    context.startService(serviceStartIntent);
                    break;
            }
        }
    }
}
