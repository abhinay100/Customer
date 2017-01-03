package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 30/12/16.
 */

public class Patient extends RealmObject {
    @PrimaryKey
    public long id;

    private String firstName;
    private String lastName;
    private String gender;
    private String patientAge;
    private String patientWeight;
    private String streetAddress;
    private String area;
    private String city;
    private String zipcode;
    private String state;
    private String enquirerName;
    private String enquirerPhone;
    private String alternatePhone;

    public Patient(){}

    public Patient(JsonObject data) throws ParseException {
        this.id = data.get("id").getAsInt();
        this.firstName = data.get("first_name").getAsString();
        this.lastName = data.get("last_name").getAsString();
        this.gender = data.get("gender").getAsString();
        this.patientAge = data.get("patient_age").getAsString();
        this.patientWeight = data.get("patient_weight").getAsString();
        this.streetAddress = data.get("street_address").getAsString();
        this.area = data.get("area").getAsString();
        this.city = data.get("city").getAsString();
        this.zipcode = data.get("zipcode").getAsString();
        this.state = data.get("state").getAsString();
        this.enquirerName = data.get("enquirer_name").getAsString();
        this.enquirerPhone = data.get("enquirer_phone").getAsString();
        this.alternatePhone = data.get("alternate_number").getAsString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(String patientWeight) {
        this.patientWeight = patientWeight;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
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

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEnquirerName() {
        return enquirerName;
    }

    public void setEnquirerName(String enquirerName) {
        this.enquirerName = enquirerName;
    }

    public String getEnquirerPhone() {
        return enquirerPhone;
    }

    public void setEnquirerPhone(String enquirerPhone) {
        this.enquirerPhone = enquirerPhone;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }
}
