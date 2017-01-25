package customer.apnacare.in.customer.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import customer.apnacare.in.customer.R;
import customer.apnacare.in.customer.model.ServiceList;

/**
 * Created by root on 18/1/17.
 */

public class NewRequestActivity extends BaseActivity {

    Context mContext;
    EditText txtName, txtPhone, txtEmail;
    EditText txtArea, txtCity, txtState;
    Spinner careType;
    Button btnSubmitRequest;
    private ProgressDialog mDialog;
    ArrayList<ServiceList> serviceList;
    private int requestServiceID = 0;
    String name,phone,email,area,city,state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newrequest);

        mContext = this;
        mDialog = new ProgressDialog(mContext);
        setUpNavigation("New Service Request");

        serviceList = new ArrayList<>();
        serviceList.add(new ServiceList(0,"-- Select Service --"));
        serviceList.add(new ServiceList(1,"CLINIC VISIT"));
        serviceList.add(new ServiceList(2,"COUNSELOR"));
        serviceList.add(new ServiceList(3,"DENTAL CARE"));
        serviceList.add(new ServiceList(4,"EYE CARE"));
        serviceList.add(new ServiceList(5,"HOME VISIT - DOCTOR"));
        serviceList.add(new ServiceList(6,"HOME VISIT - NURSE"));
        serviceList.add(new ServiceList(7,"HOSPITALIZATION"));
        serviceList.add(new ServiceList(8,"LONG TERM NURSE"));
        serviceList.add(new ServiceList(9,"LONG TERM NURSE ASSISTANT"));
        serviceList.add(new ServiceList(10,"PHYSIOTHERAPY"));
        serviceList.add(new ServiceList(11,"SHORT TERM NURSE"));
        serviceList.add(new ServiceList(12,"SHORT TERM NURSE ASSISTANT"));
        serviceList.add(new ServiceList(13,"OLD AGE HOMES"));
        serviceList.add(new ServiceList(14,"BABY CARE"));
        serviceList.add(new ServiceList(15,"LAB TEST"));
        serviceList.add(new ServiceList(16,"PHARMACY"));
        serviceList.add(new ServiceList(17,"DIABETES SPECIALIST"));
        serviceList.add(new ServiceList(18,"AMBULANCE"));

        init();


    }

    public void init(){
        txtName = (EditText) findViewById(R.id.name);
        txtPhone = (EditText) findViewById(R.id.phone);
        txtEmail = (EditText) findViewById(R.id.email);
        txtArea = (EditText) findViewById(R.id.area);
        txtCity = (EditText) findViewById(R.id.city);
        txtState = (EditText) findViewById(R.id.state);

        // Fill the details from Preferences
//        txtName.setText(Customer.preferences.getString("profile_first_name","") + " " + Customer.preferences.getString("profile_last_name",""));
//        txtPhone.setText(Customer.preferences.getString("profile_phone",""));
//        txtEmail.setText(Customer.preferences.getString("profile_email",""));
//        txtArea.setText(Customer.preferences.getString("profile_address",""));
//        txtCity.setText(Customer.preferences.getString("profile_city",""));
//        txtState.setText(Customer.preferences.getString("profile_state",""));

        careType = (Spinner) findViewById(R.id.care_type);

        ArrayAdapter<ServiceList> adapter = new ArrayAdapter<ServiceList>(mContext, android.R.layout.simple_spinner_item, serviceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        careType.setAdapter(adapter);

        careType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ServiceList service = (ServiceList) adapterView.getSelectedItem();
                if(service.getId() != 0){
                    //Toast.makeText(mContext,service.getServiceName()+" "+service.getId(),Toast.LENGTH_SHORT).show();
                    requestServiceID = service.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmitRequest = (Button) findViewById(R.id.btnSubmitRequest);
        btnSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                submitRequest();
            }
        });
    }
}
