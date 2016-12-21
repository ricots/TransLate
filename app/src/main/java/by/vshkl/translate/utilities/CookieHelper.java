package by.vshkl.translate.utilities;

import android.content.Context;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import by.vshkl.translate.BuildConfig;
import by.vshkl.translate.R;

public class CookieHelper {

    public static void putCookie(Context context, String cookie) {
        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context)
                .withEncryptionPassword(BuildConfig.ENCRYPTED_PREFERENCES_PWD)
                .build();
        preferences.edit().putString(context.getString(R.string.pref_key_cookies), cookie).apply();
    }

    public static boolean hasCookie(Context context) {
        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context)
                .withEncryptionPassword(BuildConfig.ENCRYPTED_PREFERENCES_PWD)
                .build();
        return preferences.contains(context.getString(R.string.pref_key_cookies));
    }

    public static String getCookies(Context context) {
        EncryptedPreferences preferences = new EncryptedPreferences.Builder(context)
                .withEncryptionPassword(BuildConfig.ENCRYPTED_PREFERENCES_PWD)
                .build();
        return preferences.getString(context.getString(R.string.pref_key_cookies), "");
    }
}
