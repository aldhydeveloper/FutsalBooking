package com.ns.quenfutsalbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Interface.IRecycleItemSelectedListener;
import com.ns.quenfutsalbooking.Model.TempatFutsal;
import com.ns.quenfutsalbooking.R;

import java.util.ArrayList;
import java.util.List;

public class MyTempatAdapter extends RecyclerView.Adapter<MyTempatAdapter.MyViewHolder> {

    Context context;
    List<TempatFutsal> futsalList;
    List<CardView> cardViewsList;
    LocalBroadcastManager localBroadcastManager;

    public MyTempatAdapter(Context context, List<TempatFutsal> futsalList) {
        this.context = context;
        this.futsalList = futsalList;
        cardViewsList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyTempatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_tempatfutsal,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTempatAdapter.MyViewHolder holder, int position) {
        holder.txt_nama_tempat.setText(futsalList.get(position).getName());
        holder.txt_alamat_tempat.setText(futsalList.get(position).getAddress());
        if (!cardViewsList.contains(holder.card_lapangan))
            cardViewsList.add(holder.card_lapangan);

        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for (CardView cardView:cardViewsList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                holder.card_lapangan.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_green_light));

                Intent intent = new Intent(Common.KEY_ENABLE_NEXT);
                intent.putExtra(Common.KEY_LAPANGAN,futsalList.get(pos));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return futsalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_nama_tempat, txt_alamat_tempat;
        CardView card_lapangan;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nama_tempat = (TextView)itemView.findViewById(R.id.txt_nama_tempat);
            txt_alamat_tempat = (TextView)itemView.findViewById(R.id.txt_alamat_tempat);
            card_lapangan = (CardView)itemView.findViewById(R.id.card_tempatFutsal);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
