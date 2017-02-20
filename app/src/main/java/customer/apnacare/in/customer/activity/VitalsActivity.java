package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Routine;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 17/2/17.
 */

public class VitalsActivity extends BaseActivity {

    Context mContext;
    String logDate;
    long worlklogId;
    TextView lblCurrentDate;
    private Realm realm;
    JsonArray vitals;
    JsonObject morningSession,afternoonSession,eveningSession;

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
        Log.v(Constants.TAG,"workLog size: "+ workLog);
        JsonParser parser = new JsonParser();
        vitals = parser.parse(workLog.get(0).getVitals().toString()).getAsJsonArray();
        JsonObject vitalObject = vitals.get(0).getAsJsonObject();

        if(vitalObject !=null)  {

            try{
                morningSession = vitalObject.get("morning").getAsJsonObject();
                Log.v(Constants.TAG,"sessionaObject: "+morningSession);

            }catch (Exception e){
                Log.v(Constants.TAG,"morning Session Exception: "+e.toString());
            }

            try {
                afternoonSession = vitalObject.get("afternoon").getAsJsonObject();
                Log.v(Constants.TAG, "afternoonSession: " + afternoonSession);
            }catch (Exception e){
                Log.v(Constants.TAG,"Afternoon Session Exception: "+e.toString());
            }
            try{
                eveningSession = vitalObject.get("evening").getAsJsonObject();
                Log.v(Constants.TAG,"eveningSession"+ eveningSession);
            }catch (Exception e){
                Log.v(Constants.TAG,"evening Session Exception: "+e.toString());
            }
        }

        LinearLayout morningLayout = (LinearLayout) findViewById(R.id.morningLayoutId);
        LinearLayout afternoonLayout = (LinearLayout) findViewById(R.id.afternoonLayoutId);
        LinearLayout eveningLayout = (LinearLayout) findViewById(R.id.eveningLayoutId);




        if(morningSession.size() > 0){

            TextView sessionName = new TextView(this);
            sessionName.setText("Morning");
            sessionName.setTextSize(22);
            sessionName.setGravity(Gravity.LEFT);
            sessionName.setTypeface(null, Typeface.BOLD);
            sessionName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


            TextView textBlood = new TextView(this);
            textBlood.setText("Blood Pressure " + "- " + morningSession.get("blood_pressure").toString().replace("\"", ""));
            textBlood.setTextSize(16);
            textBlood.setGravity(Gravity.LEFT);
            textBlood.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textSugar = new TextView(this);
            textSugar.setText("Sugar Level " + "- " + morningSession.get("sugar_level").toString().replace("\"", ""));
            textSugar.setTextSize(16);
            textSugar.setGravity(Gravity.LEFT);
            textSugar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textTemperature = new TextView(this);
            textTemperature.setText("temperature " + "- " + morningSession.get("temperature").toString().replace("\"", ""));
            textTemperature.setTextSize(16);
            textTemperature.setGravity(Gravity.LEFT);
            textTemperature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textPulse = new TextView(this);
            textPulse.setText("Pulse Rate " + "- " + morningSession.get("pulse_rate").toString().replace("\"", ""));
            textPulse.setTextSize(16);
            textPulse.setGravity(Gravity.LEFT);
            textPulse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            morningLayout.addView(sessionName);
            morningLayout.addView(textBlood);
            morningLayout.addView(textSugar);
            morningLayout.addView(textTemperature);
            morningLayout.addView(textPulse);

        }

        if(afternoonSession !=null && afternoonSession.size() > 0){

            TextView sessionName = new TextView(this);
            sessionName.setText("Afternoon");
            sessionName.setTextSize(22);
            sessionName.setGravity(Gravity.LEFT);
            sessionName.setTypeface(null, Typeface.BOLD);
            sessionName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textBlood = new TextView(this);
            textBlood.setText("Blood Pressure " + "- " + afternoonSession.get("blood_pressure").toString().replace("\"", ""));
            textBlood.setTextSize(16);
            textBlood.setGravity(Gravity.LEFT);
            textBlood.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textSugar = new TextView(this);
            textSugar.setText("Sugar Level " + "- " + afternoonSession.get("sugar_level").toString().replace("\"", ""));
            textSugar.setTextSize(16);
            textSugar.setGravity(Gravity.LEFT);
            textSugar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textTemperature = new TextView(this);
            textTemperature.setText("temperature " + "- " + afternoonSession.get("temperature").toString().replace("\"", ""));
            textTemperature.setTextSize(16);
            textTemperature.setGravity(Gravity.LEFT);
            textTemperature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textPulse = new TextView(this);
            textPulse.setText("Pulse Rate " + "- " + afternoonSession.get("pulse_rate").toString().replace("\"", ""));
            textPulse.setTextSize(16);
            textPulse.setGravity(Gravity.LEFT);
            textPulse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            afternoonLayout.addView(sessionName);
            afternoonLayout.addView(textBlood);
            afternoonLayout.addView(textSugar);
            afternoonLayout.addView(textTemperature);
            afternoonLayout.addView(textPulse);

        }

        if(eveningSession !=null && eveningSession.size() > 0 ){

            TextView sessionName = new TextView(this);
            sessionName.setText("Evening");
            sessionName.setTextSize(22);
            sessionName.setGravity(Gravity.LEFT);
            sessionName.setTypeface(null, Typeface.BOLD);
            sessionName.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textBlood = new TextView(this);
            textBlood.setText("Blood Pressure " + "- " + eveningSession.get("blood_pressure").toString().replace("\"", ""));
            textBlood.setTextSize(16);
            textBlood.setGravity(Gravity.LEFT);
            textBlood.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textSugar = new TextView(this);
            textSugar.setText("Sugar Level " + "- " + eveningSession.get("sugar_level").toString().replace("\"", ""));
            textSugar.setTextSize(16);
            textSugar.setGravity(Gravity.LEFT);
            textSugar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textTemperature = new TextView(this);
            textTemperature.setText("temperature " + "- " + eveningSession.get("temperature").toString().replace("\"", ""));
            textTemperature.setTextSize(16);
            textTemperature.setGravity(Gravity.LEFT);
            textTemperature.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            TextView textPulse = new TextView(this);
            textPulse.setText("Pulse Rate " + "- " + eveningSession.get("pulse_rate").toString().replace("\"", ""));
            textPulse.setTextSize(16);
            textPulse.setGravity(Gravity.LEFT);
            textPulse.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            eveningLayout.addView(sessionName);
            eveningLayout.addView(textBlood);
            eveningLayout.addView(textSugar);
            eveningLayout.addView(textTemperature);
            eveningLayout.addView(textPulse);

        }




    }
}
