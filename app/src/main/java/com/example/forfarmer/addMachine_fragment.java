package com.example.forfarmer;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class addMachine_fragment extends BaseFragment {

    EditText machineNameEditText, priceEditText, descriptionEditText, locationEditText;
    View selectImageButton, uploadButton;
    ImageView machineImageView;
    Uri imageUri;
    Bitmap bitmapImage; // To hold the selected image
    DatabaseReference machineRef;
    static final int IMAGE_REQUEST_CODE = 101;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_machine_fragment, container, false);

        machineNameEditText = view.findViewById(R.id.machineNameEditText);
        descriptionEditText = view.findViewById(R.id.discriptionEditText);
        priceEditText = view.findViewById(R.id.priceEditText);
        locationEditText = view.findViewById(R.id.locationEditText);
        selectImageButton = view.findViewById(R.id.selectImageButton);
        machineImageView = view.findViewById(R.id.machineImageView);
        uploadButton = view.findViewById(R.id.uploadButton);

        // Initialize Firebase reference
        machineRef = FirebaseDatabase.getInstance().getReference("machines");

        // Set onClickListeners for image selection and uploading
        selectImageButton.setOnClickListener(v -> openImageSelector());
        uploadButton.setOnClickListener(v -> uploadMachine());

        return view;
    }

    // Method to initialize and save FCM token


    // Method to upload machine details to Firebase
    private void uploadMachine() {
        String machineName = machineNameEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String location = locationEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        // Check if all fields are filled and an image is selected
        if (TextUtils.isEmpty(machineName) || TextUtils.isEmpty(price) || TextUtils.isEmpty(location) || bitmapImage == null) {
            Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the image to a Base64 string
        String base64Image = convertImageToBase64(bitmapImage);

        String machineId = machineRef.push().getKey(); // Generate unique ID for the machine
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String ownerId = currentUser.getEmail(); // or use currentUser.getEmail()

        // Retrieve FCM token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FCM_PREF", MODE_PRIVATE);
        String fcmToken = sharedPreferences.getString("fcmToken", null);

        if (fcmToken == null) {
            Toast.makeText(getContext(), "Failed to retrieve FCM token! Please ensure you are connected to the internet.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Machine object with the data
        Machine machine = new Machine(machineId, machineName, price, location, base64Image, ownerId, description, fcmToken);
        machineRef.child(machineId).setValue(machine)
                .addOnSuccessListener(unused ->
                        Toast.makeText(getContext(), "Machine Uploaded Successfully!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Failed to upload machine: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    // Method to open the image selector to pick an image
    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    // Handle image selection and display it in the ImageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            machineImageView.setImageURI(imageUri);
            machineImageView.setVisibility(View.VISIBLE);

            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed to load image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Utility method to convert a Bitmap to a Base64 string
    private String convertImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the image (50% quality to reduce size)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        // Convert to Base64 string
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
