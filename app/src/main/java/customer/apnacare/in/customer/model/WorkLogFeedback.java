package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/1/17.
 */

public class WorkLogFeedback extends RealmObject {

    @PrimaryKey
    private long id;

    private long careplanId;
    private long worklogId;
    private String customerName;
    private float rating;
    private String comment;
    private Date createdAt;

    public WorkLogFeedback(){}

    public WorkLogFeedback(JsonObject data) throws ParseException{
        this.id = data.get("id").getAsInt();
        this.careplanId = data.get("careplan_id").getAsLong();
        this.worklogId = data.get("worklog_id").getAsLong();
        this.customerName = data.get("customer_name").getAsString();
        this.rating = data.get("rating").getAsFloat();
        this.comment = data.get("comment").getAsString();
        this.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get("created_at").getAsString());

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCareplanId() {
        return careplanId;
    }

    public void setCareplanId(long careplanId) {
        this.careplanId = careplanId;
    }

    public long getWorklogId() {
        return worklogId;
    }

    public void setWorklogId(long worklogId) {
        this.worklogId = worklogId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
