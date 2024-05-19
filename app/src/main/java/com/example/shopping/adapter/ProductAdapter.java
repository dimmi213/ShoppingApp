package com.example.shopping.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopping.Interface.ItemClickListener;
import com.example.shopping.R;
import com.example.shopping.activity.DetailActivity;
import com.example.shopping.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    List<Product> array;
    public boolean isRecyclerViewItemClicked = false;

    public ProductAdapter(Context context, List<Product> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_new,parent,false);
        return new MyViewHolder(item);
    }

    public void setRecyclerViewItemClicked(boolean clicked) {
        isRecyclerViewItemClicked = clicked;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;
        Product product = array.get(position);
        double price = Double.parseDouble(product.getPrice());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtprice.setText("đ" + decimalFormat.format(price));
        holder.txtname.setText(product.getName());
        Glide.with(context).load(product.getImage()).into(holder.imgimage);
        myViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    setRecyclerViewItemClicked(true);
                    isRecyclerViewItemClicked = true;

                    //click
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("detail", product);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

    }

    public boolean isRecyclerViewItemClicked() {
        return isRecyclerViewItemClicked;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtname, txtprice;
        ImageView imgimage;
        private ItemClickListener itemClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtprice = itemView.findViewById(R.id.itemP_price);
            txtname = itemView.findViewById(R.id.itemP_name);
            imgimage = itemView.findViewById(R.id.itemP_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }
    }
}
