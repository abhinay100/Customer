package customer.apnacare.in.customer.service;

import android.app.IntentService;
import android.content.Intent;

import io.realm.Realm;

/**
 * Created by root on 30/12/16.
 */


public class CaseDataService extends IntentService {

    public CaseDataService() {
        super("CaseDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Realm realm = Realm.getDefaultInstance();
    }
}
