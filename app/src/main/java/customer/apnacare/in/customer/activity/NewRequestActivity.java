package customer.apnacare.in.customer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.ServiceList;
import customer.apnacare.in.customer.model.ServiceRequest;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.realm.Realm;

/**
 * Created by root on 18/1/17.
 */

public class NewRequestActivity extends BaseActivity {

    Context mContext;
    EditText txtName, txtPhone, txtEmail;
    EditText txtArea, txtCity, txtState;
    Spinner careType;
    Button btnSubmitRequest;
    private ProgressDialog mDialog;
    ArrayList<ServiceList> serviceList;
    private int requestServiceID = 0;
    String name,phone,email,area,city,state;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrequest);
        mContext = this;

        realm = Realm.getDefaultInstance();

        mDialog = new ProgressDialog(mContext);

        setUpNavigation("New Service Request");

        serviceList = new ArrayList<>();
        serviceList.add(new ServiceList(0,"-- Select Service --"));
        serviceList.add(new ServiceList(1,"CLINIC VISIT"));
        serviceList.add(new ServiceList(2,"COUNSELOR"));
        serviceList.add(new ServiceList(3,"DENTAL CARE"));
        serviceList.add(new ServiceList(4,"EYE CARE"));
        serviceList.add(new ServiceList(5,"HOME VISIT - DOCTOR"));
        serviceList.add(new ServiceList(6,"HOME VISIT - NURSE"));
        serviceList.add(new ServiceList(7,"HOSPITALIZATION"));
        serviceList.add(new ServiceList(8,"LONG TERM NURSE"));
        serviceList.add(new ServiceList(9,"LONG TERM NURSE ASSISTANT"));
        serviceList.add(new ServiceList(10,"PHYSIOTHERAPY"));
        serviceList.add(new ServiceList(11,"SHORT TERM NURSE"));
        serviceList.add(new ServiceList(12,"SHORT TERM NURSE ASSISTANT"));
        serviceList.add(new ServiceList(13,"OLD AGE HOMES"));
        serviceList.add(new ServiceList(14,"BABY CARE"));
        serviceList.add(new ServiceList(15,"LAB TEST"));
        serviceList.add(new ServiceList(16,"PHARMACY"));
        serviceList.add(new ServiceList(17,"DIABETES SPECIALIST"));
        serviceList.add(new ServiceList(18,"AMBULANCE"));

        init();
    }

    public void init(){
        txtName = (EditText) findViewById(R.id.name);
        txtPhone = (EditText) findViewById(R.id.phone);
        txtEmail = (EditText) findViewById(R.id.email);
        txtArea = (EditText) findViewById(R.id.area);
        txtCity = (EditText) findViewById(R.id.city);
        txtState = (EditText) findViewById(R.id.state);

        // Fill the details from Preferences
        txtName.setText(CustomerApp.preferences.getString("fullName",""));
        txtPhone.setText(CustomerApp.preferences.getString("phoneNumber",""));
        txtEmail.setText(CustomerApp.preferences.getString("email",""));

        careType = (Spinner) findViewById(R.id.care_type);

        ArrayAdapter<ServiceList> adapter = new ArrayAdapter<ServiceList>(mContext, android.R.layout.simple_spinner_item, serviceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        careType.setAdapter(adapter);

        careType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ServiceList service = (ServiceList) adapterView.getSelectedItem();
                if(service.getId() != 0){
                    //Toast.makeText(mContext,service.getServiceName()+" "+service.getId(),Toast.LENGTH_SHORT).show();
                    requestServiceID = service.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmitRequest = (Button) findViewById(R.id.btnSubmitRequest);
        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRequest();
            }
        });
    }

    public void addRequest(){
        ServiceRequest request = new ServiceRequest();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                long id = 0;
                if(realm.where(ServiceRequest.class).max("id") != null){
                    id = realm.where(ServiceRequest.class).max("id").longValue();
                }

                request.setId((id + 1));
                request.setName(txtName.getText().toString());
                request.setPhoneNumber(txtPhone.getText().toString());
                request.setEmail(txtEmail.getText().toString());
                request.setArea(txtArea.getText().toString());
                request.setCity(txtCity.getText().toString());
                request.setState(txtState.getText().toString());
                request.setService(String.valueOf(requestServiceID));

                realm.copyToRealmOrUpdate(request);

                showProgressBar("Sending Service Request");

                Intent i = new Intent(NewRequestActivity.this, DataSyncService.class);
                i.putExtra("serviceName","newServiceRequest");
                i.putExtra("requestID",(id + 1));
                startService(i);
            }
        });
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

                startActivity(new Intent(NewRequestActivity.this,MainActivity.class));
            }
        }
    };

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
}
