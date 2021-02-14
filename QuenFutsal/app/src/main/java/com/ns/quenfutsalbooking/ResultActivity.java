package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    ImageView btnBackResult, ivResultPlace;
    TextView tvTitleResultPlace, tvAddressResultPlace,tvDaerah,tvJenis
            ,tvLantai,tvBola,tvHarga;
    ImageButton btnNavResult;

    String ivTempat, title, address, lat, lon, daerah, inDoor, outDor, lantai, bola, harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        btnBackResult = findViewById(R.id.btnBackResult);
        ivResultPlace = findViewById(R.id.ivResultPlace);
        tvTitleResultPlace = findViewById(R.id.tvTitleResultPlace);
        tvAddressResultPlace = findViewById(R.id.tvAddressResultPlace);
        tvDaerah = findViewById(R.id.tvDaerah);
        tvJenis = findViewById(R.id.tvJenis);
        tvLantai = findViewById(R.id.tvLantai);
        tvBola = findViewById(R.id.tvBola);
        tvHarga = findViewById(R.id.tvHarga);
        btnNavResult = findViewById(R.id.btnNavResult);

        if (getIntent() != null){
            daerah = getIntent().getStringExtra("daerah");
            inDoor = getIntent().getStringExtra("door1");
            outDor = getIntent().getStringExtra("door2");
            lantai = getIntent().getStringExtra("lantai");
            bola = getIntent().getStringExtra("bola");
            harga = getIntent().getStringExtra("harga");
        }

        initView();
    }

    private void initView() {
        tvDaerah.setText(daerah);
        if (inDoor.equals("0") && !outDor.equals("0")){
            tvJenis.setText(outDor);
        }else if (!inDoor.equals("0") && outDor.equals("0")){
            tvJenis.setText(inDoor);
        }else if (!inDoor.equals("0") && !outDor.equals("0")){
            tvJenis.setText(String.format("%s,%s", inDoor, outDor));
        }

        tvHarga.setText(harga);
        tvLantai.setText(lantai);
        tvBola.setText(bola);
    }
}