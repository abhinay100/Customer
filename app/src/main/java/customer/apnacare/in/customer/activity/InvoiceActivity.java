package customer.apnacare.in.customer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.utils.Constants;

/**
 * Created by root on 6/2/17.
 */

public class InvoiceActivity extends BaseActivity {

    int invoiceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        Bundle extras = getIntent().getExtras();
        invoiceId = extras.getInt("invoiceId");

        Log.v(Constants.TAG,"invoiceId: "+invoiceId);

        WebView webView = new WebView(InvoiceActivity.this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);


        String pdfURL = "https://beta.apnacare.in/api/v1/customer/invoice?invoice_id=" + invoiceId;
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);
        setContentView(webView);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }
    }
}
