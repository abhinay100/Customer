package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Comments;
import customer.apnacare.in.customer.model.Dispositions;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/2/17.
 */

public class DispositionsAdapter extends RealmBasedRecyclerViewAdapter<Dispositions, DispositionsAdapter.ViewHolder> {

    Context mContext;

    public DispositionsAdapter(
            Context context,
            RealmResults<Dispositions> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }


    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.lblDispositionName) TextView dispositionName;
        @BindView(R.id.lblDispositionDate) TextView dispositionDate;
        @BindView(R.id.lblStatus) TextView dispositionStatus;
        @BindView(R.id.lblComments) TextView dispositionsComments;


        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder vh = null;
        try {
            View v = inflater.inflate(R.layout.dispositions_card,viewGroup,false);
            vh = new ViewHolder(v);

        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }
        return  vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindRealmViewHolder(DispositionsAdapter.ViewHolder viewHolder, int position) {

        final Dispositions dispositions = realmResults.get(position);

        try {

            viewHolder.dispositionDate.setText(dispositions.getDispositionDate());
            viewHolder.dispositionStatus.setText(dispositions.getStatus());
            viewHolder.dispositionsComments.setText(dispositions.getComment());
            viewHolder.dispositionName.setText(dispositions.getUserName());

        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }

    }


}
