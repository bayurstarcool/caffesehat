package com.sigarda.vendingmachine.Adapters;

import android.content.Context;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
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

public class AdapterMoney extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Money> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    private OnMoreButtonClickListener onMoreButtonClickListener;

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    public AdapterMoney(Context context, List<Money> items,OnItemClickListener mItemClickListener) {
        this.items = items;
        this.mOnItemClickListener = mItemClickListener;
        ctx = context;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            final Money p = items.get(position);
            view.title.setText(Tools.currency("Rp",p.getPrice()));
            view.stock.setText(Html.fromHtml("Stock kembalian: <b>"+p.getStock()+"</b>"));
            if(!p.isSelected()){
                view.lyt_parent.setAlpha((float)0.4);
            }else{
                view.lyt_parent.setAlpha(1);
            }
//            Tools.displayImageOriginal(ctx, view.image, p.getImage());
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(ctx,"item "+items.get(position).getName(),Toast.LENGTH_SHORT).show();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

        }
    }

    private void onMoreButtonClick(final View view, final Money m) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, m, item);
                return true;
            }
        });
//        popupMenu.inflate(R.menu.menu_product_more);
        popupMenu.show();
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