package com.sigarda.vendingmachine;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sigarda.vendingmachine.DialogPaymentSuccessFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sigarda.vendingmachine.Adapters.AdapterGridShopProductCard;
import com.sigarda.vendingmachine.Adapters.AdapterMoney;
import com.sigarda.vendingmachine.Utils.Method;
import com.sigarda.vendingmachine.Utils.Tools;
import com.sigarda.vendingmachine.models.Money;
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

public class MainActivity extends AppCompatActivity {
    BaseApiService mApiService;
    RecyclerView recyclerView,recyclerViewMoney;
    View parent_view;
    private boolean doubleBackToExitPressedOnce = false;
    private AdapterGridShopProductCard mAdapter;
    private AdapterMoney.OnItemClickListener onMoneyItemClickListener;
    private AdapterGridShopProductCard.OnItemClickListener onProductItemClickListener;
    private AdapterMoney adapterMoney;
    private MaterialRippleLayout lyt_next;
    private int moneySelected=-1;
    private int productSelected=-1;
    private Product product;
    private AppCompatButton show_dialog;
    private ProgressBar progress_bar,progress_bar_money,progress_bar_product;
    private ScrollView ctx_container;
    private ImageView logout;
    private Method method;
    List<Product> productList = new ArrayList<>();
    List<Money> moneyList = new ArrayList<>();
    List<Money> money_refunds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(R.id.parent_view);
        lyt_next = findViewById(R.id.lyt_next);
        ctx_container = findViewById(R.id.ctx_container);
        progress_bar = findViewById(R.id.progress_bar);
        progress_bar_money = findViewById(R.id.progress_bar_money);
        progress_bar_product = findViewById(R.id.progress_bar_product);
        lyt_next.setAlpha((float)0.4);
        logout = findViewById(R.id.logout);
        method = new Method(MainActivity.this);
        initToolbar();
        initComponent();
        getMonies();
        getProducts();
        Intent intent = getIntent();
        String fullname;
        if(intent.hasExtra("fullname")) {
            fullname = intent.getStringExtra("fullname");
            Toast.makeText(this,"Halo, "+fullname,Toast.LENGTH_SHORT).show();
        }
        logout.setOnClickListener(out->{
            method.editor.putBoolean(method.pref_login, false);
            method.editor.commit();
            method.editor.clear();
            method.editor.apply();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        });
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Tools.setSystemBarColor(this);
        mApiService = ApiClient.UtilsApi.getAPIService();
    }

    public void getProducts(){
        progress_bar_product.setVisibility(View.VISIBLE);
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
                        mAdapter = new AdapterGridShopProductCard(MainActivity.this, productList,onProductItemClickListener);
                        recyclerView.setAdapter(mAdapter);
                        progress_bar_product.setVisibility(View.GONE);
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
    public void getMonies(){
        progress_bar_money.setVisibility(View.VISIBLE);
        mApiService.getMonies().enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        JSONArray monies = jsonRESULTS.getJSONArray("monies");
                        List<Money> newMoney = new Gson().fromJson(monies.toString(), new TypeToken<List<Money>>() {
                        }.getType());
                        for(int i=0;i<newMoney.size();i++){
                            newMoney.get(i).setSelected(true);
                        }
                        moneyList.addAll(newMoney);
                        adapterMoney = new AdapterMoney(MainActivity.this, moneyList,onMoneyItemClickListener);
                        recyclerViewMoney.setAdapter(adapterMoney);
                        progress_bar_money.setVisibility(View.GONE);
//                        adapterMoney.setOnItemClickListener((view, obj, position) -> Toast.makeText(getBaseContext(), "Item " + obj.getName() + " clicked", Toast.LENGTH_SHORT).show());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        Log.d("JJJ", "Hasil "+e.getMessage());
                        Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        progress_bar_money.setVisibility(View.GONE);
                    }
                } else {
                    Log.d("JJJ", "Hasil "+response);

                    progress_bar_money.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("JJJ", "Hasil "+t.getMessage());
            }


        });
    }
    private void initComponent() {
        List<Product> products = new ArrayList<>();
        moneyList = new ArrayList<>();
        money_refunds = new ArrayList<>();
        onMoneyItemClickListener = ((view, obj, position) -> {
            moneySelected = obj.getPrice();
            recyclerView.setAlpha(1);
            for(int i=0;i<moneyList.size();i++){
                if(moneyList.get(i).getPrice()==moneySelected){
                    moneyList.get(i).setSelected(true);
                }else{
                    moneyList.get(i).setSelected(false);
                }
            }
            adapterMoney.notifyDataSetChanged();
        });
        onProductItemClickListener = ((view, obj, position) -> {
            if(moneySelected==-1){
                Toast.makeText(this,"Silahkan pilih uang dulu",Toast.LENGTH_SHORT).show();
            }else {
                productSelected = obj.getId();
                product = obj;
                lyt_next.setAlpha(1);
                for (int i = 0; i < productList.size(); i++) {
                    if (productList.get(i).getId() == productSelected) {
                        productList.get(i).setSelected(true);
                    } else {
                        productList.get(i).setSelected(false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
        lyt_next.setOnClickListener(sub->{
            if(moneySelected==-1){
                Toast.makeText(this,"Pilih uang dulu",Toast.LENGTH_SHORT).show();
            }else if(productSelected==-1){
                Toast.makeText(this,"Pilih product dulu",Toast.LENGTH_SHORT).show();
            }else {
                newTransacction();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setAlpha((float)0.4);
        recyclerViewMoney = (RecyclerView) findViewById(R.id.recyclerViewMoney);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 4), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewMoney.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewMoney.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(this, 4), true));
        recyclerViewMoney.setHasFixedSize(true);
        recyclerViewMoney.setNestedScrollingEnabled(false);

        //set data and list adapter
        mAdapter = new AdapterGridShopProductCard(getBaseContext(), products,onProductItemClickListener);
        recyclerView.setAdapter(mAdapter);
        adapterMoney = new AdapterMoney(getBaseContext(),moneyList,onMoneyItemClickListener);
        recyclerViewMoney.setAdapter(adapterMoney);
        // on item list clicked
        mAdapter.setOnMoreButtonClickListener((view, obj, item) ->
                Toast.makeText(getBaseContext(), obj.getName() + " (" + item.getTitle() + ") clicked", Toast.LENGTH_SHORT).show()
        );

    }

//    private void submitAction() {
//        progress_bar.setVisibility(View.VISIBLE);
////        ctx_container.setAlpha(0f);
//        lyt_next.setVisibility(View.GONE);
//        newTransacction();
////        new Handler().postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                showDialogPaymentSuccess();
////                getMonies();
////                getProducts();
////                progress_bar.setVisibility(View.GONE);
//////                ctx_container.setAlpha(1f);
////                lyt_next.setVisibility(View.VISIBLE);
////            }
////        }, 1000);
//    }
    private void newTransacction(){
        progress_bar.setVisibility(View.VISIBLE);
        lyt_next.setVisibility(View.GONE);
        mApiService.transaction(moneySelected,productSelected).enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        String status = jsonRESULTS.getString("status");
                        String message = jsonRESULTS.getString("message");
                        if(status.equals("success")){
                            JSONArray monies = jsonRESULTS.getJSONArray("data");
                            List<Money> newMoney = new Gson().fromJson(monies.toString(), new TypeToken<List<Money>>() {
                            }.getType());
                            showDialogPaymentSuccess(newMoney);
                            moneyList.clear();
                            moneySelected=-1;
                            productSelected=-1;
                            productList.clear();
                            lyt_next.setAlpha((float)0.4);
                            recyclerView.setAlpha((float)0.4);
                        getMonies();
                        getProducts();
                        }else{
                            Toast.makeText(getBaseContext(),message,Toast.LENGTH_LONG).show();
                        }
                        progress_bar.setVisibility(View.GONE);
                        lyt_next.setVisibility(View.VISIBLE);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        progress_bar.setVisibility(View.GONE);
                        lyt_next.setVisibility(View.VISIBLE);
                        Log.d("JJJ", "Hasil "+e.getMessage());
                    }
                } else {
                    Log.d("JJJ", "Hasil "+response);
                    progress_bar.setVisibility(View.GONE);
                    lyt_next.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("JJJ", "Hasil "+t.getMessage());
                progress_bar.setVisibility(View.GONE);
                lyt_next.setVisibility(View.VISIBLE);
            }


        });
    }

    private void showDialogPaymentSuccess(List<Money> moneyList) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle args = new Bundle();
        String jsonList = new Gson().toJson(moneyList);
        args.putInt("money", moneySelected);
        args.putInt("amount",product.getPrice());
        args.putString("product_name",product.getName());
        args.putString("image_url",product.getImage());
        args.putString("money_refunds",jsonList);
        DialogPaymentSuccessFragment newFragment = new DialogPaymentSuccessFragment();
        newFragment.setArguments(args);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.doubleBackToExitPressedOnce = false;
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
    }
}