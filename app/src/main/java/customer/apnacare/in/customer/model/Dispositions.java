package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 10/2/17.
 */

public class Dispositions extends RealmObject {

    @PrimaryKey
    private long id;

    private long careplanId;
    private long userId;
    private String userName;
    private String status;
    private String dispositionDate;
    private String comment;
    private String createdAt;

    public Dispositions(){}

    public  Dispositions(JsonObject data) throws ParseException{
        this.id = data.get("id").getAsLong();
        this.careplanId = data.get("careplan_id").getAsLong();
        this.userId = data.get("user_id").getAsLong();
        this.userName = data.get("user_name").getAsString();
        this.status = data.get("status").getAsString();
        this.dispositionDate = data.get("disposition_date").getAsString();
        this.comment = data.get("comment").getAsString();
        this.createdAt = data.get("created_at").getAsString();
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDispositionDate() {
        return dispositionDate;
    }

    public void setDispositionDate(String dispositionDate) {
        this.dispositionDate = dispositionDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
