package by.vshkl.translate.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import by.vshkl.translate.receivers.NetworkAndLocationStateReceiver;

public class BroadcastReceiverHelper {

    public static void enableBroadcastReceiver(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName receiverComponent = new ComponentName(context, NetworkAndLocationStateReceiver.class);
        packageManager.setComponentEnabledSetting(receiverComponent,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static void disableBroadcastReceiver(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName receiverComponent = new ComponentName(context, NetworkAndLocationStateReceiver.class);
        packageManager.setComponentEnabledSetting(receiverComponent,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }
}
