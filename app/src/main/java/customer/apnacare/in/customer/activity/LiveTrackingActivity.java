package customer.apnacare.in.customer.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.Caregiver;
import customer.apnacare.in.customer.model.Tracking;
import customer.apnacare.in.customer.utils.Constants;
import io.realm.Realm;

public class LiveTrackingActivity extends BaseActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    public Geocoder geocoder;
    List<Address> addresses;

    Context mContext;

    private DatabaseReference mDatabase;
    private ChildEventListener childEventListener;

    protected boolean isFirstMarker = true;

    Realm realm;

    String selectedEmployee = "";
    boolean isMarkerRunning = false;
    boolean isLocationReceived = false;
    Caregiver caregiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mContext = this;

        realm = Realm.getDefaultInstance();
        caregiver = realm.where(Caregiver.class).findFirst();

        if(caregiver != null) {
            initLiveTracking(caregiver.getId());

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something here
                    hideProgressBar();

                    Log.v(Constants.TAG, "isLocationReceived: " + isLocationReceived);

                    if (!isLocationReceived) {
                        MaterialDialog md = new MaterialDialog.Builder(mContext).content("Could not get tracking data!").positiveText("Ok").build();
                        md.getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                redirect();
                            }
                        });

                        md.show();
                    }
                }
            }, 10000);
        }else{
            new MaterialDialog.Builder(mContext)
                    .content("Sorry! Tracking data not available!")
                    .positiveText("Ok")
                    .show()
                    .getActionButton(DialogAction.POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            redirect();
                        }
                    });
        }
    }

    public void redirect(){
        startActivity(new Intent(LiveTrackingActivity.this,MainActivity.class));
    }

    public void initLiveTracking(long userID){
        selectedEmployee = caregiver.getFirstName() + " " + caregiver.getLastName();
        setUpNavigation("Live Tracking - " + selectedEmployee);

        showProgressBar("Fetching Location");

        // Initialize Database
        mDatabase = FirebaseDatabase.getInstance().getReference()
                .child("tracking").child(String.valueOf(userID));

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(caregiver != null) {
            mMap = googleMap;

            // Attach Live Tracking Listener
            try {
                childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.v(Constants.TAG, "onChildAdded:" + dataSnapshot.getKey());
                        isLocationReceived = true;

                        hideProgressBar();

                        if (dataSnapshot.getValue() != null) {
                            Tracking tracking = dataSnapshot.getValue(Tracking.class);
                            try {
                                addMarker(tracking);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.v(Constants.TAG, "onChildChanged:" + dataSnapshot.getKey());
                        isLocationReceived = true;
                        hideProgressBar();

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Log.v(Constants.TAG, "onChildRemoved:" + dataSnapshot.getKey());
                        isLocationReceived = true;
                        hideProgressBar();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                        Log.v(Constants.TAG, "onChildMoved:" + dataSnapshot.getKey());
                        isLocationReceived = true;
                        hideProgressBar();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.v(Constants.TAG, "locationTrackingListener:onCancelled", databaseError.toException());
                        isLocationReceived = true;
                        hideProgressBar();
                    }
                };

                mDatabase.addChildEventListener(childEventListener);

            } catch (Exception e) {
                Log.e(Constants.TAG, "childEventListener Exception: " + e.toString());
                hideProgressBar();
            }
        }
    }


    protected void addMarker(Tracking trackingData) throws ParseException {
//        Runnable updateMarker = new Runnable() {
//            @Override
//            public void run() {
//                marker.remove();
//                marker = map.addMarker(new MarkerOptions().position(location));
//
//                handler.postDelayed(this, MARKER_UPDATE_INTERVAL);
//            }
//        };

        double latitude = trackingData.lat;
        double longitude = trackingData.lng;
        String timestamp = "-";
        String address = "-";

        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.location_circle_yellow);

        if (isFirstMarker) {
            markerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN);
            isFirstMarker = false;
        }

        try {
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            address = addresses.get(0).getAddressLine(0) + " \n" + addresses.get(0).getAddressLine(1) + " \n" + addresses.get(0).getLocality();
        } catch (Exception e) {
            Log.v(Constants.TAG, "geoencoder exception: " + e.toString());
        }

        if (trackingData.timestamp != null)
            timestamp = new SimpleDateFormat("hh:mm a").format(trackingData.timestamp);

        //Adding marker to map
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .icon(markerIcon)
                .flat(true)     // Set flat true
                .title(Html.fromHtml("<b>" + selectedEmployee + "</b>").toString())
                .snippet("Registered Nurse"));

        String finalTimestamp = timestamp;
        String finalAddress = address;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String snippet = "<b> " + selectedEmployee + "</b><br><br> <b>Current Location: </b> " + finalAddress + "<br><br> <b>Last Seen at:</b> " + finalTimestamp;
                try {
                    new MaterialDialog.Builder(mContext)
                            .title("Tracking Details")
                            .content(Html.fromHtml(snippet))
                            .positiveText("Ok")
                            .show();
                } catch (Exception e) {

                }
            }
        });

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        mMap = null;
        if(mDatabase != null)
            mDatabase.removeEventListener(childEventListener);
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_tracking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //no inspection SimplifiableIfStatement
        if (id == R.id.action_close) {
            startActivity(new Intent(LiveTrackingActivity.this, MainActivity.class));
            moveTaskToBack(false);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

}