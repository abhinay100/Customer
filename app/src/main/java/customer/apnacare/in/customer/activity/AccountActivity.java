package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.utils.CustomerApp;

/**
 * Created by root on 27/12/16.
 */

public class AccountActivity extends BaseActivity {

    @BindView(R.id.accountName) EditText accountName;
    @BindView(R.id.accountEmail) EditText accountEmail;
    @BindView(R.id.accountPhoneNumber) EditText accountPhoneNumber;

    @BindView(R.id.btnSaveAccount) Button btnSaveAccount;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
        mContext = this;

        setUpNavigation("My Profile");

        accountName.setText(CustomerApp.preferences.getString("fullName",""));
        accountEmail.setText(CustomerApp.preferences.getString("email",""));
        accountPhoneNumber.setText(CustomerApp.preferences.getString("phoneNumber",""));
    }

    @OnClick(R.id.btnSaveAccount)
    public void save(View v) {
        Snackbar.make(getCurrentFocus(),"Details saved successfully",Snackbar.LENGTH_SHORT).show();

        //this.onBackPressed();
    }
}
