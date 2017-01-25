package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.text.Html;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

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

public class CaseTaskAdapter extends RealmBasedRecyclerViewAdapter<WorkLog, CaseTaskAdapter.ViewHolder> {

    Context mContext;
    TableLayout.LayoutParams tableLayoutParams;
    TableRow.LayoutParams tableRowParams;


    public CaseTaskAdapter(
            Context context,
            RealmResults<WorkLog> realmResults,
            boolean automaticUpdate,
            boolean animateIdType) {
        super(context, realmResults, automaticUpdate, animateIdType);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewtype) {

        ViewHolder vh = null;
        try {

            View v = inflater.inflate(R.layout.task_item_card,viewGroup,false);
            vh = new ViewHolder(v);

        }catch (Exception e){
            Log.v(Constants.TAG,"onCreateRealmViewHolder Exception: "+e.toString());
        }
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {

        final WorkLog workLog = realmResults.get(position);
        viewHolder.caseTaskDate.setText("10/01/2017");
        viewHolder.caseTaskName.setText("Krishna");

    }

    public class ViewHolder extends RealmViewHolder {

        @BindView(R.id.cards_view) CardView cardView;
        @BindView(R.id.lbl_caseTask_date) TextView caseTaskDate;
        @BindView(R.id.lblTaskName) TextView caseTaskName;
        @BindView(R.id.btnViewRoutine) Button btnViewRoutine;
        @BindView(R.id.btnViewVitals) Button btnViewVitals;


        public ViewHolder(View container) {
            super(container);
            ButterKnife.bind(this, container);
            btnViewRoutine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    WorkLog workLog = new WorkLog();
//                    JsonArray routines = new JsonParser().parse(workLog.getRoutines()).getAsJsonArray();
//                    Log.v(Constants.TAG,"routines size: "+routines.toString());
//                    TableLayout tasksTable = createTasksTableLayout(routines);
                    ScrollView sv = new ScrollView(mContext);
                    sv.setFillViewport(true);
                    sv.setScrollContainer(false);

                    //    sv.addView(tasksTable);

                    MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                            .title("Routines")
                            .customView(sv,true)
                            .positiveText("Ok")
                            .show();

                }
            });
            btnViewVitals.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ScrollView sv = new ScrollView(mContext);
                    sv.setFillViewport(true);
                    sv.setScrollContainer(false);

                    MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                            .title("Vitals")
                            .customView(sv,true)
                            .positiveText("Ok")
                            .show();

                }
            });

        }

        private TableLayout createTasksTableLayout(JsonArray routines){
            HashMap<Integer, String> sessions = new HashMap<>();
            sessions.put(0, "morning");
            sessions.put(1, "afternoon");
            sessions.put(2, "evening");
            sessions.put(3, "night");

            // 1) Create a tableLayout and its params
            tableLayoutParams = new TableLayout.LayoutParams();
            TableLayout tableLayout = new TableLayout(mContext);
            tableLayout.setBackgroundColor(Color.BLACK);

            // 2) create tableRow params
            tableRowParams = new TableRow.LayoutParams();
            tableRowParams.setMargins(1,1,1,1);
            tableRowParams.weight = 1;

            if(routines.size()> 0 ){
                JsonObject routinesObject = new JsonObject();
                routinesObject = routines.get(0).getAsJsonObject();
                int rowCount = routinesObject.size();
                for(int i=0;i<rowCount;i++){
                    JsonObject routine = routinesObject.get(sessions.get(i)).getAsJsonObject();

                    // Add Session Heading
                    TableRow tableRowHead = new TableRow(mContext);
                    TextView txt = new TextView(mContext);
                    txt.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
                    txt.setText(sessions.get(i).toUpperCase());
                    txt.setBackgroundColor(Color.WHITE);
                    txt.setTypeface(null, Typeface.BOLD);
                    txt.setTextColor(mContext.getResources().getColor(R.color.apnacareBlue));
                    txt.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

                    tableRowHead.addView(txt, tableRowParams);
                    tableLayout.addView(tableRowHead, tableLayoutParams);

                    if(routine.size() > 0){
                        for(Map.Entry<String, JsonElement> entry: routine.entrySet()){
                            TableRow tableRow = new TableRow(mContext);

                            TextView txtRoutine = new TextView(mContext);
                            txtRoutine.setText(entry.getKey().toString().replaceAll("\"",""));
                            txtRoutine.setBackgroundColor(Color.WHITE);
                            txtRoutine.setGravity(Gravity.LEFT);
                            tableRow.addView(txtRoutine, tableRowParams);

                            TextView txtRoutineTime = new TextView(mContext);
                            txtRoutineTime.setText(entry.getValue().toString().replaceAll("\"",""));
                            txtRoutineTime.setBackgroundColor(Color.WHITE);
                            txtRoutineTime.setGravity(Gravity.LEFT);
                            tableRow.addView(txtRoutineTime, tableRowParams);

                            tableLayout.addView(tableRow, tableLayoutParams);

                        }

                    }else{

                        TableRow tableRow = new TableRow(mContext);

                        TextView txtRoutine = new TextView(mContext);
                        txtRoutine.setText(Html.fromHtml("<i>No data.</i>"));
                        txtRoutine.setBackgroundColor(Color.WHITE);
                        txtRoutine.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        tableRow.addView(txtRoutine, tableRowParams);

                        tableLayout.addView(tableRow, tableLayoutParams);

                    }
                }

            }
            return tableLayout;
        }



    }
}
