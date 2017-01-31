package customer.apnacare.in.customer.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.realm.Realm;

/**
 * Created by root on 4/1/17.
 */

public class CaseDetails extends Fragment {

    Realm realm;
    CaseRecord caseRecord;
    Patient patient;
    ProviderStatus providerStatusUpdate;
    long caseID;

    @BindView(R.id.lblPatientName) TextView lblPatientName;
    @BindView(R.id.lblPatientAge) TextView lblPatientAge;
    @BindView(R.id.lblPatientWeight) TextView lblPatientWeight;
    @BindView(R.id.lblMedicalConditions) TextView lblMedicalConditions;
    @BindView(R.id.lblMedications) TextView lblMedications;
    @BindView(R.id.lblService) TextView lblService;
    @BindView(R.id.lblNoOfHours) TextView lblNoOfHours;
    @BindView(R.id.lblGenderPreference) TextView lblGenderPreference;
    @BindView(R.id.lblLanguagePreference) TextView lblLanguagePreference;
    @BindView(R.id.lblEnquirerName) TextView lblEnquirerName;
    @BindView(R.id.lblEnquirerPhone) TextView lblEnquirerPhone;
    @BindView(R.id.lblEnquirerEmail) TextView lblEnquirerEmail;
    @BindView(R.id.lblAddress) TextView lblAddress;

//    TextView lblPatientName, lblPatientAge, lblPatientWeight, lblMedicalConditions, lblMedications, lblService, lblNoOfHours, lblGenderPreference, lblLanguagePreference, lblEnquirerName, lblEnquirerPhone, lblEnquirerEmail, lblAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_case_details, container, false);
        ButterKnife.bind(this, view);

        realm = Realm.getDefaultInstance();

        caseID = getArguments().getLong("caseID");


        caseRecord = realm.where(CaseRecord.class).equalTo("id",getArguments().getLong("caseID")).findFirst();
        patient = realm.where(Patient.class).equalTo("id",caseRecord.getPatientId()).findFirst();

//        Bundle extras = getActivity().getIntent().getExtras();
//        Log.v(Constants.TAG,"caseid in casedetail: "+getArguments().getLong("caseID"));

        if(patient != null){
            lblPatientName.setText(patient.getFirstName());
            lblPatientAge.setText(patient.getPatientAge());
            lblPatientWeight.setText(patient.getPatientWeight());

            lblEnquirerName.setText(patient.getEnquirerName());
            lblEnquirerPhone.setText(patient.getEnquirerPhone());
            lblEnquirerEmail.setText(CustomerApp.preferences.getString("email","-"));
            lblAddress.setText(patient.getStreetAddress()+" \n"+patient.getArea()+"\n"+patient.getCity()+"\n"+patient.getState());


        }

        if(caseRecord != null){
            lblMedicalConditions.setText("   \u2022 " + TextUtils.join("\n   \u2022 ",caseRecord.getMedicalConditions().toString().replace("\"","").split(",")));
            lblMedications.setText("   \u2022 " + TextUtils.join("\n   \u2022 ",caseRecord.getMedications().toString().replace("\"","").split(",")));
            lblService.setText(caseRecord.getServiceName());
            lblNoOfHours.setText(caseRecord.getNoOfHours());
            lblGenderPreference.setText(caseRecord.getGenderPreference());
            lblLanguagePreference.setText(caseRecord.getLanguagePreference());
        }




//        lblPatientName = (TextView) view.findViewById(R.id.lblPatientName);
//        lblPatientAge = (TextView) view.findViewById(R.id.lblPatientAge);
//        lblPatientWeight = (TextView) view.findViewById(R.id.lblPatientWeight);
//        lblMedicalConditions = (TextView) view.findViewById(R.id.lblMedicalConditions);
//        lblMedications = (TextView) view.findViewById(R.id.lblMedications);
//        lblService  = (TextView) view.findViewById(R.id.lblService);
//        lblNoOfHours = (TextView) view.findViewById(R.id.lblNoOfHours);
//        lblGenderPreference = (TextView) view.findViewById(R.id.lblGenderPreference);
//        lblLanguagePreference = (TextView) view.findViewById(R.id.lblLanguagePreference);
//        lblEnquirerName = (TextView) view.findViewById(R.id.lblEnquirerName);
//        lblEnquirerPhone = (TextView) view.findViewById(R.id.lblEnquirerPhone);
//        lblEnquirerEmail = (TextView) view.findViewById(R.id.lblEnquirerEmail);
//        lblAddress = (TextView) view.findViewById(R.id.lblAddress);
//
//
//            lblPatientName.setText(patient.getFirstName());
//            lblPatientAge.setText(patient.getPatientAge());
//            lblPatientWeight.setText(patient.getPatientWeight());
//
//            lblEnquirerName.setText(patient.getEnquirerName());
//            lblEnquirerPhone.setText(patient.getEnquirerPhone());
//            lblEnquirerEmail.setText("-");
//            lblAddress.setText(patient.getStreetAddress()+" \n"+patient.getArea()+"\n"+patient.getCity()+"\n"+patient.getState());
//
//
//
//
//
//            lblMedicalConditions.setText(caseRecord.getMedicalConditions());
//            lblMedications.setText(caseRecord.getMedications());
//            lblService.setText(caseRecord.getServiceName());
//            lblNoOfHours.setText(caseRecord.getNoOfHours());
//            lblGenderPreference.setText(caseRecord.getGenderPreference());
//            lblLanguagePreference.setText(caseRecord.getLanguagePreference());




        return view;
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        providerStatusUpdate = (ProviderStatus) a;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(patient.getFirstName()+" - Case Details");
    }
    public interface ProviderStatus {
        void updateStatus(String status);
    }

}
