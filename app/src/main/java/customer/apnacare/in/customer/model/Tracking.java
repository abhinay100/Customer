package customer.apnacare.in.customer.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by suhas on 3/9/16.
 */
@IgnoreExtraProperties
public class Tracking {

    public double lat;
    public double lng;
    public String user;
    public Date timestamp;

    public Tracking() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Tracking(LatLng latLng, String user) {
        this.lat = latLng.latitude;
        this.lng = latLng.longitude;
        this.user = user;
        this.timestamp = new Date();
    }

}
