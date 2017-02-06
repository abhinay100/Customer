package customer.apnacare.in.customer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.ui.activities.CitrusUIActivity;
import com.citrus.sdk.ui.fragments.ResultFragment;
import com.citrus.sdk.ui.utils.CitrusFlowManager;
import com.citrus.sdk.ui.utils.ResultModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.adapters.BillsListAdapter;
import customer.apnacare.in.customer.model.AppEnvironment;
import customer.apnacare.in.customer.model.Bills;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 27/12/16.
 */

public class BillsActivity extends BaseActivity {

    Context mContext;
    @BindView(R.id.billsList) RealmRecyclerView billsListRecycler;


    public static final String returnUrlLoadMoney = "https://beta.apnacare.in/gateway/redirectUrlLoadCash.html";
    SharedPreferences settings;

    private Realm realm;
    BillsListAdapter billsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        ButterKnife.bind(this);

        setUpNavigation("Payment Details");
        mContext = this;




        realm = Realm.getDefaultInstance();



//        realm.copyToRealmOrUpdate(bills);

        RealmResults<Bills> billsList = realm.where(Bills.class).findAll();
        billsListAdapter = new BillsListAdapter(this, billsList, true, true);
        billsListRecycler.setAdapter(billsListAdapter);

        billsListRecycler.setOnRefreshListener(new RealmRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent i = new Intent(mContext, DataSyncService.class);
                i.putExtra("serviceName", "getMyBills");
                startService(i);
            }
        });

        //setting citrus to production environment
        settings = getSharedPreferences("settings", MODE_PRIVATE);
        if (settings.getBoolean("is_prod_env", true)) {
            ((CustomerApp) getApplication()).setAppEnvironment(AppEnvironment.PRODUCTION);
        } else {
            ((CustomerApp) getApplication()).setAppEnvironment(AppEnvironment.SANDBOX);
        }


        setupCitrusConfigs();
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver DataSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String serviceName;
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                serviceName = intent.getStringExtra("serviceName");
                int resultValue = intent.getIntExtra("resultValue",2);

                hideProgressBar();

                billsListRecycler.setRefreshing(false);
                billsListAdapter.notifyDataSetChanged();
            }
        }
    };


    private void setupCitrusConfigs() {
        AppEnvironment appEnvironment = ((CustomerApp) getApplication()).getAppEnvironment();
        if (appEnvironment == AppEnvironment.PRODUCTION) {
//            Toast.makeText(BillsActivity.this, "Environment Set to Production", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(BillsActivity.this, "Environment Set to SandBox", Toast.LENGTH_SHORT).show();
        }
        CitrusFlowManager.initCitrusConfig(appEnvironment.getSignUpId(),
                appEnvironment.getSignUpSecret(), appEnvironment.getSignInId(),
                appEnvironment.getSignInSecret(), ContextCompat.getColor(this, R.color.white),
                BillsActivity.this, appEnvironment.getEnvironment(), appEnvironment.getVanity(), appEnvironment.getBillUrl(),
                returnUrlLoadMoney, false);
    }



    public void redirect(){
        if(CustomerApp.isConnectedToInternet){
            startActivity(new Intent(BillsActivity.this,MainActivity.class));
        }
    }

    @Override
    public void onResume() {
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(DataSyncService.ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(DataSyncReceiver, filter);
        // or `registerReceiver(DataSyncReceiver, filter)` for a normal broadcast
        hideProgressBar();
        super.onResume();
    }

    @Override
    public void onPause() {
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(DataSyncReceiver);
        // or `unregisterReceiver(DataSyncReceiver)` for a normal broadcast
        hideProgressBar();
        super.onPause();
    }


    @Override
    public void onDestroy(){
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(DataSyncReceiver);
        hideProgressBar();

        realm.close();
        realm = null;

        super.onDestroy();
    }

    @Override
    public void onStop(){
        hideProgressBar();

        super.onStop();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BillsActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == CitrusFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data !=
                null) {
            // You will get data here if transaction flow is started through pay options other than wallet
            TransactionResponse transactionResponse = data.getParcelableExtra(CitrusUIActivity
                    .INTENT_EXTRA_TRANSACTION_RESPONSE);
            // You will get data here if transaction flow is started through wallet
            ResultModel resultModel = data.getParcelableExtra(ResultFragment.ARG_RESULT);

            // Check which object is non-null
            if (transactionResponse != null && transactionResponse.getJsonResponse() != null) {
                // Decide what to do with this data
                Log.d(Constants.TAG, "transaction response" + transactionResponse.getJsonResponse());
            } else if (resultModel != null && resultModel.getTransactionResponse() != null) {
                // Decide what to do with this data
                Log.d(Constants.TAG, "result response" + resultModel.getTransactionResponse().getTransactionId());
            } else {
                Log.d(Constants.TAG, "Both objects are null!");
            }
        }
    }
}
