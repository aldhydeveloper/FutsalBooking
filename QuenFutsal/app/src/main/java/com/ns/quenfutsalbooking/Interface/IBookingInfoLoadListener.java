package com.ns.quenfutsalbooking.Interface;

import com.ns.quenfutsalbooking.Model.BookingInformation;

public interface IBookingInfoLoadListener {
    void onBookingInfoLoadEmpty();
    void onBookingInfoLoadSuccess(BookingInformation bookingInformation, String documentId);
    void onBookingInfoLoadFailed(String message);
}
