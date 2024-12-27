package com.example.forfarmer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Bookin_machines extends Fragment {

    private ImageView machineImageView;
    private EditText usernameEditText, userContectEditText, startDateEditText, endtDateEditText;
    private TextView machineNameTextView, machinePriceTextView, machineLocationTextView;
    private Button bookButton;
    private DatabaseReference bookingsRef;
    private String machineId, machineName, machinePrice, machineLocation, machineImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookin_machines, container, false);

        bookingsRef = FirebaseDatabase.getInstance().getReference("bookings");

        machineImageView = view.findViewById(R.id.machineImageView);
        machineNameTextView = view.findViewById(R.id.machineNameTextView);
        machinePriceTextView = view.findViewById(R.id.machinePriceTextView);
        machineLocationTextView = view.findViewById(R.id.machineLocationTextView);
        usernameEditText = view.findViewById(R.id.userNameEditText);
        userContectEditText = view.findViewById(R.id.userContactEditText);
        startDateEditText = view.findViewById(R.id.startDateEditText);
        endtDateEditText = view.findViewById(R.id.endDateEditText);
        bookButton = view.findViewById(R.id.bookbutton);

        Bundle args = getArguments();
        if (args != null) {
            machineId = args.getString("machineId");
            machineName = args.getString("machineName");
            machinePrice = args.getString("machinePrice");
            machineLocation = args.getString("machineLocation");
            machineImageUrl = args.getString("machineImageUrl");

            machineNameTextView.setText(machineName);
            machinePriceTextView.setText("Price per day: ₹" + machinePrice);
            machineLocationTextView.setText("Location: " + machineLocation);

            byte[] decodedString = Base64.decode(machineImageUrl, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            machineImageView.setImageBitmap(decodedBitmap);
        }

        bookButton.setOnClickListener(v -> bookMachine());

        return view;
    }

    private void bookMachine() {
        String userName = usernameEditText.getText().toString();
        String userContact = userContectEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endtDateEditText.getText().toString();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();
        String sanitizedOwnerEmail = sanitizeEmail(userEmail);

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userContact) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookingId = bookingsRef.push().getKey();
        Booking booking = new Booking(bookingId, machineId, userName, userContact, startDate, endDate, "Pending", machineName, userEmail);

        bookingsRef.child(bookingId).setValue(booking)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Booking Successful!", Toast.LENGTH_SHORT).show();
                    sendBookingNotificationToOwner(machineId, userName, sanitizedOwnerEmail);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to book: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void sendBookingNotificationToOwner(String machineId, String userName, String sanitizedOwnerEmail) {
        DatabaseReference machinesRef = FirebaseDatabase.getInstance().getReference("machines");
        machinesRef.child(machineId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String phone = userContectEditText.getText().toString();
                    String ownerEmail = dataSnapshot.child("o").getValue(String.class);// Assuming ownerEmail is stored
                    if (ownerEmail != null) {
                        String sanitizedOwnerEmail = sanitizeEmail(ownerEmail);
                        String notificationId = FirebaseDatabase.getInstance().getReference("notifications").child(sanitizedOwnerEmail).push().getKey();
                        String message = "Your machine '" + machineName + "' has been booked by " + userName;

                        // Create a notification object
                        Notification notification = new Notification(notificationId, machineId, "Booking Request", message, System.currentTimeMillis(), phone );

                        // Save the notification in Firebase
                        FirebaseDatabase.getInstance().getReference("notifications")
                                .child(sanitizedOwnerEmail)  // Use sanitized email as path
                                .child(notificationId)
                                .setValue(notification)
                                .addOnSuccessListener(aVoid -> Log.d("Notification", "Notification saved successfully"))
                                .addOnFailureListener(e -> Log.e("Notification Error", e.getMessage()));
                    } else {
                        Log.e("Notification Error", "Owner email not found");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Notification Error", databaseError.getMessage());
            }
        });
    }

    private String sanitizeEmail(String email) {
        return email.replace(".", "_").replace("@", "_at_");
    }
}
