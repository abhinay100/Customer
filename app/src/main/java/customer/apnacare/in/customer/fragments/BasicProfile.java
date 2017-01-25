package customer.apnacare.in.customer.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Caregiver;
import customer.apnacare.in.customer.utils.CircleTransform;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;

/**
 * Created by root on 9/1/17.
 */

public class BasicProfile extends Fragment {

    TextView txtConcat;
    ImageView caregiverImage;
    Realm realm;
    Caregiver caregiver;




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

        realm = Realm.getDefaultInstance();


        caregiver = realm.where(Caregiver.class).findFirst();
        Log.v(Constants.TAG,"caregiverprofile: "+caregiver);


        txtConcat = (TextView) view.findViewById(R.id.txtCareGiver);
        caregiverImage = (ImageView) view.findViewById(R.id.careGiverImage);
        String details1 = "I am";
        String firstName = caregiver.getFirstName();
//        String firstName = caregiver.getFirstName();
        String lastName = caregiver.getLastName();
        String details2 = "I am";
        String age = "50";
        String details3 = "years old";
        String details4 = "I have";
        int experience = caregiver.getExperience();
        String details5 = "years experience as";
        String specialization = caregiver.getSpecialization();
        String details6 = " I studied at";
        String school = "Oxford";
//        String details7 = "I am looking forward to provide services to your";
//        String relationship = "grandmother";
        String details8 = "My conatct numbers is";
        String phone = caregiver.getMobileNumber();
        String details9 = "My supervisor is";
        String supervisor = "Krishna";
        String details10 = "If you need more information about me, please click on this button";
        String profileImage = caregiver.getProfileImage();

        Log.v(Constants.TAG,"profileImage: "+profileImage);


        txtConcat .setText(details1 + " " + firstName + " " + lastName + "." + " " +  details2 + " " + age + " " + details3 + "." + " " + details4 + " " + experience + " " + details5 + " " + specialization + "." + " " + details6 + " " + school + "."  + " " + details8 + " " + phone + "." + " " + details9 + " " + supervisor + "." + " " + details10 + "." );





        //Loading Image from URL
        Picasso.with(this.getContext())
                .load(profileImage)
                .placeholder(R.drawable.ic_profile)   // optional
                .resize(400,400)
                .transform(new CircleTransform())
                .into(caregiverImage);






        return view;
    }
}
