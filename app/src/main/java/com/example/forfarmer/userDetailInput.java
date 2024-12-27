package com.example.forfarmer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.forfarmer.ProfileUpdate;
import com.example.forfarmer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.ByteArrayOutputStream;

public class userDetailInput extends AppCompatActivity {

    Button btnUpdate;
    Button btnupload;
    EditText etName;
    EditText etPhone;
    EditText etLocation;
    Uri imageUri;
    Bitmap bitmapImage; // To hold the selected image
    DatabaseReference profileRef;
    static final int IMAGE_REQUEST_CODE = 101;
    ImageView profileImageView;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_detail_input);

        btnUpdate = findViewById(R.id.saveProfileButton);
        btnupload = findViewById(R.id.uploadPhotoButton);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etLocation = findViewById(R.id.etLocation);
        profileImageView = findViewById(R.id.profileImageView);

        profileRef = FirebaseDatabase.getInstance().getReference("users");

        btnUpdate.setOnClickListener(v -> uploadProfileDetail());
        btnupload.setOnClickListener(v -> openImageSelector());
    }

    private void openImageSelector() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            profileImageView.setImageURI(selectedImageUri);

            try {
                // Convert URI to Bitmap
                bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadProfileDetail() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String location = etLocation.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || location.isEmpty() || bitmapImage == null) {
            Toast.makeText(this, "Please fill all the fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert the image to Base64
        String base64Image = convertImageToBase64(bitmapImage);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = currentUser.getEmail();
        String profileId = profileRef.push().getKey();

        // Fetch FCM token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Failed to get FCM Token.", Toast.LENGTH_SHORT).show();
                        Log.e("FCM Token", "Fetching FCM token failed", task.getException());
                        return;
                    }

                    // Get the FCM token
                    String fcmToken = task.getResult();
                    Log.d("FCM Token", "Token: " + fcmToken);

                    // Create profile object with FCM token
                    ProfileUpdate profile = new ProfileUpdate(userEmail, name, phone, location, base64Image, fcmToken);

                    // Store in Firebase Realtime Database
                    profileRef.child(profileId).setValue(profile)
                            .addOnSuccessListener(unused -> Toast.makeText(this, "Profile Uploaded Successfully!", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(this, "Failed to upload profile: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                });
    }

    private String convertImageToBase64(Bitmap bitmapImage) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Compress the image (50% quality to reduce size)
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        // Convert to Base64 string
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Log.d("Base64 Image", "Base64 String: " + base64Image); // Log the Base64 string (optional)
        return base64Image;
    }
}
