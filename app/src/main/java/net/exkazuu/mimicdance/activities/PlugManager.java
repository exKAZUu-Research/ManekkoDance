package net.exkazuu.mimicdance.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class PlugManager {
    private static PlugStateChangeReceiver receiver = new PlugStateChangeReceiver();
    private static IntentFilter plugIntentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private static boolean isPlugged = false;

    private PlugManager() {
    }

    public static void register(Activity activity) {
        activity.registerReceiver(receiver, plugIntentFilter);
    }

    public static void unregister(Activity activity) {
        activity.unregisterReceiver(receiver);
    }

    public static boolean isPlugged() {
        return isPlugged;
    }

    private static class PlugStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // plug状態を取得
            if (intent.getIntExtra("state", 0) > 0) {
                isPlugged = true;
            } else {
                isPlugged = false;
            }
        }
    }
}
