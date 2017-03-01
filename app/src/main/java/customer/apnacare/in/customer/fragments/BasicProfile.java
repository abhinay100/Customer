package customer.apnacare.in.customer.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.adapters.DocumentsAdapter;
import customer.apnacare.in.customer.model.Caregiver;
import customer.apnacare.in.customer.model.Documents;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.CircleTransform;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 9/1/17.
 */

public class BasicProfile extends Fragment {

    TextView txtConcat;
    ImageView caregiverImage;
    private Realm realm;
    Caregiver caregiver;
    Context mContext;
    Button moreDetails;
    RecyclerView mRecyclerView;
    DocumentsAdapter documentsAdapter;

    ViewGroup view;

    public BasicProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getDefaultInstance();

        if(realm.where(Documents.class).findAll().size() <= 0) {
            Intent i = new Intent(this.getContext(), DataSyncService.class);
            i.putExtra("serviceName", "getDocuments");
            getActivity().startService(i);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = (ViewGroup) inflater.inflate(R.layout.fragment_profile_basic, container, false);
        mContext = getContext();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.documentList);
//        moreDetails = (Button) view.findViewById(R.id.btnCaregiverInfo) ;
        moreDetails = (ToggleButton) view.findViewById(R.id.btnCaregiverInfo) ;
        mRecyclerView.setVisibility(view.GONE);

        realm = Realm.getDefaultInstance();

        LinearLayout imageLayout = (LinearLayout) view.findViewById(R.id.imageLayout);
        imageLayout.setVisibility(View.GONE);



        moreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String flag=(String) moreDetails.getText();
                if(flag.equalsIgnoreCase("Less Info")){
                    mRecyclerView.setVisibility(view.VISIBLE);
                    if(documentsAdapter != null)
                        documentsAdapter.notifyDataSetChanged();
                }else {
                    mRecyclerView.setVisibility(view.INVISIBLE);
                }
            }
        });






        RealmResults<Documents> DocumentsList = realm.where(Documents.class).findAll();
        //Log.v(Constants.TAG,"DocumentsList: " + DocumentsList);
        documentsAdapter = new DocumentsAdapter(this.getContext(), DocumentsList, true, true, view);
//        documentListRecycler.setAdapter(documentsAdapter);

        mRecyclerView.setAdapter(documentsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        documentsAdapter.notifyDataSetChanged();


        caregiver = realm.where(Caregiver.class).findFirst();
        //Log.v(Constants.TAG,"caregiverprofile: "+caregiver);


        txtConcat = (TextView) view.findViewById(R.id.txtCareGiver);
        caregiverImage = (ImageView) view.findViewById(R.id.careGiverImage);

        // Profile Description Builder
        String profileDesc = "Hi, <br> <br> I am " + caregiver.getFirstName() + " " + caregiver.getLastName();
        if(!caregiver.getDateOfBirth().isEmpty() && getAge(caregiver.getDateOfBirth()) != 0){
            profileDesc += ". I am " + String.valueOf(getAge(caregiver.getDateOfBirth())) + " years old";
        }

        if(caregiver.getExperience() != 0){
            profileDesc += ". I have " + caregiver.getExperience() + " years of experience as  " + caregiver.getSpecialization();
        }

        if(!caregiver.getCollegeName().isEmpty()){
            profileDesc += ". I studied at " + caregiver.getCollegeName() +  ",Bangalore";
        }

        profileDesc += ".\n\nI can speak " +  "English, Hindi, kannada";
//        profileDesc += ".\n\nMy Contact number is <b>" + caregiver.getMobileNumber() + "</b>";
        profileDesc += ".\n\nMy Contact number is " + "9878597462";
        profileDesc += ". <br> <br>My supervisor is " + "Krishna";
        profileDesc += ". His number is " + "9035656331";
        profileDesc += ". <br> <br> If you need more information about me. please click on 'More Info' button below";
        profileDesc += ". <br> <br> I look forward to serving your mom.";




        txtConcat .setText(Html.fromHtml(profileDesc));

        String profileImage = caregiver.getProfileImage();

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

