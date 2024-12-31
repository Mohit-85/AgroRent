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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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

    private DatabaseReference machineRef, userRef;
    private ProfileMachineAdapter profileMachineAdapter;
    private List<Machine> machineList;


    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FragmentStateAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);

        // Initialize views
        profileImageView = view.findViewById(R.id.profileImageView);
        profileNameTextView = view.findViewById(R.id.profileNameTextView);
        profileLocationTextView = view.findViewById(R.id.profileLocationTextView);
//        logoutButton = view.findViewById(R.id.logoutButton);

        // TabLayout and ViewPager2
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        // Initialize Firebase references
        userRef = FirebaseDatabase.getInstance().getReference("users");

        // Setup ViewPager and TabLayout
        pagerAdapter = new FragmentStateAdapter(getActivity()) {
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new YourMachineFragment();  // "Your Machine" fragment
                    case 1:
                        return new RentalHistoryFragment();  // "Rental History" fragment
                    default:
                        return new YourMachineFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 2;  // We have 2 tabs
            }
        };

        viewPager.setAdapter(pagerAdapter);

        // Connect the TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setIcon(R.drawable.baseline_photo_24);
                    break;
                case 1:
                    tab.setIcon(R.drawable.baseline_history_24);
                    break;
            }
        }).attach();

        // logout functionality
//        logoutButton.setOnClickListener(view1 -> {
//            FirebaseAuth.getInstance().signOut(); // Log out the user
//            Intent intent = new Intent(getActivity(), MainActivity.class); // Redirect to login screen
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
//            startActivity(intent);
//            Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();
//        });

        fetchUserDetails();

        return view;
    }

    private void fetchUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            userRef.orderByChild("profileId").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                            String name = userSnapshot.child("name").getValue(String.class);
                            String location = userSnapshot.child("location").getValue(String.class);
                            String base64Image = userSnapshot.child("profileImageUrl").getValue(String.class);

                            profileNameTextView.setText(name != null ? name : "No Name Available");
                            profileLocationTextView.setText(location != null ? location : "No Location Available");

                            if (base64Image != null && !base64Image.isEmpty()) {
                                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
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
}
