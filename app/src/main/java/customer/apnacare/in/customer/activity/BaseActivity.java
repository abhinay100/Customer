package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import customer.apnacare.in.customer.utils.PermissionsUtil;

/**
 * Created by root on 21/12/16.
 */
public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;

    TextView headerProviderName, headerProviderEmail;
    protected Context mContext;
    public boolean isLoggedIn = false;

    public MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

//        checkSession();

    }

    public void checkSession(){
        isLoggedIn = CustomerApp.preferences.getBoolean("isLoggedIn",false);

        if(!isLoggedIn){
            Intent i = new Intent(BaseActivity.this,LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }



    protected void setUpNavigation(String title){
        try{
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setContentInsetsAbsolute(toolbar.getContentInsetLeft(), 0);
            if(!title.equals(null)){
                toolbar.setTitle(title);
            }


            setSupportActionBar(toolbar);




            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
            navigationView.addHeaderView(header);

            headerProviderName = (TextView) header.findViewById(R.id.lblHeaderProviderName);
            headerProviderEmail = (TextView) header.findViewById(R.id.lblHeaderProviderDesignation);

            headerProviderName.setText(CustomerApp.preferences.getString("fullName","").toString());
            headerProviderEmail.setText(CustomerApp.preferences.getString("phoneNumber","").toString());

            setStatusBarColor();

        }catch (Exception e){
            Log.v(Constants.TAG,"setUpNavigation() Exception: "+e.toString());
        }
    }

    public void setStatusBarColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void showProgressBar(String title){
        if(materialDialog != null){
            materialDialog.dismiss();
        }
        materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content("Please wait...")
                .progress(true, 0)
                .show();
    }

    public void showMaterialDialog(String title, String message){
        if(materialDialog != null){
            materialDialog.dismiss();
        }
        materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .show();
    }

    public void hideProgressBar(){
        if(materialDialog != null && materialDialog.isShowing()){
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            startActivity(new Intent(BaseActivity.this,MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.menu_home: startActivity(new Intent(BaseActivity.this,MainActivity.class));break;
//            case R.id.menu_settings: break;
            case R.id.menu_logout: logout(); break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void logout(){
        CustomerApp.e.putBoolean("isLoggedIn",false);
        CustomerApp.e.putLong("userID",-1);
        CustomerApp.e.apply();

        Intent i = new Intent(BaseActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public void checkPermissions(){
        if(PermissionsUtil.doesAppNeedPermissions()) {
            PermissionsUtil.askPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionsUtil.PERMISSION_ALL: {
                Log.v(Constants.TAG,"grantResults: "+grantResults);
                if (grantResults.length > 0) {

                    List<Integer> indexesOfPermissionsNeededToShow = new ArrayList<>();

                    for(int i = 0; i < permissions.length; ++i) {
                        if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i])) {
                            indexesOfPermissionsNeededToShow.add(i);
                        }
                    }

                    int size = indexesOfPermissionsNeededToShow.size();
                    if(size != 0) {
                        int i = 0;
                        boolean isPermissionGranted = true;

                        while(i < size && isPermissionGranted) {
                            isPermissionGranted = grantResults[indexesOfPermissionsNeededToShow.get(i)]
                                    == PackageManager.PERMISSION_GRANTED;
                            i++;
                        }

                        if(!isPermissionGranted) {

                            showDialogNotCancelable("Permissions mandatory",
                                    "All the permissions are required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            checkPermissions();
                                        }
                                    });
                        }
                    }
                }
            }
        }
    }

    private void showDialogNotCancelable(String title, String message,
                                         DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setCancelable(false)
                .create()
                .show();
    }
}


