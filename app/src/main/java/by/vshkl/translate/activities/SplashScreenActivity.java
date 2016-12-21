package by.vshkl.translate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import by.vshkl.translate.utilities.CookieHelper;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CookieHelper.hasCookie(getApplicationContext())) {
            startActivity(new Intent(SplashScreenActivity.this, MapActivity.class));
            finish();
        } else {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        }
    }
}
