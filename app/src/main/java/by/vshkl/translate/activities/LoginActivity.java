package by.vshkl.translate.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import by.vshkl.translate.R;
import by.vshkl.translate.receivers.NetworkAndLocationStateReceiver;
import by.vshkl.translate.utilities.BroadcastReceiverHelper;
import by.vshkl.translate.utilities.CookieHelper;
import by.vshkl.translate.utilities.NetworkHelper;

public class LoginActivity extends AppCompatActivity
        implements NetworkAndLocationStateReceiver.NetworkAndLocationStateReceiverCallback {

    private static final String URL_LOGGED_IN = "http://www.minsktrans.by/lookout_yard/";
    private static final String URL_LOGIN = "http://www.minsktrans.by/lookout_yard/Account/Login";

    private WebView wvLogin;
    private AVLoadingIndicatorView pbLoading;

    private boolean hasSavedState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wvLogin = (WebView) findViewById(R.id.wv_login);
        pbLoading = (AVLoadingIndicatorView) findViewById(R.id.pb_loading);

        hasSavedState = savedInstanceState != null;

        checkNetwork();
        enableBroadcastReceiver();
    }

    @Override
    protected void onDestroy() {
        disableBroadcastReceiver();
        super.onDestroy();
    }

    @Override
    public void onStateChangeReceived() {
        checkNetwork();
    }

    private void initializeWebView() {
        if (Build.VERSION.SDK_INT >= 19) {
            wvLogin.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            wvLogin.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        wvLogin.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hideLoading();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                CookieHelper.putCookie(getApplicationContext(), CookieManager.getInstance().getCookie(url));
                if (url.equals(URL_LOGGED_IN)) {
                    startActivity(new Intent(LoginActivity.this, MapActivity.class));
                    finish();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        wvLogin.setWebChromeClient(new WebChromeClient());
        if (!hasSavedState) {
            wvLogin.loadUrl(URL_LOGIN);
        }
    }

    private void enableBroadcastReceiver() {
        BroadcastReceiverHelper.enableBroadcastReceiver(this);
        NetworkAndLocationStateReceiver.setCallback(this);
    }

    private void disableBroadcastReceiver() {
        BroadcastReceiverHelper.disableBroadcastReceiver(this);
        NetworkAndLocationStateReceiver.removeCallback();
    }

    private void showLoading() {
        wvLogin.setVisibility(View.GONE);
        pbLoading.show();
    }

    private void hideLoading() {
        pbLoading.hide();
        wvLogin.setVisibility(View.VISIBLE);
    }

    private void checkNetwork() {
        FrameLayout emptyView = (FrameLayout) findViewById(R.id.empty_view_login);
        TextView tvAlertMessage = (TextView) findViewById(R.id.tv_alert_message);

        if (NetworkHelper.hasNetworkConnection(LoginActivity.this)) {
            emptyView.setVisibility(View.GONE);
            wvLogin.setVisibility(View.VISIBLE);
            showLoading();
            initializeWebView();
        } else {
            wvLogin.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
            tvAlertMessage.setText(getString(R.string.message_template_one,
                    getString(R.string.message_network_needed)));
        }
    }

}
