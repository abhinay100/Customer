package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/1/17.
 */

public class CaseVitalsAdapter extends RealmBasedRecyclerViewAdapter<WorkLog, CaseVitalsAdapter.ViewHolder> {

    Context mContext;
    TableLayout.LayoutParams tableLayoutParams;
    TableRow.LayoutParams tableRowParams;

    public CaseVitalsAdapter(
            Context context,
            RealmResults<WorkLog> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.lbl_caseTask_date) TextView vitalInspectionDate;
        @BindView(R.id.lblTaskName) TextView caregiverName;
        @BindView(R.id.btnViewMorning) public Button btnViewVitalMorning;
        @BindView(R.id.btnViewAfternoon) public Button btnViewVitalNoon;
        @BindView(R.id.btnViewEvening) public Button btnViewVitalEvening;

        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
        }
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        ViewHolder vh = null;
        try {
            View v = inflater.inflate(R.layout.task_item_card, viewGroup, false);
            vh = new ViewHolder(v);
        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }

        return  vh;
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final WorkLog worklog = realmResults.get(position);

        try {
            if(worklog != null) {
                viewHolder.vitalInspectionDate.setText(worklog.getWorklogDate());
                viewHolder.caregiverName.setText(worklog.getCaregiverName());

                viewHolder.btnViewVitalMorning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JsonParser parser = new JsonParser();
                        JsonArray vitals = parser.parse(worklog.getVitals().toString()).getAsJsonArray();

                        createVitalsTableLayout(vitals, "morning");
                    }
                });

                viewHolder.btnViewVitalNoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JsonParser parser = new JsonParser();
                        JsonArray vitals = parser.parse(worklog.getVitals().toString()).getAsJsonArray();

                        createVitalsTableLayout(vitals, "afternoon");
                    }
                });

                viewHolder.btnViewVitalEvening.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        JsonParser parser = new JsonParser();
                        JsonArray vitals = parser.parse(worklog.getVitals().toString()).getAsJsonArray();

                        createVitalsTableLayout(vitals, "evening");
                    }
                });
            }
        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }
    }

    private void createVitalsTableLayout(JsonArray vitals, String sessionName) {
        // 1) Create a tableLayout and its params
        tableLayoutParams = new TableLayout.LayoutParams();
        TableLayout tableLayout = new TableLayout(mContext);
        tableLayout.setBackgroundColor(Color.BLACK);

        // 2) create tableRow params
        tableRowParams = new TableRow.LayoutParams();
        tableRowParams.setMargins(1, 1, 1, 1);
        tableRowParams.weight = 1;


        JsonObject vitalObject = vitals.get(0).getAsJsonObject();
        if(vitalObject.getAsJsonObject().get(sessionName).isJsonObject()){
            JsonObject morningSession = (JsonObject) vitalObject.getAsJsonObject().get(sessionName);

            TableRow tableHeaderRow = new TableRow(mContext);
            tableHeaderRow.setBackgroundColor(Color.GRAY);

            TextView lblSessionName = new TextView(mContext);
            lblSessionName.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            lblSessionName.setText(toTitleCase(sessionName));
            lblSessionName.setTextColor(Color.WHITE);
            lblSessionName.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

            tableHeaderRow.addView(lblSessionName);
            tableLayout.addView(tableHeaderRow);

            tableLayout = addVitalParameters(tableLayout,morningSession);

            ScrollView sv = new ScrollView(mContext);
            sv.setFillViewport(true);
            sv.setScrollContainer(false);
            sv.addView(tableLayout);

            MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                    .title("Vitals")
                    .customView(sv,true)
                    .positiveText("Ok")
                    .show();
        }else{
            MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                    .title("Vitals")
                    .content("No vital data collected for this session")
                    .positiveText("Ok")
                    .show();
        }
    }

    public TableLayout addVitalParameters(TableLayout tableLayout, JsonObject session ){
        TableRow tableRow1 = new TableRow(mContext);

        // Add BP Before Food
        TextView txt11 = new TextView(mContext);
        txt11.setText("Blood Pressure");
        txt11.setBackgroundColor(Color.WHITE);
        txt11.setGravity(Gravity.LEFT);

        tableRow1.addView(txt11, tableRowParams);

        // Add Value
        TextView txt12 = new TextView(mContext);
        txt12.setText(session.get("blood_pressure").toString().replace("\"",""));
        txt12.setBackgroundColor(Color.WHITE);
        txt12.setGravity(Gravity.CENTER);

        tableRow1.addView(txt12, tableRowParams);

        tableLayout.addView(tableRow1, tableLayoutParams);

        TableRow tableRow3 = new TableRow(mContext);
        // Add Sugar Level Before Food
        TextView txt31 = new TextView(mContext);
        txt31.setText("Sugar Level");
        txt31.setBackgroundColor(Color.WHITE);
        txt31.setGravity(Gravity.LEFT);

        tableRow3.addView(txt31, tableRowParams);

        // Add Value
        TextView txt32 = new TextView(mContext);
        txt32.setText(session.get("sugar_level").toString().replace("\"",""));
        txt32.setBackgroundColor(Color.WHITE);
        txt32.setGravity(Gravity.CENTER);

        tableRow3.addView(txt32, tableRowParams);

        tableLayout.addView(tableRow3, tableLayoutParams);

        TableRow tableRow5 = new TableRow(mContext);
        // Add Temperature
        TextView txt51 = new TextView(mContext);
        txt51.setText("Temperature");
        txt51.setBackgroundColor(Color.WHITE);
        txt51.setGravity(Gravity.LEFT);

        tableRow5.addView(txt51, tableRowParams);

        // Add Value
        TextView txt52 = new TextView(mContext);
        txt52.setText(session.get("temperature").toString().replace("\"",""));
        txt52.setBackgroundColor(Color.WHITE);
        txt52.setGravity(Gravity.CENTER);

        tableRow5.addView(txt52, tableRowParams);

        tableLayout.addView(tableRow5, tableLayoutParams);

        TableRow tableRow6 = new TableRow(mContext);
        // Add Pulse Rate
        TextView txt61 = new TextView(mContext);
        txt61.setText("Pulse Rate");
        txt61.setBackgroundColor(Color.WHITE);
        txt61.setGravity(Gravity.LEFT);

        tableRow6.addView(txt61, tableRowParams);

        // Add Value
        TextView txt62 = new TextView(mContext);
        txt62.setText(session.get("pulse_rate").toString().replace("\"",""));
        txt62.setBackgroundColor(Color.WHITE);
        txt62.setGravity(Gravity.CENTER);

        tableRow6.addView(txt62, tableRowParams);

        tableLayout.addView(tableRow6, tableLayoutParams);

        //Log.v(Constants.TAG," key: " + entry.getKey() + "  value: "+ entry.getValue());

        return tableLayout;
    }

    public String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
}
