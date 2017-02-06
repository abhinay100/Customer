package customer.apnacare.in.customer.model;

import com.google.gson.JsonObject;

import java.text.ParseException;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by root on 5/2/17.
 */

public class Documents extends RealmObject {

    @PrimaryKey
    private long caregiverId;

    private String documentName;
    private String documentURL;

    public Documents(){}

    public Documents(JsonObject data) throws ParseException {

        this.caregiverId = data.get("caregiver_id").getAsLong();
        this.documentName = data.get("document_name").getAsString();
        this.documentURL = data.get("document_path").getAsString();

    }

    public long getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(long caregiverId) {
        this.caregiverId = caregiverId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentURL() {
        return documentURL;
    }

    public void setDocumentURL(String documentURL) {
        this.documentURL = documentURL;
    }
}
