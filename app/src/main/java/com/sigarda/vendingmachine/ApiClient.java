package com.sigarda.vendingmachine;


import android.text.TextUtils;



import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static final String BASE_URL = "https://vmachine.sigarda.com";

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit =builder.build();

        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor =
                    new AuthenticationInterceptor(authToken);

            if (!httpClient.interceptors().contains(interceptor)) {
                httpClient.addInterceptor(interceptor);
                builder.client(httpClient.build());
                retrofit = builder.build();
            }
        }
        return retrofit.create(serviceClass);
    }

    public static class RetrofitClient {

        private static Retrofit retrofit = null;

        public static Retrofit getClient(String baseUrl) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client)
                        .build();
            }
            return retrofit;
        }
    }

    public static class UtilsApi {

        // 10.0.2.2 ini adalah localhost.
        public static final String BASE_URL_API = BASE_URL;

        // Mendeklarasikan Interface BaseApiService
        public static BaseApiService getAPIService(){
            return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
        }
    }
}