package com.sigarda.vendingmachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sigarda.vendingmachine.Utils.Constant_Api;
import com.sigarda.vendingmachine.Utils.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    final int PermissionMemory = 100;
    final int PermissionLocation = 101;
    final int PermissionCamera = 102;
    final int PermissionReadPhone = 103;
    // splash screen timer
    private static int SPLASH_TIME_OUT = 100;
    private Boolean isCancelled = false;

    //private ProgressBar progressBar;
    private Window mWindow;
    private TextView message;
    private FrameLayout frameLayout;
    private Animation animation;
    private Method method;
    String event_id = "0";
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        method = new Method(SplashScreen.this);
        mApiService = ApiClient.UtilsApi.getAPIService();
        splash();
    }


    public void splash(){
        if (Method.isNetworkAvailable(SplashScreen.this)) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    obtainToken();
                }
            }, SPLASH_TIME_OUT);
        } else {
            method.editor.putBoolean(method.pref_login, false);
            method.editor.commit();
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();

        }
    }
    private  void obtainToken(){
        String token = "Bearer "+method.pref.getString(method.access_token, null);
        Log.d("TOKENS",token);
        mApiService.obtain(token)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonObject = new JSONObject(response.body().string());
                                Log.d("HAsil",response.toString());
                                JSONObject result = jsonObject.getJSONObject("result");
                                String success = jsonObject.getString("success");

                                if (success.equals("true")) {
                                    method.editor.putBoolean(method.pref_login, true);
                                    method.editor.putString(method.userEmail, result.getString("email"));
                                    method.editor.putString(method.fullname,result.getString("name"));
                                    method.editor.commit();
                                    Constant_Api.token = method.pref.getString(method.token, null);
                                    Constant_Api.username = method.pref.getString(method.userEmail, null);
                                    if(result.getString("email")=="wahyu@otakkanan.co.id"){
                                        Intent intent = new Intent(SplashScreen.this, ProductActivity.class).putExtra("fullname",result.getString("name"));
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Intent intent = new Intent(SplashScreen.this, MainActivity.class).putExtra("fullname",result.getString("name"));
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    method.editor.putBoolean(method.pref_login, false);
                                    method.editor.commit();
                                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SplashScreen.this,"Unauthorized",Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                Toast.makeText(SplashScreen.this,"Unauthorized",Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(SplashScreen.this,"Unauthorized",Toast.LENGTH_LONG).show();
                            Log.d("Errornya",response.toString());
                            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(SplashScreen.this,"Unauthorized",Toast.LENGTH_LONG).show();
                        Log.d("errornya",t.toString());

                        method.editor.putBoolean(method.pref_login, false);
                        method.editor.commit();
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        finish();
                    }
                });
    }
}