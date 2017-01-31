package customer.apnacare.in.customer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {

    FloatingActionButton newRequestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        checkPermissions();

        newRequestBtn = (FloatingActionButton) findViewById(R.id.btnNewRequest);
        newRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NewRequestActivity.class));
            }
        });
        setUpNavigation("Home");
    }

    // Launching the Case loading service
    public void loadCaseData() {
        showProgressBar("Fetching Cases");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "loadCases");
        i.putExtra("type","All");
        startService(i);
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

                if(CustomerApp.isConnectedToInternet){

                }else{
                    Toast.makeText(mContext,"Could not connect to network!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void openActivity(View v){
        //Log.v(Constants.TAG,"view id: "+v.getId());
        int id = v.getId();

        switch (id){
            case R.id.cardCases: startActivity(new Intent(MainActivity.this,CasesActivity.class)); break;
            case R.id.cardNewRequest: startActivity(new Intent(MainActivity.this,NewRequestActivity.class)); break;
            case R.id.cardProfiles: startActivity(new Intent(MainActivity.this,PatientsActivity.class)); break;
            case R.id.cardTracking: startActivity(new Intent(MainActivity.this,LiveTrackingActivity.class)); break;
            case R.id.cardBills: startActivity(new Intent(MainActivity.this,BillsActivity.class)); break;
            case R.id.cardAccount: startActivity(new Intent(MainActivity.this,AccountActivity.class)); break;
            case R.id.cardShop: startActivity(new Intent(MainActivity.this,EshopActivity.class)); break;
        }
    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
