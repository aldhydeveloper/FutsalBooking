package com.ns.quenfutsalbooking.Interface;

import com.ns.quenfutsalbooking.Model.TempatFutsal;

import java.util.List;

public interface IBranchLoadListener {
    void onIBranchLoadSuccess(List<TempatFutsal> tempatFutsalList);
    void onIBranchLoadFailed(String message);
}
