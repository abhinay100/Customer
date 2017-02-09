package customer.apnacare.in.customer.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.WorkLog;
import customer.apnacare.in.customer.model.WorkLogFeedback;
import customer.apnacare.in.customer.service.DataSyncService;
import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

/**
 * Created by root on 10/1/17.
 */

public class CaseVitalsAdapter extends RealmBasedRecyclerViewAdapter<WorkLog, CaseVitalsAdapter.ViewHolder> {

    Context mContext;
    JsonArray vitals, routines;
    String strTime = "-";

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
        @BindView(R.id.lblRating) public TextView lblRating;
        @BindView(R.id.btnFeedback) public Button btnFeedback;
        @BindView(R.id.feedbackRating) RatingBar feedbackRating;

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
        Realm realm = Realm.getDefaultInstance();

        try {
            if(worklog != null) {
                viewHolder.vitalInspectionDate.setText(worklog.getWorklogDate());
                viewHolder.caregiverName.setText(worklog.getCaregiverName());

                WorkLogFeedback workLogFeedback = realm.where(WorkLogFeedback.class).equalTo("careplanId",worklog.getCareplanId()).findFirst();
                if(workLogFeedback != null) {
                    //Log.v(Constants.TAG,"feedback: "+workLogFeedback);
                    viewHolder.feedbackRating.setVisibility(View.VISIBLE);
                    viewHolder.feedbackRating.setRating(workLogFeedback.getRating());
                    viewHolder.feedbackRating.setIsIndicator(true);

                    viewHolder.lblRating.setVisibility(View.VISIBLE);
                    viewHolder.btnFeedback.setVisibility(View.GONE);
                }

                strTime = new SimpleDateFormat("HH:mm a").format(worklog.getCreatedAt()).toString();

                JsonParser parser = new JsonParser();

                if(!worklog.getVitals().isEmpty()) {
                    vitals = parser.parse(worklog.getVitals().toString()).getAsJsonArray();
                }

                if(!worklog.getRoutines().isEmpty() && !worklog.getRoutines().equals("")) {
                    routines = parser.parse(worklog.getRoutines().toString()).getAsJsonArray();
                }

//            Log.v(Constants.TAG,"vitals   : "+vitals);
//            Log.v(Constants.TAG,"routines : "+routines);

                viewHolder.btnViewVitalMorning.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createTasksWebView("morning");
                    }
                });

                viewHolder.btnViewVitalNoon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createTasksWebView("afternoon");
                    }
                });

                viewHolder.btnViewVitalEvening.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createTasksWebView("evening");
                    }
                });

                viewHolder.btnFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                                .title("Feedback")
                                .customView(R.layout.layout_feedback_view,true)
                                .positiveText("Submit")
                                .negativeText("Cancel")
                                .build();

                        materialDialog.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                RatingBar ratingBar = (RatingBar) materialDialog.getCustomView().findViewById(R.id.rating);
                                EditText comment = (EditText) materialDialog.getCustomView().findViewById(R.id.comment);

                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        WorkLogFeedback feedback = new WorkLogFeedback();
                                        long id = 0;
                                        if(realm.where(WorkLogFeedback.class).max("id") != null){
                                            id = realm.where(WorkLogFeedback.class).max("id").longValue();
                                        }
                                        feedback.setId((id + 1));
                                        feedback.setCareplanId(worklog.getCareplanId());
                                        feedback.setWorklogId(worklog.getId());
                                        feedback.setCustomerName(CustomerApp.preferences.getString("fullName",""));
                                        feedback.setRating(ratingBar.getRating());
                                        feedback.setComment(comment.getText().toString());
                                        feedback.setCreatedAt(new Date());

                                        realm.copyToRealmOrUpdate(feedback);

                                        Intent i = new Intent(mContext, DataSyncService.class);
                                        i.putExtra("serviceName","worklogFeedback");
                                        i.putExtra("feedbackID",(id + 1));
                                        mContext.startService(i);
                                    }
                                });

                                materialDialog.dismiss();

                                notifyDataSetChanged();
                            }
                        });

                        materialDialog.getActionButton(DialogAction.NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                materialDialog.dismiss();
                            }
                        });

                        materialDialog.show();
                    }
                });
            }
        }catch (Exception e){
            Log.v(Constants.TAG,"Exception: "+e.toString());
        }
    }

    private void createTasksWebView(String sessionName) {
        try {
            MaterialDialog materialDialog = new MaterialDialog.Builder(mContext)
                    .title(toTitleCase(sessionName) + " Tasks")
                    .customView(R.layout.layout_tasks_view, true)
                    .positiveText("Ok")
                    .build();

            View view = materialDialog.getCustomView();
            TextView progressBar = (TextView) view.findViewById(R.id.progressBar);
            WebView tasksWebView = (WebView) view.findViewById(R.id.tasksView);

            progressBar.setVisibility(View.VISIBLE);

            materialDialog.show();

            tasksWebView.getSettings().setJavaScriptEnabled(false);
            tasksWebView.getSettings().setSupportZoom(false);
            tasksWebView.getSettings().setBuiltInZoomControls(false);

            String borderCss = " style='border: 1px solid silver; padding: 5px'";
            String htmlString = "";
            String emptyDataRow = "<tr height='20'><td colspan='2' " + borderCss + "><center>No data</center></td></tr>";

            // Prepare Vitals Table
            htmlString = "<table style='width: 100%; text-align: left;'>";
            htmlString += "<tr height='20'><th colspan='2'><center>Vitals</center></th></tr>";
            if (vitals != null && vitals.size() > 0) {
                JsonObject vitalObject = vitals.get(0).getAsJsonObject();
                if (vitalObject.getAsJsonObject().get(sessionName).isJsonObject()) {
                    JsonObject sessionObject = (JsonObject) vitalObject.getAsJsonObject().get(sessionName);

                    if (sessionObject.size() > 0) {
                        htmlString += "<tr><td colspan='2'><center>Captured at " + strTime+ "</center></td></tr>";
                        htmlString += "<tr><th " + borderCss + ">Blood Pressure</th><td " + borderCss + ">" + sessionObject.get("blood_pressure").toString().replace("\"", "") + "</td></tr>";
                        htmlString += "<tr><th " + borderCss + ">Sugar Level</th><td " + borderCss + ">" + sessionObject.get("sugar_level").toString().replace("\"", "") + "</td></tr>";
                        htmlString += "<tr><th " + borderCss + ">Temperature</th><td " + borderCss + ">" + sessionObject.get("temperature").toString().replace("\"", "") + "</td></tr>";
                        htmlString += "<tr><th " + borderCss + ">Pulse Rate</th><td " + borderCss + ">" + sessionObject.get("pulse_rate").toString().replace("\"", "") + "</td></tr>";
                    } else {
                        htmlString += emptyDataRow;
                    }
                } else {
                    htmlString += emptyDataRow;
                }
            } else {
                htmlString += emptyDataRow;
            }
            htmlString += "</table><br>";

            // Prepare Routines Table
            htmlString += "<table style='width: 100%; text-align: left;'>";
            htmlString += "<tr height='20'><th colspan='2'><center>Routines</center></th></tr>";
            if (routines != null && routines.size() > 0) {
                JsonObject routinesObject = routines.get(0).getAsJsonObject();
                if (routinesObject.getAsJsonObject().get(sessionName).isJsonObject()) {
                    JsonObject sessionObject = (JsonObject) routinesObject.getAsJsonObject().get(sessionName);

                    if (sessionObject.size() > 0) {
                        for (Map.Entry<String, JsonElement> entry : sessionObject.entrySet()) {
                            // Add Routine Name
                            htmlString += "<tr><th " + borderCss + ">" + toTitleCase(entry.getKey().toString().replaceAll("\"", "")) + "</th>";

                            // Add Routine Timestamp
                            htmlString += "<td " + borderCss + ">" + entry.getValue().toString().replaceAll("\"", "") + "</td></tr>";
                        }
                    } else {
                        htmlString += emptyDataRow;
                    }
                }
            } else {
                htmlString += emptyDataRow;
            }
            htmlString += "</table>";

            tasksWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    if (materialDialog != null && materialDialog.isShowing()) {
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

            tasksWebView.loadData(htmlString, "text/html; charset=utf-8", "UTF-8");
        }catch (Exception e){
            Log.v(Constants.TAG,"createTasksWebView Exception: "+e.toString());
        }
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
