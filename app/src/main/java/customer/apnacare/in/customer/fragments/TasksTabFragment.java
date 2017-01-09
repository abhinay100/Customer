package customer.apnacare.in.customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customer.apnacare.in.customer.R;

/**
 * Created by root on 9/1/17.
 */

public class TasksTabFragment extends Fragment {

    Context mContext;
    ViewGroup view;
    RecyclerView mRecyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_tasks, container, false);
        mContext = getContext();
        return view;
    }



}
