package com.ns.quenfutsalbooking.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ns.quenfutsalbooking.Adapter.MyTempatAdapter;
import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Common.SpacesItemDecoration;
import com.ns.quenfutsalbooking.Interface.IBranchLoadListener;
import com.ns.quenfutsalbooking.Interface.ITempatFutsal;
import com.ns.quenfutsalbooking.Model.TempatFutsal;
import com.ns.quenfutsalbooking.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements ITempatFutsal, IBranchLoadListener {

    CollectionReference tempatFutsalRef, branchRef;
    ITempatFutsal iTempatFutsal;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_tempatFutsal)
    RecyclerView recyclerView;

    Unbinder unbinder;

    AlertDialog alertDialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance(){
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempatFutsalRef = FirebaseFirestore.getInstance().collection("DaftarTempatFutsal");
        iTempatFutsal = this;
        iBranchLoadListener = this;

        alertDialog = new SpotsDialog.Builder().setContext(getActivity()).setCancelable(false).build();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemview = inflater.inflate(R.layout.fragment_booking_step1,container,false);
        unbinder = ButterKnife.bind(this,itemview);

        initView();
        loadTempatLapangan();
        
        return itemview;
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadTempatLapangan() {
        tempatFutsalRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<String> list = new ArrayList<>();
                list.add("Pilih Lokasi Tempat Futsal");
                for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                    list.add(documentSnapshot.getId());
                iTempatFutsal.onITempatFutsalLoadSuccess(list);
            }
        }).addOnFailureListener(e -> iTempatFutsal.onITempatFutsalLoadFailed(e.getMessage()));
    }

    @Override
    public void onITempatFutsalLoadSuccess(List<String> namaTempat) {
        spinner.setItems(namaTempat);
        spinner.setOnItemSelectedListener((view, position, id, item) -> {
            if (position > 0){
                loadBranch(item.toString());
            }
        });
    }

    private void loadBranch(String namaKota) {
        alertDialog.show();

        Common.tempat = namaKota;
        branchRef = FirebaseFirestore.getInstance()
                .collection("DaftarTempatFutsal")
                .document(namaKota)
                .collection("Tempat");

        branchRef.get().addOnCompleteListener(task -> {
            List<TempatFutsal> list = new ArrayList<>();
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                {
                    TempatFutsal tempatFutsal = documentSnapshot.toObject(TempatFutsal.class);
                    tempatFutsal.setTempatId(documentSnapshot.getId());
                    list.add(tempatFutsal);
                }
                iBranchLoadListener.onIBranchLoadSuccess(list);
            }
        }).addOnFailureListener(e ->
                iBranchLoadListener.onIBranchLoadFailed(e.getMessage()));
    }

    @Override
    public void onITempatFutsalLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIBranchLoadSuccess(List<TempatFutsal> tempatFutsalList) {
        MyTempatAdapter adapter = new MyTempatAdapter(getActivity(),tempatFutsalList);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
        alertDialog.dismiss();
    }

    @Override
    public void onIBranchLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        alertDialog.dismiss();
    }
}
