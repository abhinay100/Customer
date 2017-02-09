package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/2/17.
 */

public class Comments extends RealmObject {

    @PrimaryKey
    private long id;

    private long careplanId;
    private long userId;
    private String userType;
    private String userName;
    private String comment;
    private String createdAt;

    public Comments(){}

    public Comments (JsonObject data) throws ParseException{
        this.id = data.get("id").getAsLong();
        this.careplanId = data.get("careplan_id").getAsLong();
        this.userId = data.get("user_id").getAsLong();
        this.userType = data.get("user_type").getAsString();
        this.userName = data.get("user_name").getAsString();
        this.comment = data.get("comment").getAsString();
        this.createdAt = data.get("created_at").getAsString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCareplanId() {
        return careplanId;
    }

    public void setCareplanId(long careplanId) {
        this.careplanId = careplanId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
