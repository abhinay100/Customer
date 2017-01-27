package customer.apnacare.in.customer.network;

import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by root on 30/12/16.
 */

public interface RestAPI {

    @FormUrlEncoded
    @POST("auth/signup")
    Observable<JsonObject> signup(@Field("email") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/customerLogin")
    Observable<JsonObject> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("customer/profile")
    Observable<JsonObject> loadProfile(@Field("email") String email);

    @FormUrlEncoded
    @POST("cases/all")
    Observable<JsonObject> getCaseData(@Field("user_id") long userID, @Field("type") String type, @Field("new_case_ids") String caseID, @Field("existing_case_ids") String existing);

    @POST("request/new")
    Observable<JsonObject> newServiceRequest(@Body JsonObject requestData);

}

