package customer.apnacare.in.customer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    TextView assessmentDate,assessor,assessmentPatientConditions,assessmentMedicalProcedures,assessmentSuggestedProfessional,assessmentComment;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = (ViewGroup) inflater.inflate(R.layout.fragment_assessment, container, false);
        mContext = getContext();

        realm = Realm.getDefaultInstance();
        assessment = realm.where(Assessment.class).findFirst();
        Log.v(Constants.TAG,"assessmentprofile: "+assessment);

        assessmentDate = (TextView) view.findViewById(R.id.assessmentDate);
        assessor = (TextView) view.findViewById(R.id.assessor);
        assessmentPatientConditions = (TextView) view.findViewById(R.id.assessmentPatientConditions);
        assessmentMedicalProcedures = (TextView) view.findViewById(R.id.assessmentMedicalProcedures);
        assessmentSuggestedProfessional = (TextView) view.findViewById(R.id.assessmentSuggestedProfessional);
        assessmentComment = (TextView) view.findViewById(R.id.assessmentComment);

        if(assessment != null) {
            JsonParser parser = new JsonParser();
            JsonObject assess = parser.parse(assessment.getFormData().toString()).getAsJsonObject();
            String date = (String.valueOf(assess.get("assessment_date")).replaceAll("\"", ""));
            String name = (String.valueOf(assess.get("assessor_name")).replaceAll("\"", ""));
            String suggestedProfessional = (String.valueOf(assess.get("suggested_professional")).replaceAll("\"", ""));
            String comment = (String.valueOf(assess.get("comment")).replaceAll("\"", ""));


            assessmentDate.setText(date);
            assessor.setText(name);
            assessmentPatientConditions.setText("   \u2022 " + TextUtils.join("\n   \u2022 ",assess.get("patient_condition").toString().replace("\"","").replace("[","").replace("]","").split(",")));
            assessmentMedicalProcedures.setText("   \u2022 " + TextUtils.join("\n   \u2022 ",assess.get("medical_procedures").toString().replace("\"","").replace("[","").replace("]","").split(",")));
            assessmentSuggestedProfessional.setText(suggestedProfessional);
            assessmentComment.setText(comment);
        }

        return view;
    }
}
