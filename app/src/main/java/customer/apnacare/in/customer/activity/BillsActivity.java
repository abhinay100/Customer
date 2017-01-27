package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.os.Bundle;

import customer.apnacare.in.customer.R;

/**
 * Created by root on 27/12/16.
 */

public class BillsActivity extends BaseActivity {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);

        setUpNavigation("Bill Details");
        mContext = this;
    }
}
