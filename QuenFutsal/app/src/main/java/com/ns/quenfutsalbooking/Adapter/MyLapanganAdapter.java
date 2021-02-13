package com.ns.quenfutsalbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.quenfutsalbooking.Common.Common;
import com.ns.quenfutsalbooking.Interface.IRecycleItemSelectedListener;
import com.ns.quenfutsalbooking.Model.Lapangan;
import com.ns.quenfutsalbooking.R;

import java.util.ArrayList;
import java.util.List;

public class MyLapanganAdapter extends RecyclerView.Adapter<MyLapanganAdapter.MyViewHolder> {

    Context context;
    List<Lapangan> lapanganList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyLapanganAdapter(Context context, List<Lapangan> lapanganList) {
        this.context = context;
        this.lapanganList = lapanganList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_lapangans, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_name_lapangan.setText(lapanganList.get(position).getName());
        holder.rtb_lapangan.setRating((float) lapanganList.get(position).getRatting());
        if (!cardViewList.contains(holder.card_lapangan))
            cardViewList.add(holder.card_lapangan);

        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                for (CardView cardView : cardViewList){
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.white));
                }
                holder.card_lapangan.setCardBackgroundColor(
                        context.getResources().getColor(android.R.color.holo_green_light)
                );

                Intent intent = new Intent(Common.KEY_ENABLE_NEXT);
                intent.putExtra(Common.KEY_LAPANGAN_SELECTED,lapanganList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lapanganList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_name_lapangan;
        ImageView img_lapangan1;
        RatingBar rtb_lapangan;
        CardView card_lapangan;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name_lapangan = (TextView) itemView.findViewById(R.id.txt_name_lapangan);
            img_lapangan1 = (ImageView) itemView.findViewById(R.id.img_lapangan1);
            rtb_lapangan = (RatingBar) itemView.findViewById(R.id.rtb_lapangan);
            card_lapangan = (CardView) itemView.findViewById(R.id.card_lapangan);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }
}
