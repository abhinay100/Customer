package customer.apnacare.in.customer.service;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import customer.apnacare.in.customer.model.Assessment;
import customer.apnacare.in.customer.model.Caregiver;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.network.NetworkRequest;
import customer.apnacare.in.customer.network.RestAPI;
import customer.apnacare.in.customer.network.RestAPIBuilder;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.realm.Realm;
import okhttp3.ResponseBody;
import rx.Subscription;

/**
 * Created by root on 30/12/16.
 */

public class DataSyncService extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private RestAPI api;
    private RestAPI json;
    private Subscription mNetworkSubscription;

    protected int userID;
    Realm realm;

    public static final String ACTION = "in.apnacare.customer.service";

    public DataSyncService() {
        super(DataSyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        String serviceName = intent.getStringExtra("serviceName");

        if(CustomerApp.isConnectedToInternet()){

            api = RestAPIBuilder.buildRetrofitService();
            realm = Realm.getDefaultInstance();

            switch (serviceName){
                case "sendToken": sendToken(intent); break;
                case "login": login(intent.getStringExtra("email"), intent.getStringExtra("password"), intent.getStringExtra("type")); break;
                case "loadCases": loadCases(intent); break;
//                case "careGiver": careGiver(intent); break;
            }

            Bundle bundle = new Bundle();
        }else{
            realm.close();
            publishResults(serviceName,STATUS_ERROR, null);
        }
    }

    private void publishResults(String serviceName, int result, @Nullable String extra) {
        Intent intent = new Intent(ACTION);
        // Put extras into the intent as usual
        intent.putExtra("resultCode", Activity.RESULT_OK);
        intent.putExtra("serviceName",serviceName);
        intent.putExtra("resultValue", result);
        intent.putExtra("extra",extra);
        // Fire the broadcast with intent packaged
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private boolean DownloadImage(String profileFolderPath, String fileName, ResponseBody body, String serviceName) {
        try {
            InputStream in = null;
            FileOutputStream out = null;

            // Profiles Folder Path
            File profileFolder = new File(profileFolderPath);
            if(!profileFolder.exists()){
                profileFolder.mkdirs();
            }

            try {
                in = body.byteStream();
                out = new FileOutputStream(profileFolderPath + fileName);
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.v(Constants.TAG,"DownloadImage bytestream IOException: "+e.toString());
                return false;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            File f =  new File(profileFolderPath + fileName);
            Log.v(Constants.TAG,"downloadimage: "+f.exists() + " path: "+f.getAbsolutePath());

            publishResults(serviceName,STATUS_FINISHED, null);

            return true;

        } catch (IOException e) {
            Log.v(Constants.TAG,"DownloadImage IOException: "+e.toString());
            publishResults(serviceName,STATUS_ERROR, null);
            return false;
        }
    }

    /*-------------------------------------------------------*/
    /*              All Service Methods                      */
    /*-------------------------------------------------------*/

    public void sendToken(Intent intent){
    }

    public void login(String email, String password, String type){
        try {
            // Simulate network access.
            mNetworkSubscription = NetworkRequest.performAsyncRequest(api.login(email, password,type), (data) -> {
                // Update UI on main thread
                try {
                    if(data.getAsJsonObject().get("error") != null){
                        CustomerApp.e.putBoolean("isLoggedIn",false);
                        CustomerApp.e.apply();

                        publishResults("login",STATUS_ERROR, null);
                    }

                    String token = data.getAsJsonObject().get("token").toString().replace("\"","");
                    JsonObject profile = data.getAsJsonObject().get("profile").getAsJsonObject();

                    Log.v(Constants.TAG,"profile: "+profile.toString());

                    CustomerApp.e.putString("token", token);
                    CustomerApp.e.putInt("userID", profile.get("id").getAsInt());
                    CustomerApp.e.putString("userType", profile.get("user_type").getAsString());
                    CustomerApp.e.putInt("tenantID", profile.get("tenant_id").getAsInt());
                    CustomerApp.e.putString("organizationName", profile.get("organization_name").getAsString());
                    CustomerApp.e.putString("fullName", profile.get("full_name").getAsString());
                    CustomerApp.e.putString("email", profile.get("email").getAsString());
                    CustomerApp.e.putInt("roleID", profile.get("role_id").getAsInt());
                    CustomerApp.e.putString("profileImagePath", profile.get("profile_image").getAsString());
                    CustomerApp.e.putBoolean("isLoggedIn",true);
                    CustomerApp.e.apply();

                } catch (Exception e) {
                    Log.e(Constants.TAG, "login() exception: " + e.toString());
                    publishResults("login",STATUS_ERROR, null);
                }finally {
                    publishResults("login",STATUS_FINISHED, null);
                }
            }, (error) -> {
                // Handle Error
                Log.e(Constants.TAG,"login Error: "+error.toString());
                publishResults("login",STATUS_ERROR, null);
            });

        }catch (Exception e){
            Log.e(Constants.TAG,"login() Exception: "+e.toString());
            publishResults("login",STATUS_ERROR, null);
        }
    }

    public void loadCases(Intent intent){
        userID = 4; //CustomerApp.preferences.getInt("userID",-1);
        String userType = "Customer"; //PractitionerApp.preferences.getString("userType","Provider");
        String type = intent.getStringExtra("type");

        //List<CaseRecord> cases = cr.getAllCases();
        //String fileNos[] = new String[cases.size()];

        String existingCaseIDs = "";
        String newCaseIDs = "";

        if(type.equals("All")){
            realm.beginTransaction();

            realm.where(CaseRecord.class).findAll().deleteAllFromRealm();
//            realm.where(Comment.class).findAll().deleteAllFromRealm();
            realm.where(Patient.class).findAll().deleteAllFromRealm();
//            realm.where(Visit.class).findAll().deleteAllFromRealm();

            realm.commitTransaction();
        }

        try {
            mNetworkSubscription = NetworkRequest.performAsyncRequest(api.getCaseData(userID, userType, newCaseIDs, existingCaseIDs), (data) -> {
                // Update UI on main thread
                try {
                    if(data != null) {
                        if(data != null && data.get("result") != null && data.get("result").toString() != "false"){
//                            Log.v(Constants.TAG,"lk: "+data.get("result").getAsJsonObject().get("cases"));

                            Realm realm = Realm.getDefaultInstance();

                            JsonParser parser = new JsonParser();

                            // Cases
                            try {
                                JsonArray casesJsonArray = parser.parse(data.get("result").getAsJsonObject().get("cases").toString()).getAsJsonArray();
                                Log.v(Constants.TAG,"casesJsonArray: "+casesJsonArray);
                                if(casesJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < casesJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) casesJsonArray.get(i);
                                                    CaseRecord caseRecord = new CaseRecord(jsonObject);
                                                    Log.v(Constants.TAG,"jsonObject: "+jsonObject);


//                                                    caseRecord.setLanguagePreference(jsonObject.get("language_preference").getAsString());
//                                                    caseRecord.setCareplanName(jsonObject.get("careplan_name").getAsString());



                                                    realm.copyToRealmOrUpdate(caseRecord);
                                                }catch (Exception e){

                                                }
                                            }
                                        }
                                    });
                                }
                            }catch (Exception e){
                                Log.v(Constants.TAG,"casesJsonArray Exception: "+e.toString());
                            }


                            // Patients
                            try {
                                JsonArray patientsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("patients").toString()).getAsJsonArray();
                                Log.v(Constants.TAG,"patientsJsonArray: "+patientsJsonArray);
                                if(patientsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < patientsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) patientsJsonArray.get(i);
                                                    Patient patients = new Patient(jsonObject);

                                                    realm.copyToRealmOrUpdate(patients);
                                                }catch (Exception e){

                                                }
                                            }
                                        }
                                    });
                                }
                            }catch (Exception e){
                                Log.v(Constants.TAG,"patientsJsonArray Exception: "+e.toString());
                            }


                            //Caregiver
                            try {
                              JsonArray caregiverJsonArray = parser.parse(data.get("result").getAsJsonObject().get("caregivers").toString()).getAsJsonArray();
                                Log.v(Constants.TAG,"caregiverJsonArray: "+caregiverJsonArray);
                                if(caregiverJsonArray.size() > 0){
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for(int i =0; i < caregiverJsonArray.size(); i++){
                                                try {
                                                    JsonObject jsonObject = (JsonObject) caregiverJsonArray.get(i);
                                                    Caregiver caregiver = new Caregiver(jsonObject);
                                                    realm.copyToRealmOrUpdate(caregiver);
                                                }catch (Exception e){

                                                }
                                            }
                                        }
                                    });
                                }


                            }catch (Exception e){
                                Log.v(Constants.TAG,"caregiverJsonArray Exception: "+e.toString());
                            }

                            //Assessments
                            try {

                                JsonArray assessmentsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("assessments").toString()).getAsJsonArray();
                                Log.v(Constants.TAG,"assessmentsJsonArray: "+ assessmentsJsonArray);
                                if(assessmentsJsonArray.size() > 0){
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for(int i=0; i < assessmentsJsonArray.size(); i++){
                                                try {
                                                    JsonObject jsonObject = (JsonObject) assessmentsJsonArray.get(i);
                                                    Assessment assessment = new Assessment(jsonObject);
                                                    realm.copyToRealmOrUpdate(assessment);

                                                }catch (Exception e){

                                                }
                                            }
                                        }
                                    });
                                }





                            }catch (Exception e){
                                Log.v(Constants.TAG,"AssessmentsJsonArray Exception: "+e.toString());
                            }




                            // Visits
                            try {
                                JsonArray visitsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("visits").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"visitsJsonArray: "+visitsJsonArray);
                                if (visitsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < visitsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) visitsJsonArray.get(i);
//                                                    Visit visits = new Visit(jsonObject);

//                                                    realm.copyToRealmOrUpdate(visits);
                                                }catch (Exception e){

                                                }
                                            }
                                        }
                                    });
                                }
                            }catch (Exception e){
                                Log.v(Constants.TAG,"visitsJsonArray Exception: "+e.toString());
                            }


                            // Comments
                            try {
                                JsonArray commentsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("comments").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"commentsJsonArray: "+commentsJsonArray);
                                if (commentsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < commentsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) commentsJsonArray.get(i);
//                                                    Comment comments = new Comment(jsonObject);

//                                                    realm.copyToRealmOrUpdate(comments);
                                                }catch (Exception e){

                                                }
                                            }
                                        }
                                    });
                                }
                            }catch (Exception e){
                                Log.v(Constants.TAG,"commentsJsonArray Exception: "+e.toString());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(Constants.TAG, "loadAllCases() exception: " + e.toString());
                    publishResults("loadAllCases",STATUS_ERROR, null);
                }finally {
                    if(realm!=null && !realm.isClosed()){
                        realm.close();
                    }

                    publishResults("loadAllCases",STATUS_FINISHED, null);
                }
            }, (error) -> {
                // Handle Error
                Log.e(Constants.TAG,"Error: "+error.toString());
                publishResults("loadAllCases",STATUS_ERROR, null);
            });

        }catch (Exception e){
            Log.e(Constants.TAG,"loadAllCases() Exception: "+e.toString());
            publishResults("loadAllCases",STATUS_ERROR, null);
        }
    }






}

