package customer.apnacare.in.customer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.fragments.AssessmentTabFragment;
import customer.apnacare.in.customer.fragments.BasicProfile;
import customer.apnacare.in.customer.fragments.CaseDetails;
import customer.apnacare.in.customer.fragments.TasksTabFragment;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;

/**
 * Created by root on 30/12/16.
 */

public class ViewCaseActivity extends BaseActivity implements CaseDetails.ProviderStatus {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @Nullable
    @BindView(R.id.case_nav_view)
    NavigationView navigationView;



    Context mContext;
    Bundle bundle;
    private CoordinatorLayout mCoordinatorLayout;
    ViewPager mViewPager;
    TabLayout tabs;
    ProgressDialog mDialog;
    private Realm realm;
    public static String CASE_ID = "";

    long caseID;
    CaseRecord caseRecord;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_view);
        ButterKnife.bind(this);
        setUpNavigation("Case Details");


        realm = Realm.getDefaultInstance();

//        Bundle extras = getIntent().getExtras();
//        caseID = extras.getLong("caseID");
//
//        bundle =new Bundle();
//        bundle.putLong("caseID", caseID);

        Bundle extras = getIntent().getExtras();
        caseID = extras.getLong("caseID");

        bundle = new Bundle();
        bundle.putLong("caseID", caseID);

        mContext = this;
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.viewCaseCoordinatorLayout);
        tabs = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        // Setting ViewPager for each Tabs
        setupViewPager(mViewPager);

        // Set Tabs inside Toolbar
        tabs.setupWithViewPager(mViewPager);



    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager){
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new CaseDetails(), "Patient Details",bundle);
        adapter.addFragment(new AssessmentTabFragment(), "Assessment", bundle);
        adapter.addFragment(new BasicProfile(), "CareGiver", bundle);
        adapter.addFragment(new TasksTabFragment(), "Tasks", bundle);


        viewPager.setAdapter(adapter);
    }

    @Override
    public void updateStatus(String status) {
        if(status.equals("Accepted")){
            tabs.setVisibility(View.VISIBLE);
            setupViewPager(mViewPager);
        }
    }

    static class Adapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }



        @Override
        public int getCount() {
            return mFragmentList.size();

        }

        public void addFragment(Fragment fragment, String title, Bundle bundle) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            fragment.setArguments(bundle);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
