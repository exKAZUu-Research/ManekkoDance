package net.exkazuu.mimicdance;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.deploygate.sdk.DeployGate;

public class App extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DeployGate.install(this);
    }

    /**
     * バージョンコードを取得する
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        int versionCode = 0;
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionCode;
    }

    /**
     * バージョン名を取得する
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        String versionName = "";
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionName;
    }
}
