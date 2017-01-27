package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 25/1/17.
 */

public class Assessment extends RealmObject {
    @PrimaryKey
    private long id;


    private long careplanId;
    private long userId;
    private String formData;
    private Date createdAt;


    public Assessment(){}

    public Assessment (JsonObject data) throws ParseException {
        this.id = data.get("id").getAsInt();
        this.careplanId = data.get("careplan_id").getAsLong();
        this.userId = data.get("user_id").getAsLong();
        this.formData = data.get("assess_form_data").getAsString();
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFormData() {
        return formData;
    }

    public void setFormData(String formData) {
        this.formData = formData;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


}