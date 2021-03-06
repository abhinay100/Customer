package customer.apnacare.in.customer.network;

import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    @POST("patient/update")
    Observable<JsonObject> updateProfile(@Body JsonObject requestData);

    @FormUrlEncoded
    @POST("worklog/feedback")
    Observable<JsonObject> worklogFeedback(@Field("careplan_id") long careplanID, @Field("worklog_id") long worklogID, @Field("customer_name") String customerName, @Field("rating") float rating, @Field("comment") String comment);

    @FormUrlEncoded
    @POST("customer/bills")
    Observable<JsonObject> getMyBills(@Field("email") String email);

    @GET("caregiver/document/all")
    Observable<JsonObject> getDocuments(@Query("id") long caregiverId);



}

