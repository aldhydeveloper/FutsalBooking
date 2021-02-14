package com.ns.quenfutsalbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Model.Cart;

public class CartsActivity extends AppCompatActivity {

    ImageView btnBackCart;
    TextView tvNameCart, tvPhoneCart, tvLapanganNameCart, tvPlaceCart,
             tvTimeCart, tvExTimeCart, tvTotalCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carts);

        btnBackCart = findViewById(R.id.btnBackCart);
        tvNameCart = findViewById(R.id.tvNameCart);
        tvPhoneCart = findViewById(R.id.tvPhoneCart);
        tvLapanganNameCart = findViewById(R.id.tvLapanganNameCart);
        tvPlaceCart = findViewById(R.id.tvPlaceCart);
        tvTimeCart = findViewById(R.id.tvTimeCart);
        tvExTimeCart = findViewById(R.id.tvExTimeCart);
        tvTotalCart = findViewById(R.id.tvTotalCart);

        btnBackCart.setOnClickListener(v -> super.onBackPressed());

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
                tvNameCart.setText(carts.getCostumerName());
                tvPhoneCart.setText(carts.getCostumerPhone());
                tvLapanganNameCart.setText(carts.getLapanganName());
                tvPlaceCart.setText(carts.getPlaceName());
                tvTimeCart.setText(carts.getTime());
                tvExTimeCart.setText(carts.getExtraTime());
                tvTotalCart.setText(carts.getPrice());
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}