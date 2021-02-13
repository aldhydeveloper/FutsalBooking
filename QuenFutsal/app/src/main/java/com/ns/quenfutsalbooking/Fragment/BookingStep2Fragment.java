package com.ns.quenfutsalbooking.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.quenfutsalbooking.Adapter.MyLapanganAdapter;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Common.SpacesItemDecoration;
import com.ns.quenfutsalbooking.Model.Lapangan;
import com.ns.quenfutsalbooking.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_lapangans)
    RecyclerView recycler_lpangan;

    private BroadcastReceiver lapanganDoneReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Lapangan> lapanganArrayList = intent.getParcelableArrayListExtra(Common.KEY_LAPANGAN_LOAD_DONE);
            MyLapanganAdapter adapter = new MyLapanganAdapter(getContext(),lapanganArrayList);
            recycler_lpangan.setAdapter(adapter);
        }
    };

    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(lapanganDoneReciver, new IntentFilter(Common.KEY_LAPANGAN_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(lapanganDoneReciver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step2,container,false);

        unbinder = ButterKnife.bind(this,itemView);
        
        initView();
        
        return itemView;
    }

    private void initView() {
        recycler_lpangan.setHasFixedSize(true);
        recycler_lpangan.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_lpangan.addItemDecoration(new SpacesItemDecoration(4));
    }
}

