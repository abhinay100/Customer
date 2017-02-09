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

import customer.apnacare.in.customer.model.Assessment;
import customer.apnacare.in.customer.model.Bills;
import customer.apnacare.in.customer.model.Caregiver;
import customer.apnacare.in.customer.model.CaseRecord;
import customer.apnacare.in.customer.model.Documents;
import customer.apnacare.in.customer.model.Patient;
import customer.apnacare.in.customer.model.ServiceRequest;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.model.WorkLogFeedback;
import customer.apnacare.in.customer.network.NetworkRequest;
import customer.apnacare.in.customer.network.RestAPI;
import customer.apnacare.in.customer.network.RestAPIBuilder;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.realm.Realm;
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

        if (CustomerApp.isConnectedToInternet()) {

            api = RestAPIBuilder.buildRetrofitService();
            realm = Realm.getDefaultInstance();

            switch (serviceName) {
                case "sendToken":
                    sendToken(intent);
                    break;
                case "signup":
                    signup(intent.getStringExtra("email"), intent.getStringExtra("password"));
                    break;
                case "login":
                    login(intent.getStringExtra("email"), intent.getStringExtra("password"));
                    break;
                case "loadCases":
                    loadCases(intent);
                    break;
                case "loadProfile":
                    loadProfile();
                    break;
                case "newServiceRequest":
                    newServiceRequest(intent);
                    break;
                case "updateProfile":
                    updateProfile(intent);
                    break;
                case "worklogFeedback":
                    worklogFeedback(intent);
                    break;
                case "getMyBills":
                    getMyBills();
                    break;
                case "getDocuments":
                    caregiverDocuments();
                    break;
            }

            Bundle bundle = new Bundle();
        } else {
            realm.close();
            publishResults(serviceName, STATUS_ERROR, null);
        }
    }

    private void publishResults(String serviceName, int result, @Nullable String extra) {
        Intent intent = new Intent(ACTION);
        // Put extras into the intent as usual
        intent.putExtra("resultCode", Activity.RESULT_OK);
        intent.putExtra("serviceName", serviceName);
        intent.putExtra("resultValue", result);
        intent.putExtra("extra", extra);
        // Fire the broadcast with intent packaged
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /*-------------------------------------------------------*/
    /*              All Service Methods                      */
    /*-------------------------------------------------------*/

    public void sendToken(Intent intent) {
    }

    public void signup(String email, String password) {
        try {
            // Simulate network access.
            mNetworkSubscription = NetworkRequest.performAsyncRequest(api.signup(email, password), (data) -> {
                // Update UI on main thread
                try {
                    if (data.getAsJsonObject().get("error") != null) {
                        CustomerApp.e.putBoolean("isLoggedIn", false);
                        CustomerApp.e.apply();

                        publishResults("signup", STATUS_ERROR, null);
                    }

                    CustomerApp.e.putLong("userID", data.getAsJsonObject().get("result").getAsLong());
                    CustomerApp.e.putString("userType", "Customer");
                    CustomerApp.e.putString("email", email);
                    CustomerApp.e.putBoolean("isLoggedIn", true);
                    CustomerApp.e.apply();

                } catch (Exception e) {
                    Log.e(Constants.TAG, "signup() exception: " + e.toString());
                    publishResults("signup", STATUS_ERROR, null);
                } finally {
                    publishResults("signup", STATUS_FINISHED, null);
                }
            }, (error) -> {
                // Handle Error
                Log.e(Constants.TAG, "signup Error: " + error.toString());
                publishResults("signup", STATUS_ERROR, null);
            });

        } catch (Exception e) {
            Log.e(Constants.TAG, "signup() Exception: " + e.toString());
            publishResults("signup", STATUS_ERROR, null);
        }
    }

    public void login(String email, String password) {
        try {
            // Simulate network access.
            mNetworkSubscription = NetworkRequest.performAsyncRequest(api.login(email, password), (data) -> {
                // Update UI on main thread
                try {
                    if (data.getAsJsonObject().get("error") != null) {
                        CustomerApp.e.putBoolean("isLoggedIn", false);
                        CustomerApp.e.apply();

                        publishResults("login", STATUS_ERROR, null);
                    }

//                    Log.v(Constants.TAG,"login response: "+data.getAsJsonObject().get("result"));

                    CustomerApp.e.putLong("userID", data.getAsJsonObject().get("result").getAsLong());
                    CustomerApp.e.putString("userType", "Customer");
                    CustomerApp.e.putString("email", email);
                    CustomerApp.e.putBoolean("isLoggedIn", true);
                    CustomerApp.e.apply();

                } catch (Exception e) {
                    Log.e(Constants.TAG, "login() exception: " + e.toString());
                    publishResults("login", STATUS_ERROR, null);
                } finally {
                    publishResults("login", STATUS_FINISHED, null);
                }
            }, (error) -> {
                // Handle Error
                Log.e(Constants.TAG, "login Error: " + error.toString());
                publishResults("login", STATUS_ERROR, null);
            });

        } catch (Exception e) {
            Log.e(Constants.TAG, "login() Exception: " + e.toString());
            publishResults("login", STATUS_ERROR, null);
        }
    }

    public void loadProfile() {
        String email = CustomerApp.preferences.getString("email", null);

        if (email != null) {
            try {
                // Simulate network access.
                mNetworkSubscription = NetworkRequest.performAsyncRequest(api.loadProfile(email), (data) -> {
                    // Update UI on main thread
                    try {
                        if (data.getAsJsonObject().get("error") != null) {
                            publishResults("loadProfile", STATUS_ERROR, null);
                        }

                        CustomerApp.e.putString("fullName", data.getAsJsonObject().get("result").getAsJsonObject().get("name").getAsString());
                        CustomerApp.e.putString("phoneNumber", data.getAsJsonObject().get("result").getAsJsonObject().get("phone").getAsString());
                        CustomerApp.e.apply();
                    } catch (Exception e) {
                        Log.e(Constants.TAG, "loadProfile() exception: " + e.toString());
                        publishResults("loadProfile", STATUS_ERROR, null);
                    } finally {
                        publishResults("loadProfile", STATUS_FINISHED, null);
                    }
                }, (error) -> {
                    // Handle Error
                    Log.e(Constants.TAG, "loadProfile Error: " + error.toString());
                    publishResults("loadProfile", STATUS_ERROR, null);
                });

            } catch (Exception e) {
                Log.e(Constants.TAG, "loadProfile() Exception: " + e.toString());
                publishResults("loadProfile", STATUS_ERROR, null);
            }
        }

    }

    public void loadCases(Intent intent) {
        long userID = CustomerApp.preferences.getLong("userID", -1);
        String userType = "Customer";

        String existingCaseIDs = "";
        String newCaseIDs = "";

        realm.beginTransaction();
        realm.where(CaseRecord.class).findAll().deleteAllFromRealm();
        realm.where(Patient.class).findAll().deleteAllFromRealm();
        realm.where(Caregiver.class).findAll().deleteAllFromRealm();
        realm.where(Assessment.class).findAll().deleteAllFromRealm();
        realm.where(WorkLog.class).findAll().deleteAllFromRealm();
        realm.where(WorkLogFeedback.class).findAll().deleteAllFromRealm();
        realm.where(Documents.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();

        try {
            mNetworkSubscription = NetworkRequest.performAsyncRequest(api.getCaseData(userID, userType, newCaseIDs, existingCaseIDs), (data) -> {
                // Update UI on main thread

                try {
                    if (data != null) {
                        if (data != null && data.get("result") != null && data.get("result").toString() != "false") {
                            //Log.v(Constants.TAG,"lk: "+data.get("result").getAsJsonObject().get("cases"));

                            Realm realm = Realm.getDefaultInstance();

                            JsonParser parser = new JsonParser();

                            // Cases
                            try {
                                JsonArray casesJsonArray = parser.parse(data.get("result").getAsJsonObject().get("cases").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"casesJsonArray: "+casesJsonArray);
                                if (casesJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < casesJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) casesJsonArray.get(i);
                                                    CaseRecord caseRecord = new CaseRecord(jsonObject);

                                                    Patient patientObject = new Patient();
                                                    patientObject.setId(jsonObject.get("patient_id").getAsLong());
                                                    realm.copyToRealmOrUpdate(patientObject);

                                                    caseRecord.patient = patientObject;

                                                    realm.copyToRealmOrUpdate(caseRecord);
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.v(Constants.TAG, "casesJsonArray Exception: " + e.toString());
                            }


                            // Patients
                            try {
                                JsonArray patientsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("patients").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"patientsJsonArray: "+patientsJsonArray);
                                if (patientsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < patientsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) patientsJsonArray.get(i);
                                                    Patient patients = new Patient(jsonObject);

                                                    realm.copyToRealmOrUpdate(patients);
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.v(Constants.TAG, "patientsJsonArray Exception: " + e.toString());
                            }

                            //Caregiver
                            try {
                                JsonArray caregiverJsonArray = parser.parse(data.get("result").getAsJsonObject().get("caregivers").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"caregiverJsonArray: "+caregiverJsonArray);
                                if (caregiverJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < caregiverJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) caregiverJsonArray.get(i);
                                                    Caregiver caregiver = new Caregiver(jsonObject);

                                                    realm.copyToRealmOrUpdate(caregiver);
                                                } catch (Exception e) {
                                                    Log.v(Constants.TAG, "caregiver json exception: " + e.toString());
                                                }
                                            }
                                        }
                                    });
                                }


                            } catch (Exception e) {
                                Log.v(Constants.TAG, "caregiverJsonArray Exception: " + e.toString());
                            }

                            //Assessments
                            try {
                                JsonArray assessmentsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("assessments").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"assessmentsJsonArray: "+ assessmentsJsonArray);
                                if (assessmentsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < assessmentsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) assessmentsJsonArray.get(i);
                                                    Assessment assessment = new Assessment(jsonObject);
                                                    realm.copyToRealmOrUpdate(assessment);

                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                Log.v(Constants.TAG, "AssessmentsJsonArray Exception: " + e.toString());
                            }


                            // Worklogs
                            try {
                                JsonArray worklogsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("worklogs").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"worklogsJsonArray: "+worklogsJsonArray);
                                if (worklogsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < worklogsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) worklogsJsonArray.get(i);
                                                    WorkLog worklog = new WorkLog(jsonObject);

                                                    realm.copyToRealmOrUpdate(worklog);
                                                } catch (Exception e) {
                                                    Log.v(Constants.TAG, "worklog json Exception: " + e.toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.v(Constants.TAG, "worklogsJsonArray Exception: " + e.toString());
                            }

                            // Feedbacks
                            try {
                                JsonArray feedbacksJsonArray = parser.parse(data.get("result").getAsJsonObject().get("feedbacks").toString()).getAsJsonArray();
                                //Log.v(Constants.TAG,"feedbacksJsonArray: "+feedbacksJsonArray);
                                if (feedbacksJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < feedbacksJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) feedbacksJsonArray.get(i);
                                                    WorkLogFeedback workLogFeedback = new WorkLogFeedback(jsonObject);

                                                    realm.copyToRealmOrUpdate(workLogFeedback);
                                                } catch (Exception e) {
                                                    Log.v(Constants.TAG, "workLogFeedback json Exception: " + e.toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.v(Constants.TAG, "feedbacksJsonArray Exception: " + e.toString());
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
//                                                    JsonObject jsonObject = (JsonObject) visitsJsonArray.get(i);
//                                                    Visit visits = new Visit(jsonObject);
//
//                                                    realm.copyToRealmOrUpdate(visits);
                                                } catch (Exception e) {

                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.v(Constants.TAG, "visitsJsonArray Exception: " + e.toString());
                            }


                            realm.close();
                        }
                    }
                } catch (Exception e) {
                    Log.e(Constants.TAG, "loadAllCases() exception: " + e.toString());
                    publishResults("loadAllCases", STATUS_ERROR, null);
                } finally {
                    publishResults("loadAllCases", STATUS_FINISHED, null);
                }
            }, (error) -> {
                // Handle Error
                Log.e(Constants.TAG, "loadCases Error: " + error.toString());
                publishResults("loadAllCases", STATUS_ERROR, null);
            });

        } catch (Exception e) {
            Log.e(Constants.TAG, "loadAllCases() Exception: " + e.toString());
            publishResults("loadAllCases", STATUS_ERROR, null);
        }
    }


    public void newServiceRequest(Intent intent) {
        String serviceName = intent.getStringExtra("serviceName");
        long requestID = intent.getLongExtra("requestID", 0);

        ServiceRequest request = realm.where(ServiceRequest.class).equalTo("id", requestID).findFirst();
        if (request != null) {
            JsonObject requestObject = new JsonObject();
            requestObject.addProperty("id", request.getId());
            requestObject.addProperty("name", request.getName());
            requestObject.addProperty("phone", request.getPhoneNumber());
            requestObject.addProperty("email", request.getEmail());
            requestObject.addProperty("area", request.getArea());
            requestObject.addProperty("city", request.getCity());
            requestObject.addProperty("requirement", request.getService());

            JsonObject requestJson = new JsonObject();
            requestJson.add("request", requestObject);


            try {
                // Simulate network access.
                mNetworkSubscription = NetworkRequest.performAsyncRequest(api.newServiceRequest(requestJson), (data) -> {
                    // Update UI on main thread
                    try {
                        if (data.getAsJsonObject().get("error") != null) {
                            publishResults(serviceName, STATUS_ERROR, null);
                        }

                        if (data.getAsJsonObject().get("result") != null) {
                            publishResults(serviceName, STATUS_FINISHED, null);
                        }
                    } catch (Exception e) {
                        Log.v(Constants.TAG, "newServiceRequest() exception: " + e.toString());
                        publishResults(serviceName, STATUS_ERROR, null);
                    } finally {
                        publishResults(serviceName, STATUS_FINISHED, null);
                    }
                }, (error) -> {
                    // Handle Error
                    Log.e(Constants.TAG, "newServiceRequest Error: " + error.toString());
                    publishResults(serviceName, STATUS_ERROR, null);
                });

            } catch (Exception e) {
                Log.e(Constants.TAG, "newServiceRequest() Exception: " + e.toString());
                publishResults(serviceName, STATUS_ERROR, null);
            }
        }
    }

    public void updateProfile(Intent intent) {
        String serviceName = intent.getStringExtra("serviceName");
        long requestID = intent.getLongExtra("requestID", 0);
        Patient patient = realm.where(Patient.class).equalTo("id", requestID).findFirst();

        if (patient != null) {
            JsonObject patientObject = new JsonObject();
            patientObject.addProperty("id", patient.getId());
            patientObject.addProperty("first_name", patient.getFirstName());
            patientObject.addProperty("last_name", patient.getLastName());
            patientObject.addProperty("gender", patient.getGender());
            patientObject.addProperty("patient_age", patient.getPatientAge());
            patientObject.addProperty("patient_weight", patient.getPatientWeight());
            patientObject.addProperty("street_address", patient.getStreetAddress());
            patientObject.addProperty("area", patient.getArea());
            patientObject.addProperty("city", patient.getCity());
            patientObject.addProperty("zipcode", patient.getZipcode());
            patientObject.addProperty("state", patient.getState());

            JsonObject patientJson = new JsonObject();
            patientJson.add("request", patientObject);


            try {
                // Simulate network access.
                mNetworkSubscription = NetworkRequest.performAsyncRequest(api.updateProfile(patientJson), (data) -> {
                    // Update UI on main thread
                    try {
                        if (data.getAsJsonObject().get("error") != null) {
                            publishResults(serviceName, STATUS_ERROR, null);
                        }

                        if (data.getAsJsonObject().get("result") != null) {
                            publishResults(serviceName, STATUS_FINISHED, null);
                        }
                    } catch (Exception e) {
                        Log.v(Constants.TAG, "newServiceRequest() exception: " + e.toString());
                        publishResults(serviceName, STATUS_ERROR, null);
                    } finally {
                        publishResults(serviceName, STATUS_FINISHED, null);
                    }
                }, (error) -> {
                    // Handle Error
                    Log.e(Constants.TAG, "newServiceRequest Error: " + error.toString());
                    publishResults(serviceName, STATUS_ERROR, null);
                });

            } catch (Exception e) {
                Log.e(Constants.TAG, "newServiceRequest() Exception: " + e.toString());
                publishResults(serviceName, STATUS_ERROR, null);
            }
        }
    }


    public void worklogFeedback(Intent intent) {
        String serviceName = intent.getStringExtra("serviceName");
        long feedbackID = intent.getLongExtra("feedbackID", 0);
        WorkLogFeedback feedback = realm.where(WorkLogFeedback.class).equalTo("id", feedbackID).findFirst();

        if (feedback != null) {
            long careplanID = feedback.getCareplanId();
            long worklogID = feedback.getWorklogId();
            String customerName = feedback.getCustomerName();
            float rating = feedback.getRating();
            String comment = feedback.getComment();
            try {
                // Simulate network access.
                mNetworkSubscription = NetworkRequest.performAsyncRequest(api.worklogFeedback(careplanID, worklogID, customerName, rating, comment), (data) -> {
                    // Update UI on main thread
                    try {
                        if (data.getAsJsonObject().get("error") != null) {
                            publishResults(serviceName, STATUS_ERROR, null);
                        }

                        if (data.getAsJsonObject().get("result") != null) {
                            publishResults(serviceName, STATUS_FINISHED, null);
                        }
                    } catch (Exception e) {
                        Log.v(Constants.TAG, "worklogFeedback() exception: " + e.toString());
                        publishResults(serviceName, STATUS_ERROR, null);
                    } finally {
                        publishResults(serviceName, STATUS_FINISHED, null);
                    }
                }, (error) -> {
                    // Handle Error
                    Log.e(Constants.TAG, "worklogFeedback Error: " + error.toString());
                    publishResults(serviceName, STATUS_ERROR, null);
                });

            } catch (Exception e) {
                Log.e(Constants.TAG, "worklogFeedback() Exception: " + e.toString());
                publishResults(serviceName, STATUS_ERROR, null);
            }
        }
    }


    public void getMyBills(){
        String email = CustomerApp.preferences.getString("email",null);
        if(email != null){
            try {
                // Simulate network access.
                mNetworkSubscription = NetworkRequest.performAsyncRequest(api.getMyBills(email), (data) -> {
                    // Update UI on main thread
                    try {
                        if(data.getAsJsonObject().get("error") != null && data.getAsJsonObject().get("error").getAsBoolean() == true){
                            publishResults("getMyBills",STATUS_ERROR, null);
                        }

                        if(data.getAsJsonObject().get("result") != null) {
                            // Bills
                            try {
                                Realm realm = Realm.getDefaultInstance();
                                JsonParser parser = new JsonParser();
                                JsonArray billsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("bills").toString()).getAsJsonArray();
//                                Log.v(Constants.TAG,"billsJsonArray: "+billsJsonArray);
                                if (billsJsonArray.size() > 0) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            for (int i = 0; i < billsJsonArray.size(); i++) {
                                                try {
                                                    JsonObject jsonObject = (JsonObject) billsJsonArray.get(i);
                                                    Bills bills = new Bills(jsonObject);

                                                    realm.copyToRealmOrUpdate(bills);
                                                } catch (Exception e) {
                                                    Log.v(Constants.TAG, "billsJsonArray json Exception: " + e.toString());
                                                }
                                            }
                                        }
                                    });
                                }
                            } catch (Exception e) {
                                Log.v(Constants.TAG, "billsJsonArray Exception: " + e.toString());
                            }
                        }

                    } catch (Exception e) {
                        Log.e(Constants.TAG, "getMyBills() exception: " + e.toString());
                        publishResults("getMyBills",STATUS_ERROR, null);
                    }finally {
                        publishResults("getMyBills",STATUS_FINISHED, null);
                    }
                }, (error) -> {
                    // Handle Error
                    Log.e(Constants.TAG,"getMyBills Error: "+error.toString());
                    publishResults("getMyBills",STATUS_ERROR, null);
                });

            }catch (Exception e){
                Log.e(Constants.TAG,"getMyBills() Exception: "+e.toString());
                publishResults("getMyBills",STATUS_ERROR, null);
            }
        }

    }

    public void caregiverDocuments() {

        realm = Realm.getDefaultInstance();
        Caregiver caregiver = realm.where(Caregiver.class).findFirst();
        caregiver.getId();
        long caregiverId = caregiver.getId();

        try {
//             Simulate network access.
            mNetworkSubscription = NetworkRequest.performAsyncRequest(api.getDocuments(caregiverId), (data) -> {

                // Update UI on main thread
                try {
                    if (data.getAsJsonObject().get("result") != null) {
                        // Caregiver details
                        try {
                            Realm realm = Realm.getDefaultInstance();

                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.where(Documents.class).findAll().deleteAllFromRealm();
                                }
                            });
                            JsonParser parser = new JsonParser();
                            JsonArray documentsJsonArray = parser.parse(data.get("result").getAsJsonObject().get("documents").toString()).getAsJsonArray();
                            Log.v(Constants.TAG, "documentsJsonArray: " + documentsJsonArray);
                            if (documentsJsonArray.size() > 0) {
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        for (int i = 0; i < documentsJsonArray.size(); i++) {
                                            try {
                                                JsonObject jsonObject = (JsonObject) documentsJsonArray.get(i);
                                                Documents documents = new Documents(jsonObject);

                                                realm.copyToRealmOrUpdate(documents);
                                            } catch (Exception e) {
                                                Log.v(Constants.TAG, "documentsJsonArray json Exception: " + e.toString());
                                            }
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                            Log.v(Constants.TAG, "documentsJsonArray Exception: " + e.toString());
                        }
                    }

                } catch (Exception e) {
                    Log.e(Constants.TAG, "caregiverDocuments() exception: " + e.toString());
                    publishResults("caregiverDocuments", STATUS_ERROR, null);
                } finally {
                    publishResults("caregiverDocuments", STATUS_FINISHED, null);
                }
            }, (error) -> {
                // Handle Error
                Log.e(Constants.TAG, "caregiverDocuments Error: " + error.toString());
                publishResults("caregiverDocuments", STATUS_ERROR, null);
            });

        } catch (Exception e) {
            Log.e(Constants.TAG, "caregiverDocuments() Exception: " + e.toString());
            publishResults("caregiverDocuments", STATUS_ERROR, null);
        }
    }




}

