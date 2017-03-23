package customer.apnacare.in.customer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import customer.apnacare.in.customer.utils.PermissionsUtil;

/**
 * Created by root on 22/12/16.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email_sign_up_button) Button btnSignup;
    @BindView(R.id.email_sign_in_button) Button btnSignin;
    @BindView(R.id.email) EditText txtEmail;
    @BindView(R.id.password) EditText txtPassword;

    public MaterialDialog materialDialog;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        ButterKnife.bind(this);

        checkPermissions();

        boolean isLoggedIn = CustomerApp.preferences.getBoolean("isLoggedIn",false);
        Log.v(Constants.TAG,"isLoggedIn: "+isLoggedIn);
        if(isLoggedIn){
            redirect();
        }

        if(CustomerApp.preferences.getString("email",null) != null){
            txtEmail.setText(CustomerApp.preferences.getString("email",""));
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    @OnClick(R.id.email_sign_up_button)
    public void showSignUpForm(View v){
        signup();
    }

    @OnClick(R.id.email_sign_in_button)
    public void signIn(View v){
        String email = txtEmail.getText().toString();

        if(!email.isEmpty() && isEmailValid(email)){
            login();
        }
    }

    public void signup() {
        String email;

        email = txtEmail.getText().toString();

        showProgressBar("Signing Up...");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "signup");
        i.putExtra("email",email);
        i.putExtra("password", txtPassword.getText().toString());
        startService(i);
    }

    // Launching the Provider Login service
    public void login() {
        String email, password;

        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();

        showProgressBar("Authenticating");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "login");
        i.putExtra("email",email);
        i.putExtra("password",password);
        startService(i);
    }

    public void loadCaseData() {
        showProgressBar("Fetching Cases");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "loadCases");
        i.putExtra("type","All");
        startService(i);
    }

    public void loadProfile(){
        showProgressBar("Fetching Profile");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "loadProfile");
        startService(i);
    }

    public void updateRegistrationToken(){
        showProgressBar("Updating registration id");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "updateRegistrationToken");
        startService(i);
    }

    public void loadBills() {
        showProgressBar("Fetching Bills");

        Intent i = new Intent(mContext, DataSyncService.class);
        i.putExtra("serviceName", "getMyBills");
        startService(i);
    }

    public void showProgressBar(String title){
        if(materialDialog != null){
            materialDialog.dismiss();
        }
        materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content("Authenticating..")
                .progress(true, 0)
                .show();
    }

    public void hideProgressBar(){
        if(materialDialog != null && materialDialog.isShowing()){
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

    // Define the callback for what to do when data is received
    private BroadcastReceiver DataSyncReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String serviceName;
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                serviceName = intent.getStringExtra("serviceName");
                int resultValue = intent.getIntExtra("resultValue",2);

                hideProgressBar();

                if(resultValue == 2 && serviceName == "login"){
                    Toast.makeText(mContext,"Invalid Login Credentials!",Toast.LENGTH_SHORT).show();
                    hideProgressBar();
                    return;
                }

                if(CustomerApp.isConnectedToInternet()){
                    switch (serviceName){
                        case "signup": loadProfile(); break;
                        case "login": loadProfile(); break;
                        case "loadProfile": loadCaseData(); break;
                        case "loadCases": loadBills(); break;
                        //case "updateRegistrationToken": loadCaseData(); break;
                        case "getMyBills": redirect(); break;
                        default: redirect(); break;
                    }
                }else{
                    Toast.makeText(mContext,"Could not connect to network!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void redirect(){
        if(CustomerApp.isConnectedToInternet()){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }

    @Override
    public void onResume() {
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(DataSyncService.ACTION);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(DataSyncReceiver, filter);
        // or `registerReceiver(DataSyncReceiver, filter)` for a normal broadcast
        hideProgressBar();
        super.onResume();
    }

    @Override
    public void onPause() {
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(DataSyncReceiver);
        // or `unregisterReceiver(DataSyncReceiver)` for a normal broadcast
        hideProgressBar();
        super.onPause();
    }


    @Override
    public void onDestroy(){
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(DataSyncReceiver);
        hideProgressBar();

        super.onDestroy();
    }

    @Override
    public void onStop(){
        hideProgressBar();

        super.onStop();
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
