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

public class MachineAdapter extends RecyclerView.Adapter<MachineAdapter.MachineViewHolder> {

    private Context context;
    private List<Machine> machineList;
    OnMachineClickListener clickListener;

    public interface OnMachineClickListener {
        void onMachineClick(Machine machine);
    }

    public MachineAdapter(Context context, List<Machine> machineList , OnMachineClickListener clickListener) {
        this.context = context;
        this.machineList = machineList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MachineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.machine_item, parent, false);
        return new MachineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MachineViewHolder holder, int position) {
        Machine machine = machineList.get(position);

        holder.machineNameTextView.setText(machine.getMachineName());
        holder.priceTextView.setText("₹" + machine.getPrice() + " /Day");
        holder.locationTextView.setText(machine.getLocation());



        // Load image using Glide if base64 string is present
        if (machine.getImageUrl() != null && !machine.getImageUrl().isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(machine.getImageUrl(), Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.machineImageView.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                Log.e("MachineAdapter", "Error decoding Base64", e);
                holder.machineImageView.setImageResource(R.drawable.img);
            }
        } else {
            holder.machineImageView.setImageResource(R.drawable.img);
        }

        holder.itemView.setOnClickListener(v-> clickListener.onMachineClick(machine));


    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public static class MachineViewHolder extends RecyclerView.ViewHolder {
        TextView machineNameTextView, priceTextView, locationTextView;
        ImageView machineImageView;

        public MachineViewHolder(@NonNull View itemView) {
            super(itemView);
            machineNameTextView = itemView.findViewById(R.id.machineNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            machineImageView = itemView.findViewById(R.id.machineImageView);
        }
    }
}

