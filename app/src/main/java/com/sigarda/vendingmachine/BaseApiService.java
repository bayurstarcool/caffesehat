package com.sigarda.vendingmachine;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BaseApiService {

    @FormUrlEncoded
    @POST("/transaction")
    Call<ResponseBody> transaction(@Field("money") int money,
                                    @Field("product_id") int product_id
    );
    @GET("api/v1/{username}/images")
    Call<ResponseBody> getImagesByUsername(@Header("Authorization") String auth, @Path(value = "username") String username);
    @FormUrlEncoded
    @POST("v1/oauth/token")
    Call<ResponseBody> loginRequest(@Field("grant_type") String grant_type,
                                    @Field("client_id") String client_id,
                                    @Field("client_secret") String client_secret,
                                    @Field("username") String username,
                                    @Field("password") String password
    );
    @FormUrlEncoded
    @POST("products/{id}")
    Call<ResponseBody> updateProduct(@Path(value = "id") int id,
                                     @Field("price") int price,
                                    @Field("name") String name,
                                    @Field("stock") int stock
    );
    @GET("/feeds?next_cursor=36")
    Call<ResponseBody> getFeeds(
            @Header("Authorization") String auth,
            @Query("limit") String limit
    );
    @GET("/questions")
    Call<ResponseBody> getQuestions(
            @Header("Authorization") String auth,
            @Query("limit") String limit
    );
    @GET("/posts/{id}/comments")
    Call<ResponseBody> getComments(
            @Header("Authorization") String auth,
            @Path(value = "id") String id,
            @Query("limit") String limit
    );
    @GET("/posts/{id}/comments")
    Call<ResponseBody> getCommentsMore(
            @Header("Authorization") String auth,
            @Path(value = "id") String id,
            @Query("limit") String limit,
            @Query("next_cursor") int next_cursor
    );
    @GET("/feeds")
    Call<ResponseBody> getFeedsMore(
            @Header("Authorization") String auth,
            @Query("limit") String limit,
            @Query("next_cursor") int next_cursor
    );
    @GET("/questions")
    Call<ResponseBody> getQuestionsMore(
            @Header("Authorization") String auth,
            @Query("limit") String limit,
            @Query("next_cursor") int next_cursor
    );
    @GET("api/v1/chat/voyager")
    Call<ResponseBody> getRooms(@Header("Authorization") String auth);
    @GET("questions")
    Call<ResponseBody> getQuestions(@Header("Authorization") String auth);

    @GET("organizations?limit=10")
    Call<ResponseBody> getOrganizations(@Header("Authorization") String auth);

    @POST("api/v2/profile")
    Call<ResponseBody> getUser(@Header("Authorization") String auth);

    @GET("/products")
    Call<ResponseBody> getProducts();

    @GET("/monies")
    Call<ResponseBody> getMonies();

    @POST("/obtain_token")
    Call<ResponseBody> obtain(@Header("Authorization") String auth);

    @GET("voyager/users?limit=5")
    Call<ResponseBody> getUserByUsername(@Header("Authorization") String auth,@Query("query") String query);
}