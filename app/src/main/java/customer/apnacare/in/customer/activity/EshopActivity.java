package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.utils.WebViewClientImpl;

/**
 * Created by root on 27/12/16.
 */

public class EshopActivity extends BaseActivity {

    Context context;
    WebView mWebView;
    WebSettings mWebSettings;
    WebViewClientImpl webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eshop);

        mContext = this;
        mWebView = (WebView) findViewById(R.id.webview);

        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        webViewClient = new WebViewClientImpl(this);
        mWebView.setWebViewClient(webViewClient);

        mWebView.loadUrl("https://apnacare-eshop.in");

        setUpNavigation("e-Shop");



    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
