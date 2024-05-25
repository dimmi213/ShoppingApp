package com.example.shopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopping.R;
import com.example.shopping.model.OrderDatail;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {
    List<OrderDatail> orderDatailList;
    @NonNull
    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_details, parent, false);
        return new OrderDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.MyViewHolder holder, int position) {
        OrderDatail item = orderDatailList.get(position);
        holder.productName.setText(item.getIdproduct());
    }

    @Override
    public int getItemCount() {
        return orderDatailList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName, unitPrice, amount;
        ImageView item_imgProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            unitPrice = itemView.findViewById(R.id.unitPrice);
            amount = itemView.findViewById(R.id.amount);
            item_imgProduct = itemView.findViewById(R.id.item_imgProduct);
        }
    }
}
