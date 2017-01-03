package customer.apnacare.in.customer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import io.realm.Realm;

/**
 * Created by root on 30/12/16.
 */

public class ViewCaseActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @Nullable
    @BindView(R.id.case_nav_view)
    NavigationView navigationView;

    private Realm realm;

    long caseID;
    CaseRecord caseRecord;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_view);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        Bundle extras = getIntent().getExtras();
        caseID = extras.getLong("caseID");

        if(caseID > 0){
            caseRecord = realm.where(CaseRecord.class).equalTo("id",caseID).findFirst();
            patient = realm.where(Patient.class).equalTo("id",caseRecord.getPatientId()).findFirst();
        }

    //    toolbar.setTitle("Case - "+patient.getFirstName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(realm != null && !realm.isClosed()) {
                    realm.close();
                    realm = null;
                }
                finish();
                moveTaskToBack(false);
                onBackPressed();
                return;
            }
        });

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(drawer != null){
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(realm != null && !realm.isClosed()) {
                        realm.close();
                        realm = null;
                    }
                    finish();
                    moveTaskToBack(false);
                    onBackPressed();
                }
            });
            toggle.setDrawerIndicatorEnabled(false);
            toggle.syncState();
        }

        if(navigationView != null){
            navigationView.setNavigationItemSelectedListener(this);
            View header = LayoutInflater.from(this).inflate(R.layout.nav_case_menu_header, null);
            navigationView.addHeaderView(header);
        }

        displaySelectedScreen(R.id.nav_menu1);

        setStatusBarColor();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());

        return true;
    }

    private void displaySelectedScreen(int itemId) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:
//                fragment = new CaseDetails();
                break;
//            case R.id.nav_menu2:
//                fragment = new Menu2();
//                break;
//            case R.id.nav_menu3:
//                fragment = new Menu3();
//                break;
        }

        //replacing the fragment
        if (fragment != null) {
            Bundle args = new Bundle();
            args.putLong("caseID",caseRecord.getId());
            fragment.setArguments(args);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     //   drawer.closeDrawer(GravityCompat.END);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.case_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.caseMenu){
            if (drawer.isDrawerOpen(Gravity.RIGHT)) {
                drawer.closeDrawer(Gravity.RIGHT);
            }
            else {
                drawer.openDrawer(Gravity.RIGHT);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {

        }
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(realm != null && !realm.isClosed()) {
            realm.close();
            realm = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(realm != null && !realm.isClosed()) {
            realm.close();
            realm = null;
        }
    }
}
