package com.example.sidkathuria14.myapplication.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.sidkathuria14.myapplication.R;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

public class MyQR extends AppCompatActivity {
Retrofit retrofit;ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qr);

        imgView = (ImageView)findViewById(R.id.imageqr);

        Picasso.with(this).load("https://chart.googleapis.com//chart?cht=qr&chl=AvONmQvySBZsE4nmiBNFn2CR5Uz1&chs=160x160&chld=L|0").into(imgView);




    }
}
