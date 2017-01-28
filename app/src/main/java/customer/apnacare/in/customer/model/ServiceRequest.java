package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 30/12/16.
 */

public class ServiceRequest extends RealmObject {
    @PrimaryKey
    public long id;

    private String name;
    private String phoneNumber;
    private String email;
    private String area;
    private String city;
    private String state;
    private String service;

    public ServiceRequest(){}

    public ServiceRequest(JsonObject data) throws ParseException {
        this.id = data.get("id").getAsInt();
        this.name = data.get("name").getAsString();
        this.phoneNumber = data.get("phoneNumber").getAsString();
        this.email = data.get("email").getAsString();
        this.area = data.get("area").getAsString();
        this.city = data.get("city").getAsString();
        this.state = data.get("state").getAsString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
