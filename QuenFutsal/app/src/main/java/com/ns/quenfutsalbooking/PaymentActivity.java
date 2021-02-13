package com.ns.quenfutsalbooking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.BillInfoModel;
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Interface.IBookingInfoLoadListener;
import com.ns.quenfutsalbooking.Model.BookingInformation;

import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

public class PaymentActivity extends AppCompatActivity implements IBookingInfoLoadListener {

    ImageView btnBack,btnPlus,btnMinus;
    TextView tvName,tvPhone,tvLapanganName,tvPlace,
            tvPlaceAddress,tvTime,tvPrice,tvExTime,tvTotal;
    EditText tvAddress,
            tvCity,tvPostalCode;
    Button btnPayCOD, btnPayOnline;
    LinearLayout llEmpty;

    AlertDialog dialog;

    IBookingInfoLoadListener iBookingInfoLoadListener;

    int totalExTime = 0, price = 0, totalPrice = 0;
    String firstTotal;
    String finalTotalExTime;
    String email,idTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        btnBack = findViewById(R.id.btnBack);
        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvPlace = findViewById(R.id.tvPlace);
        tvCity = findViewById(R.id.tvCity);
        tvPostalCode = findViewById(R.id.tvPostalCode);
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

        iBookingInfoLoadListener = this;


        btnBack.setOnClickListener(v ->
                super.onBackPressed());

        btnPayOnline.setOnClickListener(v ->{
                    String address = tvAddress.getText().toString();
                    String city = tvCity.getText().toString();
                    String pos = tvPostalCode.getText().toString();
                    if (address.isEmpty() && city.isEmpty() && pos.isEmpty()){
                        Toast.makeText(this, "Lengkapi alamat anda!", Toast.LENGTH_SHORT).show();
                    }else{
                        paymentOnline();
                    }
                });

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
                .setMerchantBaseUrl("https://kopikeliling.herokuapp.com/index.php/")
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255"))
                .buildSDK();
    }

    private void initView() {
        if (Common.currentBooking != null){
            loadUserInformationBooking();
        }else{

        }
    }

    private void loadUserInformationBooking() {

        if (Common.currentBooking != null){
            llEmpty.setVisibility(View.GONE);
            CollectionReference userBooking = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(Common.currentUser)
                    .collection("Booking");

            //get current date
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,0);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.add(Calendar.MINUTE,0);

            Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());

            userBooking
                    .whereGreaterThanOrEqualTo("timestamp",toDayTimeStamp)
                    .whereEqualTo("done",false)
                    .get()
                    .addOnCompleteListener(task -> {

                        if (task.isSuccessful())
                        {
                            if (!task.getResult().isEmpty())
                            {
                                for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookingInfoLoadSuccess(bookingInformation,queryDocumentSnapshot.getId());
                                    break;
                                }
                            }else
                                iBookingInfoLoadListener.onBookingInfoLoadEmpty();
                        }

                    }).addOnFailureListener(e -> {
                iBookingInfoLoadListener.onBookingInfoLoadFailed(e.getMessage());
            });

            dialog.dismiss();
        }else{
            llEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void paymentCOD() {
    }

    private void paymentOnline() {
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest());
        MidtransSDK.getInstance().startPaymentUiFlow(this, PaymentMethod.GO_PAY);
    }

    public TransactionRequest transactionRequest() {

        String fullName = tvName.getText().toString();
        String noHp = tvPhone.getText().toString();
        String address = tvAddress.getText().toString();
        String city = tvCity.getText().toString();
        String postalCode = tvPostalCode.getText().toString();
        String total = tvTotal.getText().toString();
        String lapangan = tvLapanganName.getText().toString();

        int totalPay = Integer.parseInt(total);

        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName(fullName);
        cd.setLastName(" ");
        cd.setPhone(noHp);
        cd.setEmail("email");

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress(address);
        shippingAddress.setCity(city);
        shippingAddress.setPostalCode(postalCode);
        cd.setShippingAddress(shippingAddress);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress(address);
        billingAddress.setCity(city);
        billingAddress.setPostalCode(postalCode);
        cd.setBillingAddress(billingAddress);

        TransactionRequest request = new TransactionRequest(System.currentTimeMillis()+"",totalPay);
        request.setCustomerDetails(cd);

        ItemDetails itemDetails = new ItemDetails(idTransaction,totalPay,1, lapangan);
        ArrayList<ItemDetails> details = new ArrayList<>();
        details.add(itemDetails);
        BillInfoModel billInfoModel = new BillInfoModel(idTransaction,lapangan);

        request.setBillInfoModel(billInfoModel);
        request.setItemDetails(details);

        return request;
    }

    @Override
    public void onBackPressed() {    }

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
        if (tvExTime.getText().equals("0")){
            tvTotal.setText(firstTotal);
        }else {
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