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
import customer.apnacare.in.customer.model.Patient;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_view);
        ButterKnife.bind(this);
        mContext = this;
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
        Snackbar.make(getCurrentFocus(),"Details saved successfully",Snackbar.LENGTH_SHORT).show();

        //this.onBackPressed();
    }
}
