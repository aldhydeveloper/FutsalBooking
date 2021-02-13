package com.ns.quenfutsalbooking.Common;

import com.google.firebase.Timestamp;
import com.ns.quenfutsalbooking.Model.BookingInformation;
import com.ns.quenfutsalbooking.Model.Lapangan;
import com.ns.quenfutsalbooking.Model.TempatFutsal;
import com.ns.quenfutsalbooking.Model.TimeSlot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Common {
    public static final String KEY_ENABLE_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_LAPANGAN = "LAPANGAN_SAVE";
    public static final String KEY_LAPANGAN_LOAD_DONE = "LAPANGAN_LOAD_DONE";
    public static final String KEY_DISPLAY_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_STEP = "STEP";
    public static final String KEY_LAPANGAN_SELECTED = "LAPANGAN_SELECTED";
    public static final int TIME_SLOT_TOTAL = 15;
    public static final Object DISABLE_TAG = "DISABLE" ;
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
    public static TempatFutsal currentTempat;

    public static int step = 0;
    public static String tempat = "";
    public static String fullname = "";
    public static String currentUser = "";
    public static String no_hp = "";
    public static String email = "";
    public static Lapangan currentLapangan;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd_MM_yyyy");
    public static BookingInformation currentBooking;
    public static String currentBookingId="";


    public static String convertTimeSlotToString(int slot) {
        switch (slot) {
            case 0:
                return "07:00 - 08:00";
            case 1:
                return "08:00 - 09:00";
            case 2:
                return "09:00 - 10:00";
            case 3:
                return "10:00 - 11:00";
            case 4:
                return "11:00 - 12:00";
            case 5:
                return "12:00 - 13:00";
            case 6:
                return "13:00 - 14:00";
            case 7:
                return "14:00 - 15:00";
            case 8:
                return "15:00 - 16:00";
            case 9:
                return "16:00 - 17:00";
            case 10:
                return "17:00 - 18:00";
            case 11:
                return "18:00 - 19:00";
            case 12:
                return "19:00 - 20:00";
            case 13:
                return "20:00 - 21:00";
            case 14:
                return "21:00 - 22:00";
            default:
                return "Closed";
        }
    }

    public static String convertTimeSlotToStringKey(Timestamp timestamp) {
        Date date = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(date);
    }
}

