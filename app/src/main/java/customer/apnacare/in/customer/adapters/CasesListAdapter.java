package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.CaseRecord;
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


    public CasesListAdapter(Context context, RealmResults<CaseRecord> realmResults, boolean automaticUpdate, boolean animateResults, String animateExtraColumnName) {
        super(context, realmResults, automaticUpdate, animateResults, animateExtraColumnName);
        mContext = context;
    }

    @Override
    public CasesListAdapter.ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindRealmViewHolder(CasesListAdapter.ViewHolder viewHolder, int i) {

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

    public ViewHolder onCreateRealmViewHolder

}


