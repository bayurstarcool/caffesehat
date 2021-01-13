package com.sigarda.vendingmachine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sigarda.vendingmachine.Utils.Method;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editText_username,editText_password;
    private Method method;
    private Button btnLogin;
    private String username,password;
    BaseApiService mApiService;
    private String TAG ="KKK";
    private ProgressDialog progressDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        method = new Method(LoginActivity.this);
        editText_username = findViewById(R.id.text_email);
        editText_password = findViewById(R.id.text_password);
        mApiService = ApiClient.UtilsApi.getAPIService(); // meng-init yang ada di package apihelper

        String token = "Bearer "+method.pref.getString(method.token, null);
        mApiService = ApiClient.UtilsApi.getAPIService();
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editText_username.getText().toString();
                password = editText_password.getText().toString();
                login(username,password);
            }
        });
    }
    public void login(String username, String password) {

        editText_username.setError(null);
        editText_password.setError(null);

        if (username.isEmpty()) {
            editText_username.requestFocus();
            editText_username.setError("Masukkan Email");
        } else if (password.isEmpty()) {
            editText_password.requestFocus();
            editText_password.setError("Masukkan Password");
        } else {
            if (Method.isNetworkAvailable(LoginActivity.this)) {
                doLogin(username, password);
            } else {
                Toast.makeText(LoginActivity.this, "Jaringan error", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void doLogin(String username, String password){
        showProgressBar();
        mApiService.loginRequest("password","3","BvDYtEoIgxZIh9YlDy76iJx5ChGQQALqjExa676N",username,password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                Log.d("JJJ",jsonRESULTS.toString());
                                String access_token = jsonRESULTS.getString("access_token");
                                method.editor.putString(method.access_token,access_token);
                                method.editor.commit();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("userImage","https://png.pngtree.com/element_our/png/20181206/users-vector-icon-png_260862.jpg");
                                startActivity(intent);
                                finish();
                            } catch (JSONException e) {
                                hideProgressBar();
                                e.printStackTrace();
                                Log.d("JJJ", "Hasil "+e.getMessage());
                            } catch (IOException e) {
                                hideProgressBar();
                                Log.d("JJJ", "Hasil "+e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("JJJ", "Hasil "+response);
                            hideProgressBar();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    public void showProgressBar(){
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB){
            progressDialog = new ProgressDialog(new ContextThemeWrapper(LoginActivity.this, android.R.style.Theme_Holo_Light_Dialog));
        }else{
            progressDialog = new ProgressDialog(LoginActivity.this);
        }
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }
    public void hideProgressBar(){
        progressDialog.dismiss();
    }
}
