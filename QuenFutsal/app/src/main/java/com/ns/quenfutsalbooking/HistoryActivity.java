package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Model.Cart;

public class HistoryActivity extends AppCompatActivity {

    ImageView btnBackPay;
    TextView tvNamePay, tvPhonePay, tvLapanganNamePay, tvPlacePay,
            tvTimePay, tvExTimePay, tvTotalPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        btnBackPay = findViewById(R.id.btnBackPay);
        tvNamePay = findViewById(R.id.tvNamePay);
        tvPhonePay = findViewById(R.id.tvPhonePay);
        tvLapanganNamePay = findViewById(R.id.tvLapanganNamePay);
        tvPlacePay = findViewById(R.id.tvPlacePay);
        tvTimePay = findViewById(R.id.tvTimePay);
        tvExTimePay = findViewById(R.id.tvExTimePay);
        tvTotalPay = findViewById(R.id.tvTotalPay);

        btnBackPay.setOnClickListener(v -> super.onBackPressed());

        initView();
    }

    private void initView() {
        DocumentReference cartBooking = FirebaseFirestore.getInstance()
                .collection("carts")
                .document(Common.currentUser);
        cartBooking.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                Cart carts = documentSnapshot.toObject(Cart.class);
                tvNamePay.setText(carts.getCostumerName());
                tvPhonePay.setText(carts.getCostumerPhone());
                tvLapanganNamePay.setText(carts.getLapanganName());
                tvPlacePay.setText(carts.getPlaceName());
                tvTimePay.setText(carts.getTime());
                tvExTimePay.setText(carts.getExtraTime());
                tvTotalPay.setText(carts.getPrice());
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}