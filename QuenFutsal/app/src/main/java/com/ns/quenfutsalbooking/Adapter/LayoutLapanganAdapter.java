package com.ns.quenfutsalbooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ns.quenfutsalbooking.DetailTempatActivity;
import com.ns.quenfutsalbooking.Model.Banner;
import com.ns.quenfutsalbooking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LayoutLapanganAdapter extends RecyclerView.Adapter<LayoutLapanganAdapter.MyViweHolder> {
    Context context;
    List<Banner> lapangans;

    public LayoutLapanganAdapter(Context context, List<Banner> lapangans) {
        this.context = context;
        this.lapangans = lapangans;
    }

    @NonNull
    @Override
    public MyViweHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_lapangan, parent, false);
        return new MyViweHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViweHolder holder, int position) {
        Picasso.get().load(lapangans.get(position).getImage()).into(holder.imageView);
        holder.tvTitlePlace.setText(lapangans.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> {

            context.startActivity(new Intent(context, DetailTempatActivity.class)
                    .putExtra("idPlace", lapangans.get(position).getId())
                    .putExtra("image", lapangans.get(position).getImage())
                    .putExtra("tile", lapangans.get(position).getTitle())
                    .putExtra("latitude", lapangans.get(position).getLat())
                    .putExtra("longitude", lapangans.get(position).getLon()));
        });
    }

    @Override
    public int getItemCount() {
        return lapangans.size();
    }

    public class MyViweHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvTitlePlace;

        public MyViweHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_lapangan);
            tvTitlePlace = itemView.findViewById(R.id.tvTitlePlace);
        }
    }
}
