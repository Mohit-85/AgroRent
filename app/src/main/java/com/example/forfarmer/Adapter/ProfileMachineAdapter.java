package com.example.forfarmer.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfarmer.Class.Machine;
import com.example.forfarmer.R;

import java.util.List;

public class ProfileMachineAdapter extends RecyclerView.Adapter<ProfileMachineAdapter.MachineViewHolder> {

    private Context context;
    private List<Machine> machineList;

    public ProfileMachineAdapter(Context context, List<Machine> machineList) {
        this.context = context;
        this.machineList = machineList;
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_machine_item_grid, parent, false);
        return new MachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        Machine machine = machineList.get(position);

        // Set machine details
        holder.machineNameTextView.setText(machine.getMachineName());
        holder.priceTextView.setText("₹" + machine.getPrice() + " /Day");


        // Load machine image
        if (machine.getImageUrl() != null && !machine.getImageUrl().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(machine.getImageUrl(), Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.machineImageView.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                Log.e("ProfileMachineAdapter", "Error decoding Base64", e);
                holder.machineImageView.setImageResource(R.drawable.img);
            }
        } else {
            holder.machineImageView.setImageResource(R.drawable.img);
        }
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public static class MachineViewHolder extends RecyclerView.ViewHolder {
        TextView machineNameTextView, priceTextView;
        ImageView machineImageView;

        public MachineViewHolder(@NonNull View itemView) {
            super(itemView);
            machineNameTextView = itemView.findViewById(R.id.machineNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            machineImageView = itemView.findViewById(R.id.machineImageView);
        }
    }
}
