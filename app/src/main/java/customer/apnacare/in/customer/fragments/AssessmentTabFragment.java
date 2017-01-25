package customer.apnacare.in.customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Assessment;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;

/**
 * Created by root on 12/1/17.
 */

public class AssessmentTabFragment extends Fragment {

    Context mContext;
    ViewGroup view;
    Realm realm;
    Assessment assessment;
    TextView assessmentDate,assessor,assessmentPatientConditions;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_assessment, container, false);
        mContext = getContext();

        realm = Realm.getDefaultInstance();
        assessment = realm.where(Assessment.class).findFirst();
        Log.v(Constants.TAG,"assessmentprofile: "+assessment);

        assessmentDate = (TextView) view.findViewById(R.id.assessmentDate);
        assessor = (TextView) view.findViewById(R.id.assessor);
        assessmentPatientConditions = (TextView) view.findViewById(R.id.assessmentPatientConditions);

        assessmentDate.setText(String.valueOf(assessment.getId()));
        assessmentPatientConditions.setText(assessment.getFormData());

        return view;
    }
}
