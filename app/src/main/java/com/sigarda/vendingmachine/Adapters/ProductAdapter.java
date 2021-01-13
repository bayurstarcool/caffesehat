package com.sigarda.vendingmachine.Adapters;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.sigarda.vendingmachine.EditProduct;
import com.sigarda.vendingmachine.R;
import com.sigarda.vendingmachine.Utils.Tools;
import com.sigarda.vendingmachine.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnItemRemove onItemRemove;

    public interface OnItemClickListener {
        void onItemClick(View view, Product obj, int position);
    }
    public interface  OnItemRemove{
        void onItemRemove(View view, Product obj, int position);
    }
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }
    public void setOnItemRemove(final OnItemRemove onItemRemove) {
        this.onItemRemove = onItemRemove;
    }

    public ProductAdapter(Context context, List<Product> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image,edit,delete;
        public TextView name,description,stock;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            name = (TextView) v.findViewById(R.id.name);
            stock = (TextView) v.findViewById(R.id.stock);
            description = v.findViewById(R.id.description);
            edit = v.findViewById(R.id.edit);
            delete = v.findViewById(R.id.delete);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Product p = items.get(position);
            view.name.setText(p.getName());
            view.description.setText(Tools.currency("Rp",p.getPrice()));
            view.stock.setText("Stock :"+p.getStock());
            view.delete.setOnClickListener(del->{
                if (onItemRemove != null) {
                    onItemRemove.onItemRemove(del, items.get(position), position);
                }
            });
            view.edit.setOnClickListener(edit->{
                Intent i = new Intent(ctx,EditProduct.class);
                i.putExtra("product_id",p.getId());
                i.putExtra("name",p.getName());
                i.putExtra("stock",p.getStock());
                i.putExtra("price",p.getPrice());
                ctx.startActivity(i);
            });
            Tools.displayImageOriginal(ctx, view.image, p.getImage());
            view.lyt_parent.setOnClickListener(view1 -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view1, items.get(position), position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}