package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.adapters.PatientsListAdapter;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 27/12/16.
 */

public class PatientsActivity extends BaseActivity {

    Realm realm;
    @BindView(R.id.patientList) RealmRecyclerView patientListRecycler;
    PatientsListAdapter patientsListAdapter;



    Patient patient;
//    @BindView(R.id.firstName) TextView lblFirstName;
//    @BindView(R.id.lastName) TextView lblLastName;
//    @BindView(R.id.gender) TextView lblGender;
//    @BindView(R.id.patientAge) TextView lblPatientAge;
//    @BindView(R.id.patientWeight) TextView lblPatientWeight;
//    @BindView(R.id.streetAddress) TextView lblStreetAddress;
//    @BindView(R.id.city) TextView lblCity;
//    @BindView(R.id.state) TextView lblState;
//    @BindView(R.id.zipcode) TextView lblZipCode;
//    @BindView(R.id.country) TextView lblCountry;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_profile);
        ButterKnife.bind(this);
        setUpNavigation("Patients Profile");

        realm = Realm.getDefaultInstance();

        RealmResults<CaseRecord> patientsList = realm.where(CaseRecord.class).findAll();
        Log.v(Constants.TAG,"patientsList size: "+patientsList);

        patientsListAdapter = new PatientsListAdapter(this,patientsList,true,true);
        patientListRecycler.setAdapter(patientsListAdapter);

//        try {
//            patient = realm.where(Patient.class).findFirst();
//
//            if(patient != null){
//                lblFirstName.setText(patient.getFirstName());
//                lblLastName.setText(patient.getLastName());
//                lblGender.setText(patient.getGender());
//                lblPatientAge.setText(patient.getPatientAge());
//                lblPatientWeight.setText(patient.getPatientWeight());
//                lblStreetAddress.setText(patient.getStreetAddress());
//                lblCity.setText(patient.getCity());
//                lblState.setText(patient.getState());
//                lblZipCode.setText(patient.getZipcode());
//                lblCountry.setText(patient.getArea());
//            }
//
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
    }
}
