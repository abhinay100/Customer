package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.security.PrivateKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/1/17.
 */

public class WorkLog extends RealmObject {

    @PrimaryKey
    private int id;

    private long careplanId;
    private long caregiverId;
    private String caregiverName;
    private String worklogDate;
    private String vitals;
    private String routines;
    private Date createdAt;

    public WorkLog(){}

    public WorkLog (JsonObject data) throws ParseException{
        this.id = data.get("id").getAsInt();
        this.careplanId = data.get("careplan_id").getAsLong();
        this.caregiverId = data.get("caregiver_id").getAsLong();
        this.caregiverName = data.get("caregiver_name").getAsString();
        this.worklogDate = data.get("worklog_date").getAsString();
        this.vitals = data.get("vitals").getAsString();
        this.routines = data.get("routines").getAsString();
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get("created_at").getAsString());

    }



    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCareplanId() {
        return careplanId;
    }

    public void setCareplanId(long careplanId) {
        this.careplanId = careplanId;
    }

    public String getCaregiverName() {
        return caregiverName;
    }

    public void setCaregiverName(String caregiverName) {
        this.caregiverName = caregiverName;
    }

    public long getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(long caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getWorklogDate() {
        return worklogDate;
    }

    public void setWorklogDate(String worklogDate) {
        this.worklogDate = worklogDate;
    }

    public String getVitals() {
        return vitals;
    }

    public void setVitals(String vitals) {
        this.vitals = vitals;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRoutines() {
        return routines;
    }

    public void setRoutines(String routines) {
        this.routines = routines;
    }
}
