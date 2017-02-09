package customer.apnacare.in.customer.service;

/**
 * Created by suhas on 17/8/16.
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        CustomerApp.e.putString("fcm_token",token);
        CustomerApp.e.putBoolean("fcm_token_sync",false);
        CustomerApp.e.apply();

        //Displaying token on logcat
        Log.v(Constants.TAG, "Refreshed token: " + token);
    }
}