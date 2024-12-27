package com.example.forfarmer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class profile_fragment extends Fragment {

    private ImageView profileImageView;
    private TextView profileNameTextView, profileLocationTextView;
    private RecyclerView ownedMachinesRecyclerView;
    private Button settingsButton, contactUsButton, logoutButton;

    private DatabaseReference machineRef , userRef;
    private ProfileMachineAdapter profileMachineAdapter;
    private List<Machine> machineList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);

        // Initialize views
        profileImageView = view.findViewById(R.id.profileImageView);
        profileNameTextView = view.findViewById(R.id.profileNameTextView);
        profileLocationTextView = view.findViewById(R.id.profileLocationTextView);
        ownedMachinesRecyclerView = view.findViewById(R.id.ownedMachinesRecyclerView);
        settingsButton = view.findViewById(R.id.settingsButton);
        contactUsButton = view.findViewById(R.id.contactUsButton);
        logoutButton = view.findViewById(R.id.logoutButton);

        // logout ke liye
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut(); // Log out the user

                // Redirect to login screen or splash screen
                Intent intent = new Intent(getActivity(), MainActivity.class); // Replace LoginActivity with your login screen
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
                startActivity(intent);

                // Optional: Show a logout confirmation message
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
            }
        });


        // Initialize Firebase references
        machineRef = FirebaseDatabase.getInstance().getReference("machines");
        String profileId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Get current user's email
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();
        userRef=FirebaseDatabase.getInstance().getReference("users");

        // Initialize RecyclerView
        machineList = new ArrayList<>();
        profileMachineAdapter = new ProfileMachineAdapter(getActivity(), machineList );
        ownedMachinesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        ownedMachinesRecyclerView.setAdapter(profileMachineAdapter);

        fetchUserDetails();



        if (currentUser != null) {
            String ownerEmail = currentUser.getEmail();

            // Query machines owned by the current user
            machineRef.orderByChild("o").equalTo(ownerEmail)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            machineList.clear();
                            for (DataSnapshot data : snapshot.getChildren()) {
                                Machine machine = data.getValue(Machine.class);
                                if (machine != null) {
                                    machineList.add(machine);
                                }
                            }
                            profileMachineAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        // Settings button click listener
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),userDetailInput.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void fetchUserDetails() {

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (userEmail == null) {
            Toast.makeText(getActivity(), "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query to find the user data based on the email
        userRef.orderByChild("profileId").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String name = userSnapshot.child("name").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);
                        String location = userSnapshot.child("location").getValue(String.class);
                        String base64Image = userSnapshot.child("profileImageUrl").getValue(String.class);

                        // Display the fetched data
                        profileNameTextView.setText(name != null ? name : "No Name Available");
                        //profilePhoneTextView.setText(phone != null ? phone : "No Phone Available");
                        profileLocationTextView.setText(location != null ? location : "No Location Available");

                        if (base64Image != null && !base64Image.isEmpty()) {
                            //                            Glide.with(requireContext())
//                                    .asBitmap()
//                                    .load(decodedString)
//                                    .placeholder(R.drawable.img) // Default placeholder image
//                                    .into(profileImageView);
                            byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                           profileImageView.setImageBitmap(decodedBitmap);
                        } else {
                            profileImageView.setImageResource(R.drawable.img); // Default image
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
