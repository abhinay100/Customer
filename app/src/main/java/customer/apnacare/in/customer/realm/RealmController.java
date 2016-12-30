package customer.apnacare.in.customer.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import customer.apnacare.in.customer.model.CaseRecord;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by root on 29/12/16.
 */

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

//        realm.refresh();
        realm.waitForChange();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
//        realm.clear(CaseRecord.class);
//        realm.deleteAllFromRealm(CaseRecord.class);
        realm.where(CaseRecord.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    //find all objects in the Book.class
    public RealmResults<CaseRecord> getBooks() {

        return realm.where(CaseRecord.class).findAll();
    }

    //query a single item with the given id
    public CaseRecord getBook(String id) {

        return realm.where(CaseRecord.class).equalTo("id", id).findFirst();
    }

    //check if Book.class is empty
    public boolean hasBooks() {

//        return !realm.allObjects(CaseRecord.class).isEmpty();
        return !realm.where(CaseRecord.class).findAll().isEmpty();
    }

    //query example
    public RealmResults<CaseRecord> queryedBooks() {

        return realm.where(CaseRecord.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();

    }
}



