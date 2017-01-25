package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;

/**
 * Created by root on 27/12/16.
 */

public class PatientsActivity extends BaseActivity {

    Realm realm;
    Context mContext;
    Bundle bundle;
    long caseID;
    CaseRecord caseRecord;
    Patient patient;
    @BindView(R.id.firstName) TextView lblFirstName;
    @BindView(R.id.lastName) TextView lblLastName;
    @BindView(R.id.gender) TextView lblGender;
    @BindView(R.id.patientAge) TextView lblPatientAge;
    @BindView(R.id.patientWeight) TextView lblPatientWeight;
    @BindView(R.id.streetAddress) TextView lblStreetAddress;
    @BindView(R.id.city) TextView lblCity;
    @BindView(R.id.state) TextView lblState;
    @BindView(R.id.zipcode) TextView lblZipCode;
    @BindView(R.id.country) TextView lblCountry;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_profile);
        ButterKnife.bind(this);
        setUpNavigation("Patients Profile");

        realm = Realm.getDefaultInstance();

//        Bundle extras = getIntent().getExtras();
//        caseID = extras.getLong("caseID");
        try {
            //caseRecord = realm.where(CaseRecord.class).equalTo("id", 12544).findFirst();
            patient = realm.where(Patient.class).findFirst();



        }catch (NullPointerException e){
            e.printStackTrace();
        }
//        Log.v(Constants.TAG,"hiiii "+ caseID);

        lblFirstName.setText(patient.getFirstName());
        lblLastName.setText(patient.getLastName());
        lblGender.setText(patient.getGender());
        lblPatientAge.setText(patient.getPatientAge());
        lblPatientWeight.setText(patient.getPatientWeight());
        lblStreetAddress.setText(patient.getStreetAddress());
        lblCity.setText(patient.getCity());
        lblState.setText(patient.getState());
        lblZipCode.setText(patient.getZipcode());
        lblCountry.setText(patient.getArea());


    }
}
