package com.ns.quenfutsalbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ns.quenfutsalbooking.Adapter.MyViewPagerAdapter;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Common.NonSwipeViewPager;
import com.ns.quenfutsalbooking.Model.Lapangan;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog adialog;
    CollectionReference lapanganRef;

    StepView stepView;
    NonSwipeViewPager viewPager;
    Button btn_previous, btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        stepView = findViewById(R.id.step_view);
        viewPager = findViewById(R.id.view_pager);
        btn_previous = findViewById(R.id.btn_previous);
        btn_next = findViewById(R.id.btn_next);
        ButterKnife.bind(BookingActivity.this);

        adialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();


        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(Common.KEY_ENABLE_NEXT));
        setUpStepView();
        setButton();

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                stepView.go(position, true);
                if (position == 0)
                    btn_previous.setEnabled(false);
                else
                    btn_previous.setEnabled(true);
                btn_next.setEnabled(false);
                setButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.btn_previous)
    void previousStep() {
        if (Common.step == 3 || Common.step > 0) {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
            if (Common.step < 3){
                btn_next.setEnabled(true);
                setButton();
            }
        }
    }

    @OnClick(R.id.btn_next)
    void nextClick() {
        if (Common.step < 3 || Common.step == 0) {
            Common.step++;
            if (Common.step == 1) {
                if (Common.currentTempat != null)
                    loadLapanganByTempat(Common.currentTempat.getTempatId());
            } else if(Common.step == 2){
                if (Common.currentLapangan != null){
                    loadTimeSlotOFLapangan(Common.currentLapangan.getLapanganId());
                }
            }
            else if(Common.step == 3){
                if (Common.currentTimeSlot != -1){
                    confirmBooking();
                }
            }
            viewPager.setCurrentItem(Common.step);
        }
    }

    private void confirmBooking() {
        Intent intent = new Intent(Common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotOFLapangan(String lapanganId) {
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadLapanganByTempat(String tempatId) {
        adialog.show();
///DaftarTempatFutsal/Bandung/Tempat/6NhE4qa7XhTeJTvxbYzM/Lapangan
        if (!TextUtils.isEmpty(Common.tempat)) {
            lapanganRef = FirebaseFirestore.getInstance()
                    .collection("DaftarTempatFutsal")
                    .document(Common.tempat)
                    .collection("Tempat")
                    .document(tempatId)
                    .collection("Lapangan");

            lapanganRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Lapangan> lapangans = new ArrayList<>();
                            for (QueryDocumentSnapshot lapanganSnapshot:task.getResult()){
                                Lapangan lapangan = lapanganSnapshot.toObject(Lapangan.class);
                                lapangan.setLapanganId(lapanganSnapshot.getId());
                                lapangans.add(lapangan);
                            }

                            Intent intent = new Intent(Common.KEY_LAPANGAN_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_LAPANGAN_LOAD_DONE,lapangans);
                            localBroadcastManager.sendBroadcast(intent);

                            adialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            adialog.dismiss();
                        }
                    });
        }

    }

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if (step == 1)
                Common.currentTempat = intent.getParcelableExtra(Common.KEY_LAPANGAN);
            else if (step == 2)
                Common.currentLapangan = intent.getParcelableExtra(Common.KEY_LAPANGAN_SELECTED);
            else if (step == 3)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);
            btn_next.setEnabled(true);
            setButton();
        }
    };

    private void setButton() {
        if (btn_next.isEnabled()) {
            btn_next.setBackgroundResource(R.color.light_black);
        } else {
            btn_next.setBackgroundResource(R.color.grey);
        }

        if (btn_previous.isEnabled()) {
            btn_previous.setBackgroundResource(R.color.light_black);
        } else {
            btn_previous.setBackgroundResource(R.color.grey);
        }
    }

    private void setUpStepView() {
        List<String> steplist = new ArrayList<>();
        steplist.add("Tempat Futsal");
        steplist.add("Lapangan");
        steplist.add("Jam");
        steplist.add("Konfirmasi");
        stepView.setSteps(steplist);
    }
}