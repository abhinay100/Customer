package customer.apnacare.in.customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import customer.apnacare.in.customer.R;

/**
 * Created by root on 12/1/17.
 */

public class AssessmentTabFragment extends Fragment {

    Context mContext;
    ViewGroup view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_assessment, container, false);
        mContext = getContext();
        return view;
    }
}
