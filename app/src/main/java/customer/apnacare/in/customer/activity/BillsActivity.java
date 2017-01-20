package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import io.realm.Realm;

/**
 * Created by root on 27/12/16.
 */

public class BillsActivity extends BaseActivity {

    Context mContext;
    Bundle bundle;
    private Realm realm;
    long caseID;
    Patient patient;
    CaseRecord caseRecord;

    TextView lblPatientName, lblPatientAge, lblPatientWeight, lblMedicalConditions, lblMedications, lblService, lblNoOfHours, lblGenderPreference, lblLanguagePreference, lblEnquirerName, lblEnquirerPhone, lblEnquirerEmail, lblAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_case_details);

        setUpNavigation("Bill Details");

        realm = Realm.getDefaultInstance();

        Bundle extras = getIntent().getExtras();
        caseID = extras.getLong("caseID");
        mContext = this;

        caseRecord = realm.where(CaseRecord.class).equalTo("id",caseID).findFirst();
        patient = realm.where(Patient.class).equalTo("id",caseRecord.getPatientId()).findFirst();




        lblPatientName = (TextView) findViewById(R.id.lblPatientName);
        lblPatientAge = (TextView) findViewById(R.id.lblPatientAge);


        lblPatientName.setText(String.valueOf(caseID));
        lblPatientAge.setText(patient.getCity());




    }
}
