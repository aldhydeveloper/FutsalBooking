package com.ns.quenfutsalbooking.Interface;

import com.ns.quenfutsalbooking.Model.Banner;

import java.util.List;

public interface ILapanganInterface {
    void onLapanganLoadSuccess(List<Banner> banners);
    void onLapanganLoadFailed(String message);
}
