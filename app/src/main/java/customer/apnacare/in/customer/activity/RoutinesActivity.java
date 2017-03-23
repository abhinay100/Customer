package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 17/2/17.
 */

public class RoutinesActivity extends BaseActivity {

    Context mContext;
    String logDate;
    long worlklogId;
    TextView lblCurrentDate;
    private Realm realm;
    JsonArray vitals;
    JsonObject morningSession,afternoonSession,eveningSession,nightSession;
    JsonObject vitalObject;
    Map<String, Object> morningRoutine, afternoonRoutine, eveningRoutine, nightRoutine;
    LinearLayout routinesListLayout;
    TextView textMorningMobilization, textMorningVitals, textMorningDressing, textMorningGrooming, textMorningBathing, textMorningBedCare, textMorningBreakfast, textMorningOralCare, textMorningOralMedication;
    TextView txtAfternoonVitals, txtAfternoonLunch, txtAfternoonOral;
    TextView txtEveningOral, txtEveningVital, txtEveningDinner, txtEveningMobilization ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routines);
        setUpNavigation("Routines");
        mContext = this;

        Bundle extras = getIntent().getExtras();
        logDate = extras.getString("worklogDate");
        worlklogId = extras.getLong("worlkogId");
//        createRoutineBySession();

        lblCurrentDate = (TextView) findViewById(R.id.lblCurrentDate);
        lblCurrentDate.setText("Routine Checklist " + "- " + logDate);

        realm = Realm.getDefaultInstance();


        createLayout();

    }


    public void createLayout(){


        RealmResults<WorkLog> workLog = realm.where(WorkLog.class).equalTo("id",worlklogId).findAll();
        JsonParser parser = new JsonParser();
        if(workLog.get(0).getRoutines().toString() != null) {
            try {
                vitals = parser.parse(workLog.get(0).getRoutines().toString()).getAsJsonArray();
            }catch (Exception e){
                Log.v(Constants.TAG,"routines Exception: "+e.toString());
            }
        }


        if(vitals != null) {
            try {
                vitalObject = vitals.get(0).getAsJsonObject();
            }catch (Exception e){
                Log.v(Constants.TAG,"vitalObject Exception: "+e.toString());
            }

        }

        else{
            Toast.makeText(mContext, "Routines not available", Toast.LENGTH_SHORT).show();
        }


        if(vitalObject !=null)  {

            try{
                morningSession = vitalObject.get("morning").getAsJsonObject();

            }catch (Exception e){
                Log.v(Constants.TAG,"morning Session Exception: "+e.toString());
            }

            try {
                afternoonSession = vitalObject.get("afternoon").getAsJsonObject();
            }catch (Exception e){
                Log.v(Constants.TAG,"Afternoon Session Exception: "+e.toString());
            }
            try{
                nightSession = vitalObject.get("night").getAsJsonObject();
            }catch (Exception e){
                Log.v(Constants.TAG,"night Session Exception: "+e.toString());
            }
            try{
                eveningSession = vitalObject.get("evening").getAsJsonObject();
            }catch (Exception e){
                Log.v(Constants.TAG,"evening Session Exception: "+e.toString());
            }
        }

        LinearLayout morningLayout = (LinearLayout) findViewById(R.id.morningLayoutId);
        LinearLayout afternoonLayout = (LinearLayout) findViewById(R.id.afternoonLayoutId);
        LinearLayout eveningLayout = (LinearLayout) findViewById(R.id.eveningLayoutId);
        LinearLayout nightLayout = (LinearLayout) findViewById(R.id.nightLayoutId);

        if(morningSession !=null && morningSession.size() >= 0){

            TextView sessionName = new TextView(this);
            sessionName.setText("Morning");
            sessionName.setTextSize(22);
            sessionName.setGravity(Gravity.START);
            sessionName.setTypeface(null, Typeface.BOLD);
            sessionName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if(morningSession.has("Mobilization")){

                textMorningMobilization = new TextView(this);
                textMorningMobilization.setText("Mobilization " + "- " + "          " + morningSession.get("Mobilization").toString().replace("\"", ""));
                textMorningMobilization.setTextSize(16);
                textMorningMobilization.setGravity(Gravity.START);
                textMorningMobilization.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                textMorningMobilization = new TextView(this);
                textMorningMobilization.setText("Mobilization " + "- " + "          " + "Not Done");
                textMorningMobilization.setTextColor(Color.RED);
                textMorningMobilization.setTextSize(16);
                textMorningMobilization.setGravity(Gravity.START);
                textMorningMobilization.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Vitals")){

                textMorningVitals = new TextView(this);
                textMorningVitals.setText("Vitals " + "- " + "                      " + morningSession.get("Vitals").toString().replace("\"", ""));
                textMorningVitals.setTextSize(16);
                textMorningVitals.setGravity(Gravity.START);
                textMorningVitals.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningVitals = new TextView(this);
                textMorningVitals.setText("Vitals " + "- " + "                      " + "Not Done");
                textMorningVitals.setTextColor(Color.RED);
                textMorningVitals.setTextSize(16);
                textMorningVitals.setGravity(Gravity.START);
                textMorningVitals.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            }

            if(morningSession.has("Dressing")){

                textMorningDressing = new TextView(this);
                textMorningDressing.setText("Dressing " + "- " + "                " + morningSession.get("Dressing").toString().replace("\"", ""));
                textMorningDressing.setTextSize(16);
                textMorningDressing.setGravity(Gravity.START);
                textMorningDressing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningDressing = new TextView(this);
                textMorningDressing.setText("Dressing " + "- " + "                " + "Not Done");
                textMorningDressing.setTextColor(Color.RED);
                textMorningDressing.setTextSize(16);
                textMorningMobilization.setGravity(Gravity.START);
                textMorningMobilization.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Grooming")){

                textMorningGrooming = new TextView(this);
                textMorningGrooming.setText("Grooming " + "- " + "              " + morningSession.get("Grooming").toString().replace("\"", ""));
                textMorningGrooming.setTextSize(16);
                textMorningGrooming.setGravity(Gravity.START);
                textMorningGrooming.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningGrooming = new TextView(this);
                textMorningGrooming.setText("Grooming " + "- " + "              " + "Not Done");
                textMorningGrooming.setTextColor(Color.RED);
                textMorningGrooming.setTextSize(16);
                textMorningGrooming.setGravity(Gravity.START);
                textMorningGrooming.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Bathing")){

                textMorningBathing = new TextView(this);
                textMorningBathing.setText("Bathing " + "- " + "                  " + morningSession.get("Bathing").toString().replace("\"", ""));
                textMorningBathing.setTextSize(16);
                textMorningBathing.setGravity(Gravity.START);
                textMorningBathing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningBathing = new TextView(this);
                textMorningBathing.setText("Bathing " + "- " + "                  " + "Not Done");
                textMorningBathing.setTextColor(Color.RED);
                textMorningBathing.setTextSize(16);
                textMorningBathing.setGravity(Gravity.START);
                textMorningBathing.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Bed Care")){

                textMorningBedCare = new TextView(this);
                textMorningBedCare.setText("Bed Care " + "- " + "                " + morningSession.get("Bed Care").toString().replace("\"", ""));
                textMorningBedCare.setTextSize(16);
                textMorningBedCare.setGravity(Gravity.START);
                textMorningBedCare.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningBedCare = new TextView(this);
                textMorningBedCare.setText("Bed Care " + "- " + "                " + "Not Done");
                textMorningBedCare.setTextColor(Color.RED);
                textMorningBedCare.setTextSize(16);
                textMorningBedCare.setGravity(Gravity.START);
                textMorningBedCare.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Breakfast")){

                textMorningBreakfast = new TextView(this);
                textMorningBreakfast.setText("Breakfast " + "- " + "               " + morningSession.get("Breakfast").toString().replace("\"", ""));
                textMorningBreakfast.setTextSize(16);
                textMorningBreakfast.setGravity(Gravity.START);
                textMorningBreakfast.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningBreakfast = new TextView(this);
                textMorningBreakfast.setText("Breakfast " + "- " + "               " + "Not Done");
                textMorningBreakfast.setTextColor(Color.RED);
                textMorningBreakfast.setTextSize(16);
                textMorningBreakfast.setGravity(Gravity.START);
                textMorningBreakfast.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Oral Care")){

                textMorningOralCare = new TextView(this);
                textMorningOralCare.setText("Oral Care " + "- " + "                " + morningSession.get("Oral Care").toString().replace("\"", ""));
                textMorningOralCare.setTextSize(16);
                textMorningOralCare.setGravity(Gravity.START);
                textMorningOralCare.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningOralCare = new TextView(this);
                textMorningOralCare.setText("Oral Care " + "- " + "                " + "Not Done");
                textMorningOralCare.setTextColor(Color.RED);
                textMorningOralCare.setTextSize(16);
                textMorningOralCare.setGravity(Gravity.START);
                textMorningOralCare.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(morningSession.has("Oral Medication")){

                textMorningOralMedication = new TextView(this);
                textMorningOralMedication.setText("Oral Medication " + "- " + "    " + morningSession.get("Oral Medication").toString().replace("\"", ""));
                textMorningOralMedication.setTextSize(16);
                textMorningOralMedication.setGravity(Gravity.START);
                textMorningOralMedication.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }
            else {
                textMorningOralMedication = new TextView(this);
                textMorningOralMedication.setText("Oral Medication " + "- " + "    " + "Not Done");
                textMorningOralMedication.setTextColor(Color.RED);
                textMorningOralMedication.setTextSize(16);
                textMorningOralMedication.setGravity(Gravity.START);
                textMorningOralMedication.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }




            morningLayout.setPadding(10,10,10,10);
            morningLayout.addView(sessionName);
            morningLayout.addView(textMorningMobilization);
            morningLayout.addView(textMorningVitals);
            morningLayout.addView(textMorningDressing);
            morningLayout.addView(textMorningGrooming);
            morningLayout.addView(textMorningBathing);
            morningLayout.addView(textMorningBedCare);
            morningLayout.addView(textMorningBreakfast);
            morningLayout.addView(textMorningOralCare);
            morningLayout.addView(textMorningOralMedication);




        }

        if(afternoonSession !=null && afternoonSession.size() >= 0){

            TextView txtAfternoon = new TextView(this);
            txtAfternoon.setText("Afternoon");
            txtAfternoon.setTextSize(22);
            txtAfternoon.setGravity(Gravity.LEFT);
            txtAfternoon.setTypeface(null, Typeface.BOLD);
            txtAfternoon.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if(afternoonSession.has("Vitals")){

                txtAfternoonVitals = new TextView(this);
                txtAfternoonVitals.setText("Vitals " + "- " + "                      " + afternoonSession.get("Vitals").toString().replace("\"", ""));
                txtAfternoonVitals.setTextSize(16);
                txtAfternoonVitals.setGravity(Gravity.START);
                txtAfternoonVitals.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtAfternoonVitals = new TextView(this);
                txtAfternoonVitals.setText("Vitals " + "- " + "                      " + "Not Done");
                txtAfternoonVitals.setTextColor(Color.RED);
                txtAfternoonVitals.setTextSize(16);
                txtAfternoonVitals.setGravity(Gravity.START);
                txtAfternoonVitals.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(afternoonSession.has("Lunch")){

                txtAfternoonLunch = new TextView(this);
                txtAfternoonLunch.setText("Lunch " + "- " + "                     " + afternoonSession.get("Lunch").toString().replace("\"", ""));
                txtAfternoonLunch.setTextSize(16);
                txtAfternoonLunch.setGravity(Gravity.START);
                txtAfternoonLunch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtAfternoonLunch = new TextView(this);
                txtAfternoonLunch.setText("Lunch " + "- " + "                     " + "Not Done");
                txtAfternoonLunch.setTextColor(Color.RED);
                txtAfternoonLunch.setTextSize(16);
                txtAfternoonLunch.setGravity(Gravity.START);
                txtAfternoonLunch.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(afternoonSession.has("Oral Medication")){

                txtAfternoonOral = new TextView(this);
                txtAfternoonOral.setText("Oral Medication " + "- " + "    " + afternoonSession.get("Oral Medication").toString().replace("\"", ""));
                txtAfternoonOral.setTextSize(16);
                txtAfternoonOral.setGravity(Gravity.START);
                txtAfternoonOral.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtAfternoonOral = new TextView(this);
                txtAfternoonOral.setText("Oral Medication " + "- " + "    " + "Not Done");
                txtAfternoonOral.setTextColor(Color.RED);
                txtAfternoonOral.setTextSize(16);
                txtAfternoonOral.setGravity(Gravity.START);
                txtAfternoonOral.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            afternoonLayout.setPadding(10,10,10,10);
            afternoonLayout.addView(txtAfternoon);
            afternoonLayout.addView(txtAfternoonVitals);
            afternoonLayout.addView(txtAfternoonLunch);
            afternoonLayout.addView(txtAfternoonOral);


        }

        if(eveningSession !=null && eveningSession.size() >= 0){

            TextView txtEvening = new TextView(this);
            txtEvening.setText("Evening");
            txtEvening.setTextSize(22);
            txtEvening.setGravity(Gravity.LEFT);
            txtEvening.setTypeface(null, Typeface.BOLD);
            txtEvening.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            if(eveningSession.has("Oral Medication")){

                txtEveningOral = new TextView(this);
                txtEveningOral.setText("Oral Medication " + "- " + "    " + eveningSession.get("Oral Medication").toString().replace("\"", ""));
                txtEveningOral.setTextSize(16);
                txtEveningOral.setGravity(Gravity.START);
                txtEveningOral.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtEveningOral = new TextView(this);
                txtEveningOral.setText("Oral Medication " + "- " + "    " + "Not Done");
                txtEveningOral.setTextColor(Color.RED);
                txtEveningOral.setTextSize(16);
                txtEveningOral.setGravity(Gravity.START);
                txtEveningOral.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }


            if(eveningSession.has("Vitals")){

                txtEveningVital = new TextView(this);
                txtEveningVital.setText("Vitals " + "- " + "                      " + eveningSession.get("Vitals").toString().replace("\"", ""));
                txtEveningVital.setTextSize(16);
                txtEveningVital.setGravity(Gravity.START);
                txtEveningVital.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtEveningVital = new TextView(this);
                txtEveningVital.setText("Vitals " + "- " + "                      " + "Not Done");
                txtEveningVital.setTextColor(Color.RED);
                txtEveningVital.setTextSize(16);
                txtEveningVital.setGravity(Gravity.START);
                txtEveningVital.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(eveningSession.has("Dinner")){

                txtEveningDinner = new TextView(this);
                txtEveningDinner.setText("Dinner " + "- " + "                    " + eveningSession.get("Dinner").toString().replace("\"", ""));
                txtEveningDinner.setTextSize(16);
                txtEveningDinner.setGravity(Gravity.START);
                txtEveningDinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtEveningDinner = new TextView(this);
                txtEveningDinner.setText("Dinner " + "- " + "                    " + "Not Done");
                txtEveningDinner.setTextColor(Color.RED);
                txtEveningDinner.setTextSize(16);
                txtEveningDinner.setGravity(Gravity.START);
                txtEveningDinner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }

            if(eveningSession.has("Mobilization")){

                txtEveningMobilization = new TextView(this);
                txtEveningMobilization.setText("Mobilization " + "- " + "          " + eveningSession.get("Mobilization").toString().replace("\"", ""));
                txtEveningMobilization.setTextSize(16);
                txtEveningMobilization.setGravity(Gravity.START);
                txtEveningMobilization.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }else{
                txtEveningMobilization = new TextView(this);
                txtEveningMobilization.setText("Mobilization " + "- " + "          " + "Not Done");
                txtEveningMobilization.setTextColor(Color.RED);
                txtEveningMobilization.setTextSize(16);
                txtEveningMobilization.setGravity(Gravity.START);
                txtEveningMobilization.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            }


            eveningLayout.setPadding(10,10,10,10);
            eveningLayout.addView(txtEvening);
            eveningLayout.addView(txtEveningOral);
            eveningLayout.addView(txtEveningVital);
            eveningLayout.addView(txtEveningDinner);
            eveningLayout.addView(txtEveningMobilization);



        }


    }






}
