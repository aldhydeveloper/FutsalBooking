package com.ns.quenfutsalbooking.Interface;

import java.util.List;

public interface ITempatFutsal {
    void onITempatFutsalLoadSuccess(List<String> namaTempat);
    void onITempatFutsalLoadFailed(String message);
}
