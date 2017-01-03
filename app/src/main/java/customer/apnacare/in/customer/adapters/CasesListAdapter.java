package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.activity.CasesActivity;
import customer.apnacare.in.customer.activity.MainActivity;
import customer.apnacare.in.customer.activity.ViewCaseActivity;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 28/12/16.
 */

public class CasesListAdapter extends RealmBasedRecyclerViewAdapter<CaseRecord, CasesListAdapter.ViewHolder> {

    Context mContext;


    public CasesListAdapter(
            Context context,
            RealmResults<CaseRecord> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }



    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.card_view) CardView cardView;

        @BindView(R.id.lblRequestDay) TextView requestDay;
        @BindView(R.id.lblRequestMonth) TextView requestMonth;
        @BindView(R.id.lblRequestYear) TextView requestYear;

        @BindView(R.id.lblPatientName) TextView patientName;
        @BindView(R.id.lblService) TextView service;
        @BindView(R.id.lblArea) TextView area;
        @BindView(R.id.lblCaseStatus) TextView status;

        @BindView(R.id.btn_call_customer) ImageView callCustomer;


        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup,int viewtype){
        ViewHolder vh = null;
        try {

            View v = inflater.inflate(R.layout.case_item_card, viewGroup, false);
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
        try{
            if(caseRecord.getCreatedAt() != null){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(caseRecord.getCreatedAt());

                viewHolder.requestDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
                viewHolder.requestMonth.setText(String.valueOf(new SimpleDateFormat("MMM").format(calendar.getTime())));
                viewHolder.requestYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            }
            viewHolder.patientName.setText(patient.getFirstName());
            viewHolder.service.setText(caseRecord.getServiceName());
            viewHolder.area.setText(patient.getArea()+", "+patient.getCity());
            viewHolder.status.setText("Status: "+caseRecord.getStatus());

//            viewHolder.patientName.setText("Krishna");
//            viewHolder.service.setText("hi");
//            viewHolder.area.setText("Vijaynagar"+", "+"Bangalore");
//            viewHolder.status.setText("Status: "+"available");


            viewHolder.cardView.setOnClickListener((View view) -> {
                Intent intent = new Intent(getContext(), ViewCaseActivity.class);
                intent.putExtra("caseID",caseRecord.getId());
                mContext.startActivity(intent);
            });


        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }

    }



}


