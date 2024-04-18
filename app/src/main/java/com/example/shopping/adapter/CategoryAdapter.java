package com.example.shopping.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shopping.R;
import com.example.shopping.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<Category> array;
    Context context;

    public CategoryAdapter(Context context, List<Category> array) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder{
        TextView textNameProduct;
        ImageView imgImgae;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_product, parent,false);
            viewHolder.textNameProduct = convertView.findViewById(R.id.item_name_product);
            viewHolder.imgImgae = convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.textNameProduct.setText(array.get(position).getNameProduct());
            Glide.with(context).load(array.get(position).getImage()).into(viewHolder.imgImgae);

        }

        return convertView;
    }
}
