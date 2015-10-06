package net.exkazuu.mimicdance.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArduinoManager {
    private static final String TAG = "ArduinoManager";

    // 本アプリを認識するためのインテントアクション名
    private static final String ACTION_USB_PERMISSION
        = "net.exkazuu.mimicdance.action.USB_PERMISSION";
    private static UsbStateChangeReceiver receiver = new UsbStateChangeReceiver();

    private static UsbManager usbManager;
    private static ParcelFileDescriptor fileDescriptor;
    private static FileOutputStream outputStream;// 出力用ストリーム
    private static UsbAccessory pluggedAccessory;
    private static PendingIntent permissionIntent;
    private static boolean permissionRequestPending;
    private static Context context;

    private ArduinoManager() {
    }

    public static void register(Activity activity) {
        usbManager = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        permissionIntent = PendingIntent.getBroadcast(activity, 0, new Intent(ACTION_USB_PERMISSION), 0);

        // 受信するインテントの種類を登録
        IntentFilter filter = new IntentFilter();
        // USBアクセサリが接続／切断されたときのインテント
        filter.addAction(ACTION_USB_PERMISSION);
        // USBアクセサリが切断された（取り外された）ときのインテント
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);

        activity.registerReceiver(receiver, filter);
        context = activity.getApplicationContext();

//        Toast toast = Toast.makeText(context, "registered", Toast.LENGTH_SHORT);
//        toast.show();
    }

    public static void unregister(Activity activity) {
        closeAccessory();
        activity.unregisterReceiver(receiver);
//        Toast toast = Toast.makeText(context, "unregistered", Toast.LENGTH_SHORT);
//        toast.show();

    }

    public static void resume() {
        // 未初期化の場合は何もしない
        if (usbManager == null) {
            return;
        }

        // 既に通信が開始していたら何もしない
        if (outputStream != null) {
            return;
        }
        Toast toast0 = Toast.makeText(context, "onResume0 ", Toast.LENGTH_SHORT);
        toast0.show();


        // 接続されているUSBアクセサリの確認
        UsbAccessory[] accessories = usbManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory == null) {
            Toast toast = Toast.makeText(context, "accessory was not found.", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        Toast toast = Toast.makeText(context, "onResume: " + usbManager.hasPermission(accessory), Toast.LENGTH_SHORT);
        toast.show();

        // USBアクセサリにアクセスする権限があるかチェック
        if (usbManager.hasPermission(accessory)) {
            openAccessory(accessory);
        } else {
            synchronized (receiver) {
                if (!permissionRequestPending) {
                    // パーミッションを依頼
                    usbManager.requestPermission(accessory, permissionIntent);
                    permissionRequestPending = true;
                }
            }
        }
    }

    public static void pause() {
        closeAccessory();

        Toast toast = Toast.makeText(context, "onPause", Toast.LENGTH_SHORT);
        toast.show();
    }

    public static boolean isPlugged() {
        return outputStream != null;
    }

    /**
     * USBアクセサリへの出力ストリームを開く。
     *
     * @param accessory
     */
    private static void openAccessory(UsbAccessory accessory) {
        // USBアクセサリにアクセスするためのファイルディスクリプタを取得
        fileDescriptor = usbManager.openAccessory(accessory);
        if (fileDescriptor == null) {
            return;
        }

        // 入出力用のストリームを作成
        FileDescriptor fd = fileDescriptor.getFileDescriptor();
        outputStream = new FileOutputStream(fd);

        pluggedAccessory = accessory;
//        Toast toast = Toast.makeText(context, "USB accessory is opened.", Toast.LENGTH_SHORT);
//        toast.show();
    }

    /**
     * USBアクセサリへの出力ストリームを閉じる。
     */
    private static void closeAccessory() {
        try {
            if (fileDescriptor != null) {
                fileDescriptor.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
        } finally {
            fileDescriptor = null;
            outputStream = null;
            pluggedAccessory = null;
        }
//        Toast toast = Toast.makeText(context, "USB accessory is closed.", Toast.LENGTH_SHORT);
//        toast.show();
    }

    /**
     * 出力：Androidアプリ（USBアクセサリ）-> Arduino（USBホスト）
     *
     * @param command
     */
    public static void sendCommand(byte[] command) {
        if (outputStream == null) {
            return;
        }
        try {
            outputStream.write(command);
        } catch (IOException e) {
            Log.e(TAG, "Write failed", e);
        }
    }

    private static class UsbStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            Toast toast = Toast.makeText(context, action, Toast.LENGTH_SHORT);
            toast.show();

            if (ACTION_USB_PERMISSION.equals(action)) {
                // USBアクセサリが接続して接続許可を得られた場合
                synchronized (this) {
                    // Intent からアクセサリを取得
                    UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);

                    // 接続許可ダイアログで OK=true, Cancel=false のどちらを押したか
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        openAccessory(accessory);
                    } else {
                        Log.d(TAG, "Permission denied for accessory: " + accessory);
                    }

                    permissionRequestPending = false;
                }
            } else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                // USBアクセサリが外された場合
                UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                // 接続中のUSBアクセサリか？
                if (accessory != null && accessory.equals(pluggedAccessory)) {
                    // 接続中のUSBアクセサリならストリームを閉じる
                    closeAccessory();
                }
            }
        }
    }
}
