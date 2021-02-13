package com.ns.quenfutsalbooking.Interface;

import com.ns.quenfutsalbooking.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
