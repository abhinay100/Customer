package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/1/17.
 */

public class WorkLog extends RealmObject {

    @PrimaryKey
    private  int id;

    @SerializedName("worklog_id")
    private int worklogId;

    @SerializedName("professional_id")
    private int professionalID;

    @SerializedName("case_id")
    private String caseID;

    @SerializedName("check_in")
    private String checkIn;

    @SerializedName("check_out")
    private String checkOut;

    @SerializedName("routines")
    private String routines;

    @SerializedName("vitals")
    private String vitals;

    @SerializedName("tasks")
    private String tasks;

    @SerializedName("current_status")
    private String currentStatus;

    @SerializedName("created_at")
    private String createdAt;

    public WorkLog(){}

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getVitals() {
        return vitals;
    }

    public void setVitals(String vitals) {
        this.vitals = vitals;
    }

    public String getRoutines() {
        return routines;
    }

    public void setRoutines(String routines) {
        this.routines = routines;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCaseID() {
        return caseID;
    }

    public void setCaseID(String caseID) {
        this.caseID = caseID;
    }

    public int getProfessionalID() {
        return professionalID;
    }

    public void setProfessionalID(int professionalID) {
        this.professionalID = professionalID;
    }

    public int getWorklogId() {
        return worklogId;
    }

    public void setWorklogId(int worklogId) {
        this.worklogId = worklogId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorkLog(JsonObject data){
        if(data.isJsonObject()){
            this.worklogId = data.get("id").getAsInt();
            this.professionalID = data.get("professional_id").getAsInt();
            this.caseID = data.get("case_id").getAsString();
            this.checkIn = data.get("check_in").getAsString();
            this.checkOut = data.get("check_out").getAsString();
            this.routines = data.get("routines").getAsString();
            this.vitals = data.get("vitals").getAsString();
            this.tasks = data.get("tasks").getAsString();
            this.currentStatus = data.get("current_status").getAsString();
            this.createdAt = data.get("created_at").getAsString();
        }
    }


}
