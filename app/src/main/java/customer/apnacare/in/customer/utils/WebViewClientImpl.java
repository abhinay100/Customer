package customer.apnacare.in.customer.utils;

import android.app.Activity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by root on 27/12/16.
 */

public class WebViewClientImpl extends WebViewClient {

    private Activity activity = null;

    public WebViewClientImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        //if(url.indexOf("apnacare-eshop.in") > -1 || url.indexOf("apnacare.in") > -1 ) return false;
        return true;
    }


}
