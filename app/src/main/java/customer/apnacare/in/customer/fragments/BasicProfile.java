package customer.apnacare.in.customer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;

/**
 * Created by root on 9/1/17.
 */

public class BasicProfile extends Fragment {

    ViewGroup mView;
    ProgressDialog mDialog;
    Context mContext;
    ImageView imgCareGiver;
    TextView lblCareGiverName, lblCareGiverMobile, lblCareGiverCity, lblCareGiverSpecialization, lblCareGiverQualification, lblCareGiverExperience;
    String careTakerImage;


    public BasicProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_basic, container, false);


        return view;
    }
}
