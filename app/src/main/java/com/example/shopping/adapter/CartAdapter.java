package com.example.shopping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopping.Interface.ImageClickListener;
import com.example.shopping.R;
import com.example.shopping.model.Cart;
import com.example.shopping.model.EventBus.CalculateTotalEvent;
import com.example.shopping.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.item_cart_name.setText(cart.getNameProduct());
        holder.item_cart_amount.setText(cart.getAmount() + " ");
        Glide.with(context).load(cart.getImgProduct()).into(holder.item_cart_image);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_price.setText("đ" + decimalFormat.format(cart.getPriceProduct()));
        long price = cart.getAmount() * cart.getPriceProduct();
        holder.item_cart_price2.setText(decimalFormat.format(price));
        holder.setListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                Log.d("TAG", "onImageClick: "+pos + "..."+ value);
                if(value == 1){
                    if(cartList.get(pos).getAmount() > 1){
                        int newamount = cartList.get(pos).getAmount() - 1;
                        cartList.get(pos).setAmount(newamount);

                        holder.item_cart_amount.setText(cartList.get(pos).getAmount() + " ");
                        long price = cartList.get(pos).getAmount() * cartList.get(pos).getPriceProduct();
                        holder.item_cart_price2.setText(decimalFormat.format(price));
                        EventBus.getDefault().postSticky(new CalculateTotalEvent());
                    } else if (cartList.get(pos).getAmount() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Notification");
                        builder.setMessage("Do you want delete product ?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.cartList.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new CalculateTotalEvent());
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();

                    }
                } else if (value == 2) {
                    if(cartList.get(pos).getAmount() < 11){
                        int newamount = cartList.get(pos).getAmount() + 1;
                        cartList.get(pos).setAmount(newamount);
                    }
                    holder.item_cart_amount.setText(cartList.get(pos).getAmount() + " ");
                    long price = cartList.get(pos).getAmount() * cartList.get(pos).getPriceProduct();
                    holder.item_cart_price2.setText(decimalFormat.format(price));
                    EventBus.getDefault().postSticky(new CalculateTotalEvent());

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_image, imgremove, imgadd;
        TextView item_cart_name, item_cart_price, item_cart_amount, item_cart_price2;
        ImageClickListener listener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_cart_image =itemView.findViewById(R.id.item_cart_image);
            item_cart_name = itemView.findViewById(R.id.item_cart_name);
            item_cart_price = itemView.findViewById(R.id.item_cart_price);
            item_cart_amount = itemView.findViewById(R.id.item_cart_amount);
            item_cart_price2 = itemView.findViewById(R.id.item_cart_price2);
            imgremove = itemView.findViewById(R.id.item_cart_remove);
            imgadd = itemView.findViewById(R.id.item_cart_add);

            //event click
            imgadd.setOnClickListener(this);
            imgremove.setOnClickListener(this);
        }

        public void setListener(ImageClickListener listener) {
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {
            if(v == imgremove){
                //1 tru
                listener.onImageClick(v, getAdapterPosition(), 1);
            } else if (v == imgadd) {
                //2 cong
                listener.onImageClick(v, getAdapterPosition(), 2);
                
            }
        }
    }
}
