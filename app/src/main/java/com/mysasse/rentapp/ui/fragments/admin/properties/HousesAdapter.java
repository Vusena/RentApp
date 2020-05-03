package com.mysasse.rentapp.ui.fragments.admin.properties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.models.House;

import java.util.List;

public class HousesAdapter extends RecyclerView.Adapter<HousesAdapter.HouseViewHolder> {

    private List<House> houseList;

    public HousesAdapter(List<House> houseList) {
        this.houseList = houseList;
    }

    @NonNull
    @Override
    public HouseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HouseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_house_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HouseViewHolder holder, int position) {
        holder.houseNameTv.setText(houseList.get(position).getName());
        holder.houseCostPerRoomTv.setText(String.format("KES %.2f per room", houseList.get(position).getCostPerRoom()));

        Bitmap bitmap = BitmapFactory.decodeByteArray(houseList.get(position).getImage(), 0, houseList.get(position).getImage().length);
        holder.houseIv.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return houseList.size();
    }

    public class HouseViewHolder extends RecyclerView.ViewHolder {
        ImageView houseIv;
        TextView houseNameTv, houseCostPerRoomTv;

        public HouseViewHolder(@NonNull View itemView) {
            super(itemView);

            houseIv = itemView.findViewById(R.id.house_iv);
            houseNameTv = itemView.findViewById(R.id.house_name_tv);
            houseCostPerRoomTv = itemView.findViewById(R.id.house_cost_per_room_tv);

        }
    }
}
