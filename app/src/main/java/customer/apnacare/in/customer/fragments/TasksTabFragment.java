package customer.apnacare.in.customer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.adapters.CaseTaskAdapter;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 9/1/17.
 */

public class TasksTabFragment extends Fragment {


    Context mContext;
    RecyclerView mRecyclerView;
    CaseTaskAdapter caseTaskListAdapter;
    private Realm realm;

    protected ProgressDialog mDialog;
    ViewGroup view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_tasks, container, false);
        mContext = getContext();
        int  i = 0;
        init_list();
        return view;
    }

    public void init_list(){
        try {
            realm = Realm.getDefaultInstance();
            WorkLog workLog = new WorkLog();
            workLog.setVitals("Heartbeat");
            workLog.setTasks("Tasks");
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(workLog);
            realm.commitTransaction();
            RealmResults<WorkLog> workList = realm.where(WorkLog.class).findAll();
            mRecyclerView = (RecyclerView) view.findViewById(R.id.caseTasksList);
            caseTaskListAdapter = new CaseTaskAdapter(this.getContext(),workList,true,true);

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.swapAdapter(caseTaskListAdapter,true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            caseTaskListAdapter.notifyDataSetChanged();


        }catch (Exception e){

            Log.v(Constants.TAG, "caseTaskListAdapter Recycler Exception: " + e.toString());

        }

    }
}
