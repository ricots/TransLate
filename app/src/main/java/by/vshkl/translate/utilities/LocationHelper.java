package by.vshkl.translate.utilities;

import android.content.Context;
import android.location.LocationManager;

public class LocationHelper {

    public static boolean hasLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
