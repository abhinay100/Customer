package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 23/1/17.
 */

public class Caregiver extends RealmObject {
    @PrimaryKey
    private long id;

    private String employeeId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String maritalStatus;
    private String mobileNumber;
    private String email;
    private String currentAddress;
    private String currentCity;
    private String currentState;
    private String currentCountry;
    private String profileImage;
    private String workStatus;
    private String specialization;
    private String qualification;
    private int experience;
    private String languagesKnown;
    private String profileUrl;
    private String achievements;
    private String collegeName;

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public Caregiver(){}

    public Caregiver(JsonObject data) throws ParseException {
        this.id = data.get("id").getAsInt();
        this.employeeId = data.get("employee_id").getAsString();
        this.firstName = data.get("first_name").getAsString();
        this.lastName = data.get("last_name").getAsString();
        this.dateOfBirth = data.get("date_of_birth").getAsString();
        this.gender = data.get("gender").getAsString();
        this.mobileNumber = data.get("mobile_number").getAsString();
        this.email = data.get("email").getAsString();
        this.currentAddress = data.get("current_address").getAsString();
        this.currentCity = data.get("current_city").getAsString();
        this.currentState = data.get("current_state").getAsString();
        this.currentCountry = data.get("current_country").getAsString();
        this.profileImage = data.get("profile_image").getAsString();
        this.workStatus = data.get("work_status").getAsString();
        this.specialization = data.get("specialization").getAsString();
        this.qualification = data.get("qualification").getAsString();
        this.experience = data.get("experience").getAsInt();
        this.languagesKnown = data.get("languages_known").getAsString();
        this.achievements = data.get("achievements").getAsString();
        this.collegeName = data.get("college_name").getAsString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getCurrentCountry() {
        return currentCountry;
    }

    public void setCurrentCountry(String currentCountry) {
        this.currentCountry = currentCountry;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(String workStatus) {
        this.workStatus = workStatus;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getLanguagesKnown() {
        return languagesKnown;
    }

    public void setLanguagesKnown(String languagesKnown) {
        this.languagesKnown = languagesKnown;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}