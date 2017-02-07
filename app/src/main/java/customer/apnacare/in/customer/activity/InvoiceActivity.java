package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.WebViewClientImpl;

/**
 * Created by root on 6/2/17.
 */

public class InvoiceActivity extends BaseActivity {

    int invoiceId;

    Context context;
    WebView mWebView;
    WebSettings mWebSettings;
    WebViewClientImpl webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        mContext = this;

        setUpNavigation("Invoice");

        Bundle extras = getIntent().getExtras();
        invoiceId = extras.getInt("invoiceId");

        mWebView = (WebView) findViewById(R.id.webview);


        mWebView = new WebView(InvoiceActivity.this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);


        String pdfURL = "https://beta.apnacare.in/api/v1/customer/invoice?invoice_id=" + invoiceId;
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);
        setContentView( mWebView);



    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//        Intent i = new Intent(mContext,BillsActivity.class);
//        startActivity(i);
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.mWebView.canGoBack()) {
            this.mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
