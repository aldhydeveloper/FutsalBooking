package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailTempatActivity extends AppCompatActivity {

    ImageView btnBackDtPlace, ivDtPlace;
    TextView tvTitleDtPlace, tvAddressDtPlace;
    ImageButton btnNav;

    String idPlace, image, tile, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tempat);

        btnBackDtPlace = findViewById(R.id.btnBackDtPlace);
        ivDtPlace = findViewById(R.id.ivDtPlace);
        tvTitleDtPlace = findViewById(R.id.tvTitleDtPlace);
        tvAddressDtPlace = findViewById(R.id.tvAddressDtPlace);
        btnNav = findViewById(R.id.btnNav);

        if (getIntent() != null) {
            idPlace = getIntent().getStringExtra("idPlace");
            image = getIntent().getStringExtra("image");
            tile = getIntent().getStringExtra("tile");
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
        }
        initView();
    }

    private void initView() {
        tvTitleDtPlace.setText(tile);
        Picasso.get().load(image).into(ivDtPlace);

        btnNav.setOnClickListener(v ->
                startActivity(new Intent(this, MapsActivity.class)
                        .putExtra("lat", latitude)
                        .putExtra("lon", longitude)
                        .putExtra("title", tile)));

        btnBackDtPlace.setOnClickListener(v ->
                super.onBackPressed());
    }

    @Override
    public void onBackPressed() {
    }
}