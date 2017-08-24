package com.think.shopster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URI;

/**
 * Created by anand on 18/04/17.
 */

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView productImage;
    private TextView productName;
    private TextView productPrice;
    private Button amazon;
    private Button flipkart;
    private  Button snapdeal;
    private ProductData p;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_view);

        productImage = (ImageView) findViewById(R.id.product_image);
        productName = (TextView) findViewById(R.id.product_name);
        productPrice = (TextView) findViewById(R.id.product_price);
        amazon = (Button) findViewById(R.id.amazon);
        flipkart = (Button) findViewById(R.id.flipkart);
        snapdeal = (Button) findViewById(R.id.snapdeal);

        amazon.setOnClickListener(this);
        flipkart.setOnClickListener(this);
        snapdeal.setOnClickListener(this);

        feedDataToFrontEnd();
    }

    private void feedDataToFrontEnd() {
         p= (ProductData) getIntent().getSerializableExtra("ProductSelected");
        Picasso.with(getApplicationContext()).load(p.image_url).into(productImage);
        productName.setText(p.product_name);
        productPrice.setText(""+p.min_price);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.amazon:
                goToLink(p.a_url);
                break;
            case R.id.flipkart:
                goToLink(p.f_url);
                break;
            case R.id.snapdeal:
                goToLink(p.s_url);
                break;
        }
    }

    private void goToLink(String URL){
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(URL));
        startActivity(intent);
    }
}
