package com.example.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopping.R;
import com.example.shopping.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    List<Product> array;

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

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = array.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtprice.setText("đ" + decimalFormat.format(Double.parseDouble(product.getPrice())));
        holder.txtname.setText(product.getName());
        Glide.with(context).load(product.getImage()).into(holder.imgimage);

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtname, txtprice;
        ImageView imgimage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtprice = itemView.findViewById(R.id.itemP_price);
            txtname = itemView.findViewById(R.id.itemP_name);
            imgimage = itemView.findViewById(R.id.itemP_image);
        }
    }
}
