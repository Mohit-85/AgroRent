package com.example.forfarmer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class regitratio extends AppCompatActivity {
    Button btnRegister;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    TextView logintext;

    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regitratio);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Google Sign-In configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Firebase Web Client ID
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Views initialization
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        logintext = findViewById(R.id.logintext);

        // Email-Password Registration
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String pass = etPassword.getText().toString();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(regitratio.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(regitratio.this, home.class));
                            finish();
                        } else {
                            Toast.makeText(regitratio.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Navigate to Login screen
        logintext.setOnClickListener(view -> {
            Intent intent = new Intent(regitratio.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Google Sign-In button click handler
    public void signInWithGoogle(View view) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign-in failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(regitratio.this, home.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Google Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
