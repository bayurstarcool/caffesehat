package com.sigarda.vendingmachine;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.sigarda.vendingmachine.Adapters.AdapterMoneyRefund;
import com.sigarda.vendingmachine.Utils.Tools;
import com.sigarda.vendingmachine.models.Money;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DialogPaymentSuccessFragment extends DialogFragment {
    BaseApiService mApiService;
    private View root_view;
    private TextView credit,txt_money,txt_product_name,txt_product_price,refund;
    LinearLayout rincian;
    RecyclerView recyclerView;
    AdapterMoneyRefund mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView imageView;
    List<Money> moneyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mApiService = ApiClient.UtilsApi.getAPIService();
        root_view = inflater.inflate(R.layout.dialog_payment_success, container, false);
        credit = root_view.findViewById(R.id.credit);
        txt_money = root_view.findViewById(R.id.money);
        txt_product_name = root_view.findViewById(R.id.product_name);
        txt_product_price = root_view.findViewById(R.id.product_price);
        imageView = root_view.findViewById(R.id.imageView);
        rincian = root_view.findViewById(R.id.rincian);
        recyclerView = root_view.findViewById(R.id.recyclerViewRefund);
        refund = root_view.findViewById(R.id.refund);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        moneyList = new ArrayList<>();
        Bundle mArgs = getArguments();
        int amount = mArgs.getInt("amount");
        int money = mArgs.getInt("money");
        String productName = mArgs.getString("product_name");
        String monies = mArgs.getString("money_refunds");
        String imgurl = mArgs.getString("image_url");
        txt_money.setText(Tools.currency("Rp",money));
        txt_product_price.setText(Tools.currency("Rp",amount));
        txt_product_name.setText(productName);
        refund.setText(Tools.currency("Rp",money-amount));
        List<Money> newMoney = new Gson().fromJson(monies, new TypeToken<List<Money>>() {
        }.getType());
        moneyList.addAll(newMoney);
        Tools.displayImageOriginal(getContext(),imageView,imgurl);
        mAdapter = new AdapterMoneyRefund(moneyList);
        recyclerView.setAdapter(mAdapter);
        if(money-amount==0){
            rincian.setVisibility(View.GONE);
        }else{
            rincian.setVisibility(View.VISIBLE);
        }
        credit.setText(Html.fromHtml("Aplikasi ini hanya untuk tugas kuliah.<br>By <b> Wahyu Prasetyo & TIM</b>"));
        ((FloatingActionButton) root_view.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return root_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}