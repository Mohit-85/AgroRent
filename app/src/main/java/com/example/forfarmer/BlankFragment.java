package com.example.forfarmer;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BlankFragment extends BaseFragment {

    RecyclerView recyclerView;
    MachineAdapter machineAdapter;
    List<Machine> machineList;
    List<Machine> filteredMachineList;
    DatabaseReference machineRef , userRef;
    SearchView searchView;
    TextView profileNameTextView;



    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        // Initialize views
        recyclerView = view.findViewById(R.id.machineRecyclerView);
        searchView = view.findViewById(R.id.searchView);
        profileNameTextView  = view.findViewById(R.id.welcomeText);

        // Set GridLayoutManager for 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        machineList = new ArrayList<>();
        filteredMachineList = new ArrayList<>();

        machineRef = FirebaseDatabase.getInstance().getReference("machines");
        userRef = FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();
        fetchUserDetail();
        // Fetch machine data from Firebase
        fetchMachineData();

        // Setup search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterMachines(newText);
                return true;
            }
        });

        return view;
    }

    private void fetchUserDetail() {
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
                        // Display the fetched data
                        profileNameTextView.setText(name != null ? "Hi "+name : "Hi Karim! \uD83D\uDC4B");
                        //profilePhoneTextView.setText(phone != null ? phone : "No Phone Available");
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

    private void fetchMachineData() {
        machineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                machineList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Machine machine = dataSnapshot.getValue(Machine.class);
                    machineList.add(machine);
                }

                // Initially, show all machines
                filteredMachineList.clear();
                filteredMachineList.addAll(machineList);
                machineAdapter = new MachineAdapter(getContext(), filteredMachineList, machine -> {
                    openBookingFragment(machine);
                });

                recyclerView.setAdapter(machineAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load machines: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMachines(String query) {
        filteredMachineList.clear();
        if (query.isEmpty()) {
            filteredMachineList.addAll(machineList);
        } else {
            for (Machine machine : machineList) {
                if (machine.getMachineName().toLowerCase().contains(query.toLowerCase()) ||
                        machine.getLocation().toLowerCase().contains(query.toLowerCase())) {
                    filteredMachineList.add(machine);
                }
            }
        }

        machineAdapter.notifyDataSetChanged();
    }

    private void openBookingFragment(Machine selectedMachine) {
        if (selectedMachine == null) {
            Toast.makeText(getContext(), "No machine selected!", Toast.LENGTH_SHORT).show();
            return;
        }

        Fragment bookingFragment = new Bookin_machines();
        Bundle args = new Bundle();
        args.putString("machineId", selectedMachine.getMachineId());
        args.putString("machineName", selectedMachine.getMachineName());
        args.putString("machinePrice", selectedMachine.getPrice());
        args.putString("machineLocation", selectedMachine.getLocation());
        args.putString("machineImageUrl", selectedMachine.getImageUrl());
        bookingFragment.setArguments(args);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, bookingFragment)
                .addToBackStack(null)
                .commit();
    }
}
