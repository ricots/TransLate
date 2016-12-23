package by.vshkl.translate.activities;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import by.vshkl.translate.R;
import by.vshkl.translate.receivers.NetworkAndLocationStateReceiver;
import by.vshkl.translate.utilities.BroadcastReceiverHelper;
import by.vshkl.translate.utilities.CookieHelper;
import by.vshkl.translate.utilities.LocationHelper;
import by.vshkl.translate.utilities.NetworkHelper;
import by.vshkl.translate.utilities.PermissionsHelper;

public class MapActivity extends AppCompatActivity
        implements NetworkAndLocationStateReceiver.NetworkAndLocationStateReceiverCallback {

    private static final int REQUEST_CODE = 42;
    private static final String URL_BASE = "http://www.minsktrans.by";
    private static final String URL_SCOREBOARD = "http://www.minsktrans.by/lookout_yard/Data/Scoreboard";
    private static final String URL_MAP = "http://www.minsktrans.by/lookout_yard/Home/Index/minsk?neareststops";

    private FrameLayout rootView;
    WebView wvMap;
    ImageButton btnLocation;
    private AVLoadingIndicatorView pbLoading;

    private boolean hasSavedState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        rootView = (FrameLayout) findViewById(R.id.root_view_map);
        wvMap = (WebView) findViewById(R.id.wv_map);
        btnLocation = (ImageButton) findViewById(R.id.btn_location);
        pbLoading = (AVLoadingIndicatorView) findViewById(R.id.pb_loading);

        hasSavedState = savedInstanceState != null;

        checkNetworkAndLocation();
        enableBroadcastReceiver();

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkAndLocation();
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        wvMap.restoreState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        wvMap.saveState(outState);
    }

    @Override
    protected void onDestroy() {
        disableBroadcastReceiver();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    initializeWebView();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        if (wvMap.canGoBack()) {
            wvMap.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStateChangeReceived() {
        checkNetworkAndLocation();
    }

    private void initializeWebView() {
        CookieManager.getInstance().setCookie(URL_BASE, CookieHelper.getCookies(getApplicationContext()));
        wvMap.getSettings().setJavaScriptEnabled(true);
        wvMap.getSettings().setGeolocationEnabled(true);
        wvMap.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }
        });
        wvMap.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        if (!hasSavedState) {
            wvMap.loadUrl(URL_MAP);
        }
    }

    private void checkPermissionsAndShowMap() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!PermissionsHelper.hasLocationPermissions(MapActivity.this)) {
                if (PermissionsHelper.shouldShowRationale(MapActivity.this)) {
                    showRationale();
                } else {
                    PermissionsHelper.requestLocationPermissions(MapActivity.this, REQUEST_CODE);
                }
            } else {
                showLoading();
                initializeWebView();
            }
        } else {
            showLoading();
            initializeWebView();
        }
    }

    private void showRationale() {
        Snackbar.make(rootView, getString(R.string.location_permissions_rationale), Snackbar.LENGTH_INDEFINITE)
                .setAction(android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PermissionsHelper.requestLocationPermissions(MapActivity.this, REQUEST_CODE);
                    }
                })
                .show();
    }

    private void enableBroadcastReceiver() {
        BroadcastReceiverHelper.enableBroadcastReceiver(this);
        NetworkAndLocationStateReceiver.setCallback(this);
    }

    private void disableBroadcastReceiver() {
        BroadcastReceiverHelper.disableBroadcastReceiver(this);
        NetworkAndLocationStateReceiver.removeCallback();
    }

    void showLoading() {
        wvMap.setVisibility(View.GONE);
        btnLocation.setVisibility(View.GONE);
        pbLoading.show();
        pbLoading.setVisibility(View.VISIBLE);
    }

    void hideLoading() {
        pbLoading.hide();
        pbLoading.setVisibility(View.GONE);
        wvMap.setVisibility(View.VISIBLE);
        btnLocation.setVisibility(View.VISIBLE);
    }

    void checkNetworkAndLocation() {
        FrameLayout emptyView = (FrameLayout) findViewById(R.id.empty_view_map);
        TextView tvAlertMessage = (TextView) findViewById(R.id.tv_alert_message);

        boolean hasNetwork = NetworkHelper.hasNetworkConnection(MapActivity.this);
        boolean hasLocation = LocationHelper.hasLocationEnabled(MapActivity.this);

        if (hasNetwork && hasLocation) {
            emptyView.setVisibility(View.GONE);
            wvMap.setVisibility(View.VISIBLE);
            btnLocation.setVisibility(View.VISIBLE);
            checkPermissionsAndShowMap();
        } else {
            wvMap.setVisibility(View.GONE);
            btnLocation.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            if (!hasNetwork && !hasLocation) {
                tvAlertMessage.setText(getString(R.string.message_template_both,
                        getString(R.string.message_network_needed), getString(R.string.message_location_needed)));
            } else if (!hasNetwork) {
                tvAlertMessage.setText(getString(R.string.message_template_one,
                        getString(R.string.message_network_needed)));
            } else {
                tvAlertMessage.setText(getString(R.string.message_template_one,
                        getString(R.string.message_location_needed)));
            }
        }
    }
}
