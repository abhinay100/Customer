package customer.apnacare.in.customer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;

/**
 * Created by root on 27/1/17.
 */

public class ViewPatientsActivity extends BaseActivity {

    @BindView(R.id.patientFirstName) EditText patientFirstName;
    @BindView(R.id.patientLastName) EditText patientLastName;
    @BindView(R.id.patientGender) EditText patientGender;
    @BindView(R.id.patientAge) EditText patientAge;
    @BindView(R.id.patientWeight) EditText patientWeight;
    @BindView(R.id.streetAddress) EditText streetAddress;
    @BindView(R.id.lblArea) EditText lblArea;
    @BindView(R.id.lblCity) EditText lblCity;
    @BindView(R.id.lblZipcode) EditText lblZipcode;
    @BindView(R.id.lblState) EditText lblState;

    @BindView(R.id.btnSaveAccount) Button btnSaveAccount;

    Realm realm;
    Patient patient;
    Context mContext;
    private ProgressDialog mDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_view);
        ButterKnife.bind(this);
        mContext = this;
        mDialog = new ProgressDialog(mContext);
        setUpNavigation("Edit Profile");
        realm = Realm.getDefaultInstance();

        try {
            patient = realm.where(Patient.class).findFirst();
            if(patient != null){
                patientFirstName.setText(patient.getFirstName());
                patientLastName.setText(patient.getLastName());
                patientGender.setText(patient.getGender());
                patientAge.setText(patient.getPatientAge());
                patientWeight.setText(patient.getPatientWeight());
                streetAddress.setText(patient.getStreetAddress());
                lblArea.setText(patient.getArea());
                lblCity.setText(patient.getCity());
                lblZipcode.setText(patient.getZipcode());
                lblState.setText(patient.getState());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @OnClick(R.id.btnSaveAccount)
    public void save(View v) {

        updatePatient();
        Snackbar.make(getCurrentFocus(),"Details saved successfully",Snackbar.LENGTH_SHORT).show();

        //this.onBackPressed();
    }

    public void updatePatient(){

        Patient patients = new Patient();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                patients.setId(patient.getId());
                patients.setFirstName(patientFirstName.getText().toString());
                patients.setLastName(patientLastName.getText().toString());
                patients.setGender(patientGender.getText().toString());
                patients.setPatientAge(patientAge.getText().toString());
                patients.setPatientWeight(patientWeight.getText().toString());
                patients.setStreetAddress(streetAddress.getText().toString());
                patients.setArea(lblArea.getText().toString());
                patients.setCity(lblCity.getText().toString());
                patients.setZipcode(lblZipcode.getText().toString());
                patients.setState(lblState.getText().toString());

                realm.copyToRealmOrUpdate(patients);
                showProgressBar("Updating Profile");

                Intent i = new Intent(ViewPatientsActivity.this, DataSyncService.class);
                i.putExtra("serviceName","updateProfile");
                i.putExtra("requestID",(patient.getId()));
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

                startActivity(new Intent(ViewPatientsActivity.this,MainActivity.class));
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

