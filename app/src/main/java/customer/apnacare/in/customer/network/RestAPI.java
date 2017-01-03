package customer.apnacare.in.customer.network;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by root on 30/12/16.
 */

public interface RestAPI {

    @FormUrlEncoded
    @POST("auth/login")
    Observable<JsonObject> login(@Field("email") String username, @Field("password") String password, @Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("cases/all")
    Observable<JsonObject> getCaseData(@Field("user_id") int userID, @Field("type") String type, @Field("new_case_ids") String caseID, @Field("existing_case_ids") String existing);

    // --------------------------------------------------------------------------------------------------------------------------------------------//

    @FormUrlEncoded
    @POST("provider/registerDevice")
    Observable<JsonObject> registerDevice(@Field("provider_id") int providerID, @Field("token") String token);



    @POST("provider/saveProfile")
    Observable<JsonObject> saveProfile(@Body JsonObject profile);

    @GET
    Call<ResponseBody> downloadProfileImage(@Url String fileUrl);


    @FormUrlEncoded
    @POST("provider/caregivers")
    Observable<JsonObject> getCaregivers(@Field("user_id") int providerID, @Field("existing_emp_ids") String existing);

    @FormUrlEncoded
    @POST("provider/getEmployeeProfile")
    Call<ResponseBody> getEmployeeProfile(@Field("professional_id") int professionalID);

    @FormUrlEncoded
    @POST("provider/updateCaseProviderStatus")
    Observable<JsonObject> updateCaseProviderStatus(@Field("provider_id") int providerID, @Field("case_id") String caseID, @Field("status") String status);

    @GET("provider/caseComments")
    Observable<JsonObject> getCaseComments(@Query("case_id") String caseID);

    @POST("provider/addCaseComment")
    Observable<JsonObject> addCaseComment(@Body JsonObject comment);

    @GET("provider/caseAssessment")
    Observable<JsonObject> getCaseAssessment(@Query("case_id") String caseID);

    @POST("provider/addCaseAssessment")
    Observable<JsonObject> addCaseAssessment(@Body JsonObject assessment);

    @GET("provider/getWorkLog")
    Observable<JsonObject> getWorkLog(@Query("case_id") String caseID);

    @FormUrlEncoded
    @POST("provider/cases/assignCareGiver")
    Observable<JsonObject> assignCareTaker(@Field("case_id") String caseID, @Field("caretaker_id") int careTakerID);

}

