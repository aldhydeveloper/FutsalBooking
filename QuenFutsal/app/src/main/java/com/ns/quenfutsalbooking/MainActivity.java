package com.ns.quenfutsalbooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ns.quenfutsalbooking.Adapter.HomeSlideAdapter;
import com.ns.quenfutsalbooking.Adapter.LayoutLapanganAdapter;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Interface.IBannerLoadListener;
import com.ns.quenfutsalbooking.Interface.IBookingInfoLoadListener;
import com.ns.quenfutsalbooking.Interface.IBookingInformationChangeListener;
import com.ns.quenfutsalbooking.Interface.ILapanganInterface;
import com.ns.quenfutsalbooking.Model.Banner;
import com.ns.quenfutsalbooking.Model.BookingInformation;
import com.ns.quenfutsalbooking.Service.PicassoLoadingService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadListener, ILapanganInterface, IBookingInfoLoadListener, IBookingInformationChangeListener {

//    Button btn_logout,btn_verifikasi;
//    ImageView txt_verifikasi;
    ImageView btnLogout;
    TextView txt_fullname, txt_noHp,txt_tempat_booking, txt_alamat_booking, txt_jam_booking, txt_lapangan_booking, txt_jam_remain;
    Slider banner_slider;
    RecyclerView recyler_lapangan;
    Button btnChangeBooking,btnDeleteBooking,btnPayBooking;
    AlertDialog dialog;

    CollectionReference banner_ref, lapangan_ref;

    IBannerLoadListener iBannerLoadListener;
    ILapanganInterface iLapanganInterface;
    IBookingInformationChangeListener iBookingInformationChangeListener;

    IBookingInfoLoadListener iBookingInfoLoadListener;

    CardView card_booking, card_booking_info;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser fUser;

    String userID;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Dexter.withActivity(this)
                .withPermissions(new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();



//        btn_logout = findViewById(R.id.btn_logout);
//        btn_verifikasi = findViewById(R.id.btn_verifikasi);
//        txt_verifikasi = findViewById(R.id.txt_verifikasi);

        txt_fullname = findViewById(R.id.txt_fullname);
        txt_noHp = findViewById(R.id.txt_noHp);
        banner_slider = findViewById(R.id.banner_slider);
        recyler_lapangan = findViewById(R.id.recycler_banner);
        card_booking = findViewById(R.id.card_view_booking);
        btnLogout = findViewById(R.id.btnLogout);

        dialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
        dialog.show();

        //booking Information
        card_booking_info = findViewById(R.id.card_booking_info);
        txt_tempat_booking = findViewById(R.id.txt_tempat_booking);
        txt_alamat_booking = findViewById(R.id.txt_alamat_booking);
        txt_jam_booking = findViewById(R.id.txt_jam_booking);
        txt_lapangan_booking = findViewById(R.id.txt_lapangan_booking);
        txt_jam_remain = findViewById(R.id.txt_jam_remain);
        btnChangeBooking = findViewById(R.id.btnChangeBooking);
        btnDeleteBooking = findViewById(R.id.btnDeleteBooking);
        btnPayBooking = findViewById(R.id.btnPayBooking);


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        fUser = fAuth.getCurrentUser();

        userID = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        Common.currentUser = userID;


        banner_ref = FirebaseFirestore.getInstance().collection("Banner");
        lapangan_ref = FirebaseFirestore.getInstance().collection("Lapangan");

        setUserInformation();
        loadBanner();
        loadLapangan();
        loadUserBooking();

        card_booking.setOnClickListener(v ->
                startActivity(new Intent(this,BookingActivity.class)));

        btnDeleteBooking.setOnClickListener(v ->
                deleteBooking(false));

        btnChangeBooking.setOnClickListener(v ->
                changeBookingFromUser());

        btnPayBooking.setOnClickListener(v ->
                startActivity(new Intent(this,PaymentActivity.class)));

        Slider.init(new PicassoLoadingService());
        iBannerLoadListener = this;
        iLapanganInterface = this;
        iBookingInfoLoadListener = this;
        iBookingInformationChangeListener = this;

//            btn_verifikasi.setOnClickListener(v -> {
//                assert fUser != null;
//                fUser.sendEmailVerification().addOnSuccessListener(aVoid ->
//                        Toast.makeText(MainActivity.this, "Link Verifikasi Email Terkirim...", Toast.LENGTH_SHORT).show());
//            });
//
//
//
//        btn_logout.setOnClickListener(v -> {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        });

        btnLogout.setOnClickListener(view -> {
            fAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }

    private void changeBookingFromUser() {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Ubah Booking ?")
                .setMessage("Data Booking anda sebelumnya akan terhapus, tetap lakukan perubahan?")
                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Oke", (dialog, which) -> deleteBooking(true));
        confirmDialog.show();
    }

    private void deleteBooking(boolean isChange) {

        if (Common.currentBooking != null){

            dialog.show();
            DocumentReference lapanganBookingInfo = FirebaseFirestore.getInstance()
                    .collection("DaftarTempatFutsal")
                    .document(Common.currentBooking.getCityBook())
                    .collection("Tempat")
                    .document(Common.currentBooking.getTempatId())
                    .collection("Lapangan")
                    .document(Common.currentBooking.getLapanganId())
                    .collection(Common.convertTimeSlotToStringKey(Common.currentBooking.getTimestamp()))
                    .document(Common.currentBooking.getSlot().toString());

            lapanganBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    deleteBookingFromUser(isChange);
                }
            });
        }else{
            Toast.makeText(this, "Curentbooking", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBookingFromUser(boolean isChange) {
        if (!TextUtils.isEmpty(Common.currentBookingId)){
            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(fAuth.getCurrentUser().getUid())
                    .collection("Booking")
                    .document(Common.currentBookingId);

            documentReference.delete().addOnFailureListener(e -> {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }).addOnSuccessListener(aVoid -> {
                Paper.init(this);
                Uri eventUri = Uri.parse(Paper.book().read(Common.EVENT_URI_CACHE).toString());
                this.getContentResolver().delete(eventUri,null,null);

                Toast.makeText(this, "Berhasil hapus Booking", Toast.LENGTH_SHORT).show();

                loadUserBooking();
                if (isChange)
                    iBookingInformationChangeListener.onBookingInformationChange();
            });
        }
    }

    private void loadLapangan() {
        lapangan_ref.get().addOnCompleteListener(task -> {
            List<Banner> lapangans = new ArrayList<>();
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot bannerSnapshot:task.getResult()){
                    Banner banner = bannerSnapshot.toObject(Banner.class);
                    lapangans.add(banner);
                }
                iLapanganInterface.onLapanganLoadSuccess(lapangans);
            }
        }).addOnFailureListener(e -> iLapanganInterface.onLapanganLoadFailed(e.getMessage()));
    }

    private void loadBanner() {
        banner_ref.get().addOnCompleteListener(task -> {
            List<Banner> banners = new ArrayList<>();
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot bannerSnapshot:task.getResult()){
                    Banner banner = bannerSnapshot.toObject(Banner.class);
                    banners.add(banner);
                }
                iBannerLoadListener.onBannerLoadSuccess(banners);
            }
        }).addOnFailureListener(e -> iBannerLoadListener.onBannerLoadFailed(e.getMessage()));
    }

    private void setUserInformation() {
        DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, (value, error) -> {
            assert value != null;

            txt_fullname.setText(value.getString("fullname"));
            txt_noHp.setText(value.getString("no_hp"));

            Common.fullname = value.getString("fullname");
            Common.no_hp = value.getString("no_hp");
            Common.email = value.getString("email");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserBooking();
    }

    private void loadUserBooking() {

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

    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSlideAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLapanganLoadSuccess(List<Banner> bannerss) {
        recyler_lapangan.setHasFixedSize(true);
        recyler_lapangan.setLayoutManager(new LinearLayoutManager(this));
        recyler_lapangan.setAdapter(new LayoutLapanganAdapter(this,bannerss));
        dialog.dismiss();
    }

    @Override
    public void onLapanganLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onBookingInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
    }

    @Override
    public void onBookingInfoLoadSuccess(BookingInformation bookingInformation, String bookingId) {

        Common.currentBooking = bookingInformation;
        Common.currentBookingId = bookingId;

        txt_alamat_booking.setText(bookingInformation.getTempatAddress());
        txt_tempat_booking.setText(bookingInformation.getTempatName());
        txt_jam_booking.setText(bookingInformation.getTime());
        txt_lapangan_booking.setText(bookingInformation.getLapanganName());

        String dateRemain = DateUtils.getRelativeTimeSpanString(
                Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                        Calendar.getInstance().getTimeInMillis(),0).toString();
        txt_jam_remain.setText(dateRemain);

        card_booking_info.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBookingInfoLoadFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingInformationChange() {
        startActivity(new Intent(this,BookingActivity.class));
    }

    //    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (!fUser.isEmailVerified()){
//            btn_verifikasi.setVisibility(View.VISIBLE);
//            txt_verifikasi.setVisibility(View.INVISIBLE);
//        } else {
//            btn_verifikasi.setVisibility(View.GONE);
//            txt_verifikasi.setVisibility(View.VISIBLE);
//        }
//    }
}