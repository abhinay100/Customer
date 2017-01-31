package customer.apnacare.in.customer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        //Log.v(Constants.TAG,"caregiverprofile: "+caregiver);


        txtConcat = (TextView) view.findViewById(R.id.txtCareGiver);
        caregiverImage = (ImageView) view.findViewById(R.id.careGiverImage);

        // Profile Description Builder
        String profileDesc = "Hi \n\nI am <b>" + caregiver.getFirstName() + " " + caregiver.getLastName() + "</b>";
        if(!caregiver.getDateOfBirth().isEmpty() && getAge(caregiver.getDateOfBirth()) != 0){
            profileDesc += ". I am <b>" + String.valueOf(getAge(caregiver.getDateOfBirth())) + "</b> years old";
        }

        if(caregiver.getExperience() != 0){
            profileDesc += ". I have <b>" + caregiver.getExperience() + "</b> years of experience as " + caregiver.getSpecialization();
        }

        if(!caregiver.getCollegeName().isEmpty()){
            profileDesc += ". I studied at <b>" + caregiver.getCollegeName() + "</b>";
        }

        profileDesc += ".\n\nMy Contact number is <b>" + caregiver.getMobileNumber() + "</b>";
        profileDesc += ". My supervisor is Krishna ";

        profileDesc += ".\n\nIf you need more information about me, please click on this button";

//        String details7 = "I am looking forward to provide services to your";
//        String relationship = "grandmother";
        String profileImage = caregiver.getProfileImage();

        txtConcat .setText(Html.fromHtml(profileDesc));

        //Loading Image from URL
        Picasso.with(getActivity())
                .load(profileImage)
                .resize(200,200)
                .transform(new CircleTransform())
                .into(caregiverImage);

        return view;
    }

    private int getAge(String date){
        int calculatedAge = 0;

        if(date != null) {
            try {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date mDate = df.parse(date);

                Log.v(Constants.TAG,"mDate: " + mDate);

                if (mDate != null) {
                    int year = mDate.getYear()+1900;
                    int month = mDate.getMonth() + 01;
                    int day = mDate.getDay();

                    Log.v(Constants.TAG,"year: "+year+" | month: "+month+" | day: "+day);

                    Calendar dob = Calendar.getInstance();
                    Calendar today = Calendar.getInstance();

                    dob.set(year, month, day);

                    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                        age--;
                    }

                    Integer ageInt = new Integer(age);
                    calculatedAge = ageInt;
                }
            }catch (ParseException e){
                Log.v(Constants.TAG,"exception: "+e.toString());
            }
        }

        return calculatedAge;
    }
}

