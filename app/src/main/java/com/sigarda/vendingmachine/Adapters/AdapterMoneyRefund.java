package com.sigarda.vendingmachine.Adapters;

import android.content.Context;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.sigarda.vendingmachine.R;
import com.sigarda.vendingmachine.Utils.Tools;
import com.sigarda.vendingmachine.models.Money;
import com.sigarda.vendingmachine.models.Product;

import java.util.ArrayList;
import java.util.List;

public class AdapterMoneyRefund extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Money> items = new ArrayList<>();



    public AdapterMoneyRefund(List<Money> items ) {
        this.items = items;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title,stock;

        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);
            stock = (TextView) v.findViewById(R.id.stock);
            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refund, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Money p = items.get(position);
            if(p==null){
                view.title.setText("-");
            }
            view.title.setText("• "+Tools.currency("Rp",p.getPrice()));
//            Tools.displayImageOriginal(ctx, view.image, p.getImage());

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, Money obj, int pos);
    }

    public interface OnMoreButtonClickListener {
        void onItemClick(View view, Money obj, MenuItem item);
    }

}