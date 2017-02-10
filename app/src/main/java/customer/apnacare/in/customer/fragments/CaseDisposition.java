package customer.apnacare.in.customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.adapters.DispositionsAdapter;
import customer.apnacare.in.customer.model.Dispositions;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 9/2/17.
 */

public class CaseDisposition extends Fragment {

    Context mContext;
    RecyclerView mRecyclerView;
    DispositionsAdapter dispositionsAdapter;
    private Realm realm;
    ViewGroup view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_dispositions, container, false);
        mContext = getContext();
        init_list();
        return view;
    }

    public void init_list(){
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Dispositions> dispositionList = realm.where(Dispositions.class).findAll();
            Log.v(Constants.TAG, "dispositionList " + dispositionList);
            mRecyclerView = (RecyclerView) view.findViewById(R.id.caseDispositionsList);
            dispositionsAdapter = new DispositionsAdapter(this.getContext(),dispositionList,true,true);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.swapAdapter(dispositionsAdapter,true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            dispositionsAdapter.notifyDataSetChanged();


        }catch (Exception e){
            Log.v(Constants.TAG, "Dispositions Recycler Exception: " + e.toString());
        }
    }
}
