package by.vshkl.translate.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkAndLocationStateReceiver extends BroadcastReceiver {

    private static NetworkAndLocationStateReceiverCallback callback;

    public NetworkAndLocationStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (callback != null) {
            callback.onStateChangeReceived();
        }
    }

    public interface NetworkAndLocationStateReceiverCallback {
        void onStateChangeReceived();
    }

    public static void setCallback(NetworkAndLocationStateReceiverCallback c) {
        callback = c;
    }

    public static void removeCallback() {
        callback = null;
    }
}
