package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 9/1/17.
 */

public class Bills extends RealmObject {

    @PrimaryKey
    private int id;

    private long careplanId;
    private String invoiceNo;
    private String invoicePeriodFrom;
    private String invoicePeriodTo;
    private String invoiceDate;
    private String caseCharges;
    private String invoiceAmount;
    private String status;

    public Bills(){}

    public Bills(JsonObject data) throws ParseException{
        this.id = data.get("id").getAsInt();
        this.careplanId = data.get("careplan_id").getAsLong();
        this.invoiceNo = data.get("invoice_no").getAsString();
        this.invoicePeriodFrom = data.get("invoice_period_from").getAsString();
        this.invoicePeriodTo = data.get("invoice_period_to").getAsString();
        this.invoiceDate = data.get("invoice_date").getAsString();
        this.caseCharges = data.get("case_charges").getAsString();
        this.invoiceAmount = data.get("invoice_amount").getAsString();
        this.status = data.get("status").getAsString();
    }

    public int getId() {
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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoicePeriodFrom() {
        return invoicePeriodFrom;
    }

    public void setInvoicePeriodFrom(String invoicePeriodFrom) {
        this.invoicePeriodFrom = invoicePeriodFrom;
    }

    public String getInvoicePeriodTo() {
        return invoicePeriodTo;
    }

    public void setInvoicePeriodTo(String invoicePeriodTo) {
        this.invoicePeriodTo = invoicePeriodTo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCaseCharges() {
        return caseCharges;
    }

    public void setCaseCharges(String caseCharges) {
        this.caseCharges = caseCharges;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
