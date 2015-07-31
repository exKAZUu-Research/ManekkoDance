package net.exkazuu.mimicdance.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class PlugStateChangeReceiver extends BroadcastReceiver {
    private static IntentFilter plugIntentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private static PlugStateChangeReceiver instance = null;

    private static boolean isPlugged = false;

    private static PlugStateChangeReceiver getInstance() {
        if (instance == null) instance = new PlugStateChangeReceiver();
        return instance;
    }

    public static void register(Activity activity) {
        activity.registerReceiver(getInstance(), plugIntentFilter);
    }

    public static void unregister(Activity activity) {
        activity.unregisterReceiver(getInstance());
    }

    public static boolean isPlugged() {
        return isPlugged;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // plug状態を取得
        if (intent.getIntExtra("state", 0) > 0) {
            isPlugged = true;
        } else isPlugged = false;

        // plug状態でメッセージを変更。
        if (isPlugged) {
            // ヘッドセットが挿された

        } else {
            // ヘッドセットが抜かれた

        }
    }

}
