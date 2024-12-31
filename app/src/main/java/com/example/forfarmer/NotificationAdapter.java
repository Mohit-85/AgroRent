package com.example.forfarmer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;
    private String userPath;

    public NotificationAdapter(List<Notification> notifications , Context context , String userPath) {
        this.notifications = notifications;
        this.context = context;
        this.userPath = userPath;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder , @SuppressLint("RecyclerView") int position) {
        Notification notification = notifications.get(position);
        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());

        Date date = new Date(notification.getTimestamp());
        String formattedDate = DateFormat.getDateTimeInstance().format(date);

        holder.timestampTextView.setText(formattedDate);
        holder.phonetxt.setText(notification.getPhone());

        // press call Button to call bookin person
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = notification.getPhone();
                Toast.makeText(context, "Calling " + phone, Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL , Uri.parse("tel:" + phone));
                context.startActivity(callIntent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showDetailsDialog(notification , position);
                return true;
            }
        });


    }
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    private void showDetailsDialog(Notification notification , int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Notification ")
                .setMessage("Are you sure you want to delete this notification?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deletenotification(notification,position);
                }).setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                }).show();

    }
    private void deletenotification(Notification notification, int position) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("notifications")
                .child(userPath)
                .child(notification.getNotificationId());

        notificationsRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Ensure the position is within bounds before removing
                    if (position >= 0 && position < notifications.size()) {
                        notifications.remove(position); // Remove the item from the list
                        notifyItemRemoved(position);    // Notify RecyclerView of item removal
                        notifyItemRangeChanged(position, notifications.size()); // Update subsequent items
                        Toast.makeText(context, "Notification deleted successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Invalid position for deletion.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to delete notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, messageTextView, timestampTextView , phonetxt;
        Button callbtn;


        public NotificationViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notificationTitle);
            messageTextView = itemView.findViewById(R.id.notificationMessage);
            timestampTextView = itemView.findViewById(R.id.notificationTimestamp);
            phonetxt = itemView.findViewById(R.id.personPhoneNumber);
            callbtn = itemView.findViewById(R.id.callButton);

        }
    }


}
