package com.think.shopster;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 17/04/17.
 */

public class ProductAdapter extends ArrayAdapter<ProductData> implements Serializable{

    public ProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ProductData> products) {
        super(context, resource, products);
    }



    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.individual_product,parent,false);
        }

        ProductData productData= getItem(position);



        ImageView productImage=(ImageView) convertView.findViewById(R.id.imageView);
        productImage.setAdjustViewBounds(true);

        TextView productPrice = (TextView) convertView.findViewById(R.id.product_price_ind);
        TextView productName = (TextView) convertView.findViewById(R.id.product_name_ind);

        Picasso.with(getContext()).load(productData.image_url).into(productImage);
        productName.setText(productData.product_name);
        productPrice.setText(""+productData.min_price);

        return convertView;


    }
}
