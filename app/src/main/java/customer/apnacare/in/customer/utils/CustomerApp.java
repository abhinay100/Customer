package customer.apnacare.in.customer.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by root on 22/12/16.
 */

public class CustomerApp extends Application {

    public static SharedPreferences preferences;
    public static SharedPreferences.Editor e;
    public static boolean isConnectedToInternet = false;
    private static Context mContext;
    public static ActivityManager activityManager;
    public static RealmConfiguration realmConfig;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        //MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        Realm.init(this);
        Realm.setDefaultConfiguration(getRealmConfig());

        preferences = getApplicationContext().getSharedPreferences(Constants.SETTINGS_FILE_NAME, Constants.MODE_PRIVATE);
        e = preferences.edit();

    }

    public static Context getContext(){
        return mContext;
    }

    public static RealmConfiguration getRealmConfig(){
        return realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
    }

    @Override
    public void onTerminate() {
//        if(PractitionerApp.isServiceRunning(LocationUpdatesBackgroundService.class)){
//            Log.v(Constants.TAG,"LocationUpdatesBackgroundService Stopped");
//            stopService(new Intent(getApplicationContext(),LocationUpdatesBackgroundService.class));
//        }
//
//        if(PractitionerApp.isServiceRunning(LocationTrackerService.class)){
//            Log.v(Constants.TAG,"LocationTrackerService Stopped");
//            stopService(new Intent(getApplicationContext(),LocationTrackerService.class));
//        }

        super.onTerminate();
    }

    public static boolean isConnectedToInternet(){
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public static boolean isServiceRunning(Class<?> serviceClass) {
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
