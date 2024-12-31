package com.example.forfarmer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class YourMachineFragment extends Fragment {
    private RecyclerView ownedMachinesRecyclerView;
    private ProfileMachineAdapter profileMachineAdapter;
    private List<Machine> machineList;
    private DatabaseReference machineRef;

    public YourMachineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_machine, container, false);

        // Initialize RecyclerView
        ownedMachinesRecyclerView = view.findViewById(R.id.ownedMachinesRecyclerView);
        machineList = new ArrayList<>();
        profileMachineAdapter = new ProfileMachineAdapter(getActivity(), machineList);
        ownedMachinesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ownedMachinesRecyclerView.setAdapter(profileMachineAdapter);

        // Initialize Firebase reference
        machineRef = FirebaseDatabase.getInstance().getReference("machines");

        fetchOwnedMachines();

        return view;
    }

    private void fetchOwnedMachines() {
        String ownerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        if (ownerEmail != null) {
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
    }
}
