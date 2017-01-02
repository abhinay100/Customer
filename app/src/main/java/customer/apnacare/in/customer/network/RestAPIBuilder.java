package customer.apnacare.in.customer.network;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import customer.apnacare.in.customer.utils.Constants;
import customer.apnacare.in.customer.utils.CustomerApp;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 30/12/16.
 */

public class RestAPIBuilder {

    public static RestAPI buildRetrofitService() {


        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                String token = CustomerApp.preferences.getString("token", null);
                //Log.v(Constants.TAG,"token RestAPIBuilder: "+token);
                //this is where we will add whatever we want to our request headers.
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Authorization", "Bearer "+ token)
                        .addHeader("Accept", "application/x.apnacare.v1+json");

                Request request = requestBuilder.build();

                return chain.proceed(request);
            }
        }).connectTimeout(2, TimeUnit.MINUTES).readTimeout(2, TimeUnit.MINUTES).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                // Data converter
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();



        return retrofit.create(RestAPI.class);
    }
}

