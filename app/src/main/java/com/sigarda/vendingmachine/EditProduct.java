package com.sigarda.vendingmachine;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class EditProduct extends AppCompatActivity {
    private int price,stock,id;
    private String name;
    private EditText name_et,price_et,stock_et;
    private ProgressDialog progressDialog;
    private Button btn_save;
    BaseApiService mApiService;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product);
        name_et = findViewById(R.id.name_et);
        price_et = findViewById(R.id.price_et);
        stock_et = findViewById(R.id.stock_et);
        btn_save = findViewById(R.id.save);
        mApiService = ApiClient.UtilsApi.getAPIService();

        Intent intent = getIntent();
        if(intent.hasExtra("product_id")) {
            id = intent.getIntExtra("product_id",0);
            name = intent.getStringExtra("name");
            stock = intent.getIntExtra("stock",0);
            price = intent.getIntExtra("price",0);
            name_et.setText(name);
            price_et.setText(""+price);
            stock_et.setText(""+stock);
            Toast.makeText(this,"Halo, "+id,Toast.LENGTH_SHORT).show();
        }
        btn_save.setOnClickListener(ok->{
            doSave();
        });
    }

    public void doSave(){
        showProgressBar();
        mApiService.updateProduct(id,Integer.parseInt(price_et.getText().toString()),name_et.getText().toString(),Integer.parseInt(stock_et.getText().toString()))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());                             Intent intent = new Intent(EditProduct.this, ProductActivity.class);
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
            progressDialog = new ProgressDialog(new ContextThemeWrapper(EditProduct.this, android.R.style.Theme_Holo_Light_Dialog));
        }else{
            progressDialog = new ProgressDialog(EditProduct.this);
        }
        progressDialog.show();
        progressDialog.setMessage("Menyimpan...");
        progressDialog.setCancelable(false);
    }
    public void hideProgressBar(){
        progressDialog.dismiss();
    }
}
