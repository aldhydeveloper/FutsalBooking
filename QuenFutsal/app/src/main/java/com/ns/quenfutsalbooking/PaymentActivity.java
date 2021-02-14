package com.ns.quenfutsalbooking;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Interface.IBookingInfoLoadListener;
import com.ns.quenfutsalbooking.Model.BookingInformation;
import com.ns.quenfutsalbooking.Model.Cart;

import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class PaymentActivity extends AppCompatActivity implements IBookingInfoLoadListener {

    ImageView btnBack;
    TextView tvName, tvPhone, tvLapanganName, tvPlace,
            tvPlaceAddress, tvTime, tvPrice, tvExTime, tvTotal;
    Button btnPayCOD, btnPayOnline;
    LinearLayout llEmpty;

    AlertDialog dialog;

    IBookingInfoLoadListener iBookingInfoLoadListener;

    DocumentReference cartBooking;

    int totalExTime = 0, price = 0, totalPrice = 0;
    String firstTotal;
    String email, idTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnBack = findViewById(R.id.btnBack);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvPlace = findViewById(R.id.tvPlace);
        tvLapanganName = findViewById(R.id.tvLapanganName);
        tvPlaceAddress = findViewById(R.id.tvPlaceAddress);
        tvTime = findViewById(R.id.tvTime);
        tvPrice = findViewById(R.id.tvPriceTotal);
        btnPayCOD = findViewById(R.id.btnPayCOD);
        btnPayOnline = findViewById(R.id.btnPayOnline);
        tvExTime = findViewById(R.id.tvExTime);
        tvTotal = findViewById(R.id.tvTotal);
        llEmpty = findViewById(R.id.llEmpty);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        dialog.show();

        cartBooking = FirebaseFirestore.getInstance()
                .collection("carts")
                .document(Common.currentUser);

        iBookingInfoLoadListener = this;


        btnBack.setOnClickListener(v ->
                super.onBackPressed());

        btnPayOnline.setOnClickListener(v ->
                paymentOnline());

        btnPayCOD.setOnClickListener(v ->
                paymentCOD());


        initView();
        initMidtrans();


    }

    private void initMidtrans() {
        SdkUIFlowBuilder.init()
                .setClientKey("Mid-client-Vu31erBpWiv7D463")
                .setContext(this)
                .setTransactionFinishedCallback(transactionResult -> {
                    if (transactionResult.getResponse() != null) {
                        switch (transactionResult.getStatus()) {
                            case TransactionResult.STATUS_SUCCESS:
                                Toast.makeText(PaymentActivity.this, "Transaction Finished ID :" + transactionResult.getResponse().getTransactionId(), Toast.LENGTH_SHORT).show();
                                String addIdCart = Common.currentUser;
                                String name = tvName.getText().toString();
                                String phone = tvPhone.getText().toString();
                                String place = tvPlace.getText().toString();
                                String lapangan = tvLapanganName.getText().toString();
                                String price = tvTotal.getText().toString();
                                String time = tvTime.getText().toString();
                                String exTime = tvExTime.getText().toString();
                                Cart cars = new Cart(addIdCart, name, phone, place, lapangan, price, time, exTime, "Lunas");
                                cartBooking.set(cars).addOnSuccessListener(documentReference -> {
                                    startActivity(new Intent(PaymentActivity.this, MainActivity.class));
                                    Toast.makeText(PaymentActivity.this, "Terimakasih, Silahkan Cek Cart Anda", Toast.LENGTH_SHORT).show();
                                }).addOnFailureListener(e ->
                                        Toast.makeText(PaymentActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                break;
                            case TransactionResult.STATUS_PENDING:
                                Toast.makeText(PaymentActivity.this, "Transaction Pending ID :" + transactionResult.getResponse().getTransactionId(), Toast.LENGTH_SHORT).show();
                                break;
                            case TransactionResult.STATUS_FAILED:
                                Toast.makeText(PaymentActivity.this, "Transaction Failed ID :" + transactionResult.getResponse().getTransactionId(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        transactionResult.getResponse().getValidationMessages();
                    } else if (transactionResult.isTransactionCanceled()) {
                        Toast.makeText(PaymentActivity.this, "Transaction Cancelled", Toast.LENGTH_SHORT).show();
                    } else {
                        if (transactionResult.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                            Toast.makeText(PaymentActivity.this, "Transaction Invalid", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Transaction Finished", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setMerchantBaseUrl("https://futsallbooking.herokuapp.com/index.php/")
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .buildSDK();
    }

    private void initView() {
        if (Common.currentBooking != null) {
            loadUserInformationBooking();
        } else {

        }
    }

    private void loadUserInformationBooking() {

        if (Common.currentBooking != null) {
            llEmpty.setVisibility(View.GONE);
            CollectionReference userBooking = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(Common.currentUser)
                    .collection("Booking");

            //get current date
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 0);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.add(Calendar.MINUTE, 0);

            Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

            userBooking
                    .whereGreaterThanOrEqualTo("timestamp", toDayTimeStamp)
                    .whereEqualTo("done", false)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation, queryDocumentSnapshot.getId());
                                    break;
                                }
                            } else
                                iBookingInfoLoadListener.onBookingInfoLoadEmpty();
                        }

                    }).addOnFailureListener(e -> {
                iBookingInfoLoadListener.onBookingInfoLoadFailed(e.getMessage());
            });

            dialog.dismiss();
        } else {
            llEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void paymentCOD() {
        String addIdCart = Common.currentUser;
        String name = tvName.getText().toString();
        String phone = tvPhone.getText().toString();
        String place = tvPlace.getText().toString();
        String lapangan = tvLapanganName.getText().toString();
        String price = tvTotal.getText().toString();
        String time = tvTime.getText().toString();
        String exTime = tvExTime.getText().toString();
        Cart cars = new Cart(addIdCart, name, phone, place, lapangan, price, time, exTime, "Pending");
        cartBooking.set(cars).addOnSuccessListener(documentReference -> {
            startActivity(new Intent(PaymentActivity.this, MainActivity.class));
            Toast.makeText(PaymentActivity.this, "Terimakasih, Silahkan Cek Cart Anda", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->
                Toast.makeText(PaymentActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void paymentOnline() {
        String total = tvTotal.getText().toString();
        int totalPay = Integer.parseInt(total);
        String lapangan = tvLapanganName.getText().toString();
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest(idTransaction, totalPay, 1, lapangan));
        MidtransSDK.getInstance().startPaymentUiFlow(this);
    }

    public TransactionRequest transactionRequest(String id, int total, int qty, String name) {

        String fullName = tvName.getText().toString();
        String noHp = tvPhone.getText().toString();
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName(fullName);
        cd.setPhone(noHp);
        cd.setEmail("admin@gmail.com");

        TransactionRequest request = new TransactionRequest(System.currentTimeMillis() + "", total);
        request.setCustomerDetails(cd);

        ItemDetails itemDetails = new ItemDetails(id, total, qty, name);
        ArrayList<ItemDetails> details = new ArrayList<>();
        details.add(itemDetails);

        request.setItemDetails(details);

        return request;
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onBookingInfoLoadEmpty() {

    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation, String bookingId) {
        Common.currentBooking = bookingInformation;
        Common.currentBookingId = bookingId;

        tvName.setText(bookingInformation.getCostumerName());
        tvPhone.setText(bookingInformation.getCostumerPhone());
        tvLapanganName.setText(bookingInformation.getLapanganName());
        tvPlace.setText(bookingInformation.getTempatName());
        tvPlaceAddress.setText(bookingInformation.getTempatAddress());
        tvTime.setText(bookingInformation.getTime());
        tvPrice.setText(bookingInformation.getHarga());
        tvExTime.setText(bookingInformation.getExtraTime());
        firstTotal = tvPrice.getText().toString();
        idTransaction = bookingId;
        email = bookingInformation.getCostumerEmail();
        if (tvExTime.getText().equals("0")) {
            tvTotal.setText(firstTotal);
        } else {
            totalExTime = Integer.parseInt(bookingInformation.getExtraTime()) + 1;
            price = Integer.parseInt(bookingInformation.getHarga());
            totalPrice = totalExTime * price;
            tvTotal.setText(String.format("%d", totalPrice));
        }


    }

    @Override
    public void onBookingInfoLoadFailed(String message) {

    }
}