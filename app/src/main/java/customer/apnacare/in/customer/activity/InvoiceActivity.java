package customer.apnacare.in.customer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.utils.WebViewClientImpl;

/**
 * Created by root on 6/2/17.
 */

public class InvoiceActivity extends BaseActivity {

    int invoiceId;

    Context context;
    WebView mWebView;


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

        ProgressDialog pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pDialog.dismiss();
            }
        });

        String pdfURL = "https://beta.apnacare.in/api/v1/customer/invoice?invoice_id=" + invoiceId;
        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfURL);
        setContentView(mWebView);
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
