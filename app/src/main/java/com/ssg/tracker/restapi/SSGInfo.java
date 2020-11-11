package com.ssg.tracker.restapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ssg.tracker.model.LoginBody;
import com.ssg.tracker.model.LoginResponse;

import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SSGInfo {


    @POST("login")
    Call<LoginResponse> doLogin(@Body LoginBody user);

    @Multipart
    @POST("login")
    Call<JsonObject> doLoginMultipart(@Part("api_id") RequestBody api_id, @Part("api_key") RequestBody api_key, @Part("email") RequestBody email, @Part("password") RequestBody password);

    @Multipart
    @POST("fbLogin")
    Call<JsonObject> dofbLofin(@Part("api_id") RequestBody api_id, @Part("api_key") RequestBody api_key, @Part("id") RequestBody id, @Part("s_email") RequestBody s_email, @Part("s_name") RequestBody s_name);

    @Multipart
    @POST("googleLogin")
    Call<JsonObject> dogoogleLofin(@Part("api_id") RequestBody api_id, @Part("api_key") RequestBody api_key, @Part("s_email") RequestBody s_email, @Part("s_name") RequestBody s_name);


    @GET("register")
    Call<JsonElement> dorRgisterNameMobile(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("email") String email, @Query("password") String password, @Query("name") String name, @Query("mobile") String mobile,@Query("ref_code") String ref_code);


    @GET("updateSocial")
    Call<JsonElement> dorRgisterSocialMobile(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("email") String email, @Query("password") String password, @Query("name") String name, @Query("mobile") String mobile, @Query("u_id") String userId,@Query("ref_code") String ref_code);


    @GET("update_username")
    Call<JsonElement> insertSexUsername(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("uid") String uid, @Query("username") String username, @Query("sex") String sex, @Query("mobile_verified") String ismobileveryfied);

    @GET("update_height")
    Call<JsonElement> insertBodyInfo(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("uid") String uid, @Query("ft") String ft, @Query("inc") String inc, @Query("age") String age, @Query("weight") String weight);

    @POST("fbLogin")
    Call<JsonElement> insertFbLogin(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("id") String id, @Query("s_email") String s_email, @Query("s_name") String s_name);

    @GET("resendOtp")
    Call<JsonElement> getOtpAgain(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("mobile") String mobile);

    @GET("postSteps")
    Call<JsonElement> postSteps(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id, @Query("cal") String cal, @Query("time") String time, @Query("steps") String steps);

    @GET("getVideoCat")
    Call<JsonElement> getworkout(@Query("api_key") String api_key, @Query("api_id") String api_id);

    @GET("getVideos")
    Call<JsonElement> getworkoutDetails(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("cat_id") String cat_id);

    @GET("getProductCat")
    Call<JsonElement> getproduct(@Query("api_key") String api_key, @Query("api_id") String api_id);

    @GET("getProducts")
    Call<JsonElement> getproductDetails(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("p_id") String p_id);


    @GET("getTxn")
    Call<JsonElement> getCoinHistory(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id);

    @GET("getProfile")
    Call<JsonElement> getProfile(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id);

    @GET("getFriends")
    Call<JsonElement> getFriends(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id);

    @GET("updateProfile")
    Call<JsonElement> updateProfile(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id, @Query("u_ft") String u_ft, @Query("u_in") String u_in, @Query("u_w") String u_w, @Query("u_a") String u_a);

    @GET("claimOffer")
    Call<JsonElement> claimOffer(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id, @Query("p_pcost") String p_pcost, @Query("pro_id") String pro_id);

    @GET("submitRating")
    Call<JsonElement> submitRation(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id, @Query("pro_id") String pro_id, @Query("rating") String rating);
   // to get sum of total from app installed(Km,cal,steps)
    @GET("getDashboard")
    Call<JsonElement> getDashboard(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id);

    @GET("getTodayCoins")
    Call<JsonElement> getTodayCoins(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String u_id);

    @GET("checkUser")
    Call<JsonElement> checkUser(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_email") String name, @Query("u_mobile") String mobile);
    @GET("resetPass")
    Call<JsonElement> resetPass(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String name, @Query("u_pwd") String mobile);

    @GET("invite")
    Call<JsonElement> invite(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String name);
    @GET("freeCoin")
    Call<JsonElement> freeCoin(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String name);
    @GET("bonusCoin")
    Call<JsonElement> bonusCoin(@Query("api_key") String api_key, @Query("api_id") String api_id, @Query("u_id") String name);

}
