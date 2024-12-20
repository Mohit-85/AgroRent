package com.example.forfarmer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Bookin_machines extends BaseFragment {

   public ImageView machineImageView;
    public EditText usernameEditText , userContectEditText , startDateEditText , endtDateEditText ;
    public  TextView machineNameTextView, machinePriceTextView, machineLocationTextView;
    public  Button bookButton;
    public  DatabaseReference bookingsRef;
    public  String machineId, machineName, machinePrice, machineLocation, machineImageUrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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


            // Load the image using Glide
//            Glide.with(this)
//                    .load(machineImageUrl)
//                    .placeholder(R.drawable.img) // Optional placeholder
//                    .into(machineImageView);
        }

        // Handle booking
        bookButton.setOnClickListener(v -> bookMachine());

        return view;
    }
    private void bookMachine() {
        String userName = usernameEditText.getText().toString();
        String userContact = userContectEditText.getText().toString();
        String startDate = startDateEditText.getText().toString();
        String endDate = endtDateEditText.getText().toString();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userContact) || TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
            Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        String bookingId = bookingsRef.push().getKey();
        Booking booking = new Booking(bookingId, machineId, userName, userContact, startDate, endDate, "Pending");

        bookingsRef.child(bookingId).setValue(booking)
                .addOnSuccessListener(unused -> Toast.makeText(getContext(), "Booking Successful!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to book: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}