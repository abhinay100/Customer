package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 28/12/16.
 */

public class CaseRecord extends RealmObject {
    @PrimaryKey
    public long id;

    private String crnNumber;
    private long branchId;
    public long patientId;
    private String careplanName;
    private String medicalConditions;
    private String medications;
    private int serviceId;
    private String serviceName;
    private String noOfHours;
    private float caseCharges;
    private String genderPreference;
    private String languagePreference;
    private int apnacareLead;
    private String status;
    private Date createdAt;

    @Ignore
    public Patient patient;

    public CaseRecord(){}

    public CaseRecord(JsonObject data) throws ParseException {
        this.id = data.get("id").getAsInt();
        this.crnNumber = data.get("file_no").getAsString();
        this.branchId = data.get("branch_id").getAsInt();
        this.patientId = data.get("patient_id").getAsLong();
        this.careplanName = data.get("careplan_name").getAsString();
        this.medicalConditions = data.get("medical_conditions").getAsString();
        this.medications = data.get("medications").getAsString();
        this.genderPreference = data.get("gender_preference").getAsString();
        this.languagePreference = data.get("language_preference").getAsString();
        this.serviceId = data.get("service_id").getAsInt();
        this.serviceName = data.get("service_name").getAsString();
        this.noOfHours = data.get("no_of_hours").getAsString();
        this.caseCharges = Float.valueOf(data.get("case_charges").getAsString().replace("\"",""));
        this.apnacareLead = data.get("apnacare_lead").getAsInt();
        this.status = data.get("status").getAsString();
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get("created_at").getAsString());
        this.createdAt = date;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCrnNumber() {
        return crnNumber;
    }

    public void setCrnNumber(String crnNumber) {
        this.crnNumber = crnNumber;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    public Patient getPatient(){
        return this.patient;
    }

    public long getPatientId(){
        return this.patientId;
    }

    public String getCareplanName() {
        return careplanName;
    }

    public void setCareplanName(String careplanName) {
        this.careplanName = careplanName;
    }

    public String getMedicalConditions() {
        return medicalConditions;
    }

    public void setMedicalConditions(String medicalConditions) {
        this.medicalConditions = medicalConditions;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getNoOfHours() {
        return noOfHours;
    }

    public void setNoOfHours(String noOfHours) {
        this.noOfHours = noOfHours;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public float getCaseCharges() {
        return caseCharges;
    }

    public void setCaseCharges(float caseCharges) {
        this.caseCharges = caseCharges;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public String getLanguagePreference() {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference) {
        this.languagePreference = languagePreference;
    }

    public int getApnacareLead() {
        return apnacareLead;
    }

    public void setApnacareLead(int apnacareLead) {
        this.apnacareLead = apnacareLead;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}