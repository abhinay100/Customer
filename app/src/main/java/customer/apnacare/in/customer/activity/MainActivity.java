package customer.apnacare.in.customer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;

import butterknife.ButterKnife;
import customer.apnacare.in.customer.R;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setUpNavigation("Home");

    }

    public void openActivity(View v){
        //Log.v(Constants.TAG,"view id: "+v.getId());
        int id = v.getId();

        switch (id){
            case R.id.cardCases: startActivity(new Intent(MainActivity.this,CasesActivity.class)); break;
            case R.id.cardProfiles: startActivity(new Intent(MainActivity.this,PatientsActivity.class)); break;
            case R.id.cardMessage: startActivity(new Intent(MainActivity.this,MessagesActivity.class)); break;
            case R.id.cardBills: startActivity(new Intent(MainActivity.this,ProfileActivity.class)); break;
            case R.id.cardAccount: startActivity(new Intent(MainActivity.this,BillsActivity.class)); break;
            case R.id.cardShop: startActivity(new Intent(MainActivity.this,EshopActivity.class)); break;
        }

    }






}
