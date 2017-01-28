package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.activity.ViewCaseActivity;
import customer.apnacare.in.customer.activity.ViewPatientsActivity;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 27/1/17.
 */

public class PatientsListAdapter extends RealmBasedRecyclerViewAdapter<CaseRecord, PatientsListAdapter.ViewHolder> {

    Context mContext;


    public PatientsListAdapter(
            Context context,
            RealmResults<CaseRecord> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }



    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.card_view) CardView cardView;


//        @BindView(R.id.lblPatientName) TextView patientName;
//        @BindView(R.id.lblService) TextView patientAge;
//        @BindView(R.id.lblArea) TextView patientGender;


        @BindView(R.id.lblPatient) TextView patientName;
        @BindView(R.id.PatientAge) TextView patientAge;
        @BindView(R.id.PatientGender) TextView patientGender;
//        @BindView(R.id.lblCaseStatus) TextView status;

//        @BindView(R.id.btn_call_customer) ImageView callCustomer;



        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup,int viewtype){
        ViewHolder vh = null;
        try {

            View v = inflater.inflate(R.layout.patient_card, viewGroup, false);
            vh = new ViewHolder(v);

        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }
        return  vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position)  {

        final CaseRecord caseRecord = realmResults.get(position);
        Patient patient = Realm.getDefaultInstance().where(Patient.class).equalTo("id",caseRecord.getPatientId()).findFirst();

            if(patient != null){
                viewHolder.patientName.setText("Name: "+ String.valueOf(patient.getFirstName()));
                viewHolder.patientAge.setText("Age: "+ String.valueOf(patient.getPatientAge()));
                viewHolder.patientGender.setText("Gender: "+ String.valueOf(patient.getGender()));
            }

        viewHolder.cardView.setOnClickListener((View view) -> {
            Intent intent = new Intent(getContext(), ViewPatientsActivity.class);
            intent.putExtra("caseID",caseRecord.getId());
            mContext.startActivity(intent);
        });



    }



}