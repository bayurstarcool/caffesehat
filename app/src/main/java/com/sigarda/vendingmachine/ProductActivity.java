package com.sigarda.vendingmachine;

import android.os.Build;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sigarda.vendingmachine.Adapters.AdapterGridShopProductCard;
import com.sigarda.vendingmachine.Adapters.ProductAdapter;
import com.sigarda.vendingmachine.Utils.Tools;
import com.sigarda.vendingmachine.models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {

    private View parent_view;
    BaseApiService mApiService;
    private RecyclerView recyclerView;
    private ProductAdapter mAdapter;
    List<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.products_list);
        parent_view = findViewById(android.R.id.content);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Tools.setSystemBarColor(this);
        mApiService = ApiClient.UtilsApi.getAPIService();
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        List<Product> items = new ArrayList<>();

        //set data and list adapter
        mAdapter = new ProductAdapter(this, items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener((view, obj, position) -> Log.d("OKOK",obj.getName()));
        getProducts();
    }
    public void getProducts(){
        mApiService.getProducts().enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray products = jsonRESULTS.getJSONArray("products");

                        List<Product> newProduct = new Gson().fromJson(products.toString(), new TypeToken<List<Product>>() {
                        }.getType());
                        for(int i=0;i<newProduct.size();i++){
                            newProduct.get(i).setSelected(true);
                        }
                        productList.addAll(newProduct);
                        mAdapter = new ProductAdapter(ProductActivity.this, productList);
//                        mAdapter.setOnItemClickListener((view, obj, position) -> Log.d("OKOK",obj.getName()));
                        mAdapter.setOnItemRemove((view, obj, position) ->{
                           getProducts();
                        });
                        recyclerView.setAdapter(mAdapter);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        Log.d("JJJ", "Hasil "+e.getMessage());
                    }
                } else {
                    Log.d("JJJ", "Hasil "+response);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("JJJ", "Hasil "+t.getMessage());
                Toast.makeText(getBaseContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }


        });
    }
}