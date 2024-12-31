package com.example.forfarmer;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList = new ArrayList<>();
    String sanitizedEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Sanitize email
        String ownerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        sanitizedEmail = ownerEmail.replace(".", "_").replace("@", "_at_");
        Log.d("NotificationFragment", "Sanitized Email: " + sanitizedEmail);

        adapter = new NotificationAdapter(notificationList, getContext(),sanitizedEmail);
        notificationRecyclerView.setAdapter(adapter);

        loadNotification();

        return view;
    }

    private void loadNotification() {


        // Use sanitized email to fetch notifications
        DatabaseReference notificationRef = FirebaseDatabase
                .getInstance()
                .getReference("notifications")
                .child(sanitizedEmail); // Using sanitized email

        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notificationList.clear();
                for(DataSnapshot notificationSnapshot : snapshot.getChildren()) {
                    Notification notification = notificationSnapshot.getValue(Notification.class);
                    notificationList.add(notification);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Notification Load Error", error.getMessage());
            }
        });
    }

}
