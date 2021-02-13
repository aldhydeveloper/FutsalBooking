package com.ns.quenfutsalbooking.Fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Interface.ITimeSlotLoadListener;
import com.ns.quenfutsalbooking.Model.BookingInformation;
import com.ns.quenfutsalbooking.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    ITimeSlotLoadListener iTimeSlotLoadListener;
    AlertDialog dialog;


    String user;

    int countQty = 0;

    Unbinder unbinder;

    @BindView(R.id.txt_booking_time)
    TextView txt_booking_time;
    @BindView(R.id.txt_booking_lapangan)
    TextView txt_booking_lapangan;
    @BindView(R.id.txt_booking_place)
    TextView txt_booking_place;
    @BindView(R.id.txt_booking_address)
    TextView txt_booking_address;
    @BindView(R.id.txt_place_clock)
    TextView txt_place_clock;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvExtraTime)
    TextView tvExtraTime;


    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {

        txt_booking_time.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append("  Tanggal  ")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())));
        txt_booking_lapangan.setText(Common.currentLapangan.getName());
        tvPrice.setText(Common.currentLapangan.getHarga());
        txt_booking_place.setText(Common.currentTempat.getName());
        txt_booking_address.setText(Common.currentTempat.getAddress());
        txt_place_clock.setText(Common.currentTempat.getOpenHours());
    }


    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @OnClick(R.id.btnPlus)
    void plusExtraTime() {
        countQty++;
        tvExtraTime.setText(String.format("%d", countQty));
    }

    @OnClick(R.id.btnMinus)
    void minusExtraTime() {
        if (countQty <= 0) countQty = 0;
        else countQty--;
        tvExtraTime.setText(String.format("%d", countQty));
    }

    @OnClick(R.id.btn_confirm)
    void comfirmBooking() {

        if (tvExtraTime.getText().equals("0")) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Extra Time");
            alertDialog.setMessage("Apakah anda tidak akan menambah 'Extra Time?' ");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Oke",
                    (dialog, which) -> {

                        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
                        String[] convertTime = startTime.split("-"); //split 09:00 - 10:00
                        String[] startTimeConvert = convertTime[0].split(":"); // get :
                        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());// get 9
                        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());// get 00

                        Calendar bookingDateWithHourHouse = Calendar.getInstance();
                        bookingDateWithHourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
                        bookingDateWithHourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
                        bookingDateWithHourHouse.set(Calendar.MINUTE, startMinInt);

                        Timestamp timestamp = new Timestamp(bookingDateWithHourHouse.getTime());


                        BookingInformation bookingInformation = new BookingInformation();

                        bookingInformation.setCityBook(Common.tempat);
                        bookingInformation.setTimestamp(timestamp);
                        bookingInformation.setDone(false);
                        bookingInformation.setCostumerName(Common.fullname);
                        bookingInformation.setCostumerPhone(Common.no_hp);
                        bookingInformation.setCostumerEmail(Common.email);
                        bookingInformation.setTempatId(Common.currentTempat.getTempatId());
                        bookingInformation.setTempatName(Common.currentTempat.getName());
                        bookingInformation.setTempatAddress(Common.currentTempat.getAddress());
                        bookingInformation.setTempatOpenHours(Common.currentTempat.getOpenHours());
                        bookingInformation.setLapanganId(Common.currentLapangan.getLapanganId());
                        bookingInformation.setHarga(Common.currentLapangan.getHarga());
                        bookingInformation.setExtraTime(tvExtraTime.getText().toString());
                        bookingInformation.setLapanganName(Common.currentTempat.getName());
                        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));
                        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                                .append("  Tanggal  ")
                                .append(simpleDateFormat.format(bookingDateWithHourHouse.getTime())).toString());

                        user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                                .collection("DaftarTempatFutsal")
                                .document(Common.tempat)
                                .collection("Tempat")
                                .document(Common.currentTempat.getTempatId())
                                .collection("Lapangan")
                                .document(Common.currentLapangan.getLapanganId())
                                .collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                                .document(String.valueOf(Common.currentTimeSlot));

                        bookingDate.set(bookingInformation).addOnSuccessListener(aVoid ->
                                addToUserBooking(bookingInformation))
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show());

                        dialog.dismiss();
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Tambah",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        } else {

            dialog.show();


            String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
            String[] convertTime = startTime.split("-"); //split 09:00 - 10:00
            String[] startTimeConvert = convertTime[0].split(":"); // get :
            int startHourInt = Integer.parseInt(startTimeConvert[0].trim());// get 9
            int startMinInt = Integer.parseInt(startTimeConvert[1].trim());// get 00

            Calendar bookingDateWithHourHouse = Calendar.getInstance();
            bookingDateWithHourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
            bookingDateWithHourHouse.set(Calendar.HOUR_OF_DAY, startHourInt);
            bookingDateWithHourHouse.set(Calendar.MINUTE, startMinInt);

            Timestamp timestamp = new Timestamp(bookingDateWithHourHouse.getTime());


            BookingInformation bookingInformation = new BookingInformation();

            bookingInformation.setCityBook(Common.tempat);
            bookingInformation.setTimestamp(timestamp);
            bookingInformation.setDone(false);
            bookingInformation.setCostumerName(Common.fullname);
            bookingInformation.setCostumerPhone(Common.no_hp);
            bookingInformation.setTempatId(Common.currentTempat.getTempatId());
            bookingInformation.setTempatName(Common.currentTempat.getName());
            bookingInformation.setTempatAddress(Common.currentTempat.getAddress());
            bookingInformation.setTempatOpenHours(Common.currentTempat.getOpenHours());
            bookingInformation.setLapanganId(Common.currentLapangan.getLapanganId());
            bookingInformation.setHarga(Common.currentLapangan.getHarga());
            bookingInformation.setExtraTime(tvExtraTime.getText().toString());
            bookingInformation.setLapanganName(Common.currentTempat.getName());
            bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));
            bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                    .append("  Tanggal  ")
                    .append(simpleDateFormat.format(bookingDateWithHourHouse.getTime())).toString());

            user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            DocumentReference bookingDate = FirebaseFirestore.getInstance()
                    .collection("DaftarTempatFutsal")
                    .document(Common.tempat)
                    .collection("Tempat")
                    .document(Common.currentTempat.getTempatId())
                    .collection("Lapangan")
                    .document(Common.currentLapangan.getLapanganId())
                    .collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                    .document(String.valueOf(Common.currentTimeSlot));

            bookingDate.set(bookingInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    addToUserBooking(bookingInformation);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addToUserBooking(BookingInformation bookingInformation) {


        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("users")
                .document(Common.currentUser)
                .collection("Booking");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.MINUTE, 0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());
        userBooking
                .whereGreaterThanOrEqualTo("timestamp", toDayTimeStamp)
                .whereEqualTo("done", false)
                .get().addOnCompleteListener(task -> {
            if (task.getResult().isEmpty()) {
                userBooking.document()
                        .set(bookingInformation)
                        .addOnSuccessListener(aVoid -> {

                            if (dialog.isShowing())
                                dialog.dismiss();

                            addToCalendar(Common.bookingDate,
                                    Common.convertTimeSlotToString(Common.currentTimeSlot));
                            resetStaticData();
                            getActivity().finish();
                            Toast.makeText(getContext(), "Sukses Booking Lapangan", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                if (dialog.isShowing())
                    dialog.dismiss();
                resetStaticData();
                getActivity().finish();
                Toast.makeText(getContext(), "Sukses Booking Lapangan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCalendar(Calendar bookingDate, String convertTimeSlotToString) {
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); //split 09:00 - 10:00
        String[] startTimeConvert = convertTime[0].split(":"); // get :
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());// get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());// get 00

        String[] endTimeConvert = convertTime[1].split(":"); // get :
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim());// get 10
        int endtMinInt = Integer.parseInt(endTimeConvert[1].trim());// get 00

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY, startHourInt);
        startEvent.set(Calendar.MINUTE, startMinInt);

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY, endHourInt);
        endEvent.set(Calendar.MINUTE, endtMinInt);

        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startEvenTime = calendarDateFormat.format(startEvent.getTime());
        String endEvenTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEvenTime, endEvenTime, "Booking Lapangan Futsal",
                new StringBuilder("Detail Booking")
                        .append(startTime)
                        .append(" Lapangan : ")
                        .append(Common.currentLapangan.getName())
                        .append(" Di ")
                        .append(Common.currentTempat.getName()).toString(),
                new StringBuilder("Alamat: ")
                        .append(Common.currentTempat.getAddress()).toString());


    }

    private void addToDeviceCalendar(String startEvenTime, String endEvenTime, String title, String detail_booking, String location) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try {
            Date start = calendarDateFormat.parse(startEvenTime);
            Date end = calendarDateFormat.parse(endEvenTime);
            ContentValues event = new ContentValues();

            //put
            event.put(CalendarContract.Events.CALENDAR_ID, getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE, title);
            event.put(CalendarContract.Events.DESCRIPTION, detail_booking);
            event.put(CalendarContract.Events.EVENT_LOCATION, location);

            //time
            event.put(CalendarContract.Events.DTSTART, start.getTime());
            event.put(CalendarContract.Events.DTEND, end.getTime());
            event.put(CalendarContract.Events.ALL_DAY, 0);
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

            Uri calendars;
            if (Build.VERSION.SDK_INT >= 0)
                calendars = Uri.parse("content://com.android.calendar/events");
            else
                calendars = Uri.parse("content://calendar/events");

            Uri uri_save = getActivity().getContentResolver().insert(calendars, event);
            //save to cache
            Paper.init(getActivity());
            Paper.book().write(Common.EVENT_URI_CACHE, uri_save.toString());


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getCalendar(Context context) {
        String gmailIdCalendar = "";
        String projection[] = {"_id", "calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);
        if (managedCursor.moveToFirst()) {
            String callName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do {
                callName = managedCursor.getString(nameCol);
                if (callName.contains("@gmail.com")) {
                    gmailIdCalendar = managedCursor.getString(idCol);
                    break;
                }
            } while (managedCursor.moveToNext());
            managedCursor.close();
        }

        return gmailIdCalendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentTempat = null;
        Common.currentLapangan = null;
        Common.bookingDate.add(Calendar.DATE, 0);
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fregment_booking_step4, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}