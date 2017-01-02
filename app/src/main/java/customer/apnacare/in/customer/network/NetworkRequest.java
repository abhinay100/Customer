package customer.apnacare.in.customer.network;

import android.util.Log;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by root on 30/12/16.
 */

public class NetworkRequest {

    private static final String TAG = "Customer.class";

    // Default error handling
    private static Action1<Throwable> mOnError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            if(throwable instanceof HttpException){
                int code = ((HttpException) throwable).response().code();
                Log.v(TAG,"NetworkRequest Response: "+((HttpException) throwable).response());

                if(code == 401){
                    //Log.v("Main.class","Not AUthorized ! Please Login to continue");
                }
            }
            Log.v(TAG, throwable.getMessage());
            throwable.printStackTrace();
        }
    };

    public static <T> Subscription performAsyncRequest(Observable<T> observable, Action1<? super T> onAction) {
        // Use default error handling
        return performAsyncRequest(observable, onAction, mOnError);
    }

    public static <T> Subscription performAsyncRequest(Observable<T> observable, Action1<? super T> onAction, Action1<Throwable> onError) {
        // Specify a scheduler (Scheduler.newThread(), Scheduler.immediate(), ...)
        // We choose Scheduler.io() to perform network request in a thread pool
        return observable.subscribeOn(Schedulers.io())
                // Observe result in the main thread to be able to update UI
                .observeOn(Schedulers.newThread()) // AndroidSchedulers.mainThread()
                // Set callbacks actions
                .subscribe(onAction, onError);
    }

}