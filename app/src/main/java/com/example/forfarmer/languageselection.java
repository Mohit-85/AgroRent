package com.example.forfarmer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class languageselection extends AppCompatActivity {

    Button btnEnglish , btnHindi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_languageselection);

        btnEnglish=findViewById(R.id.btnEnglish);
        btnHindi=findViewById(R.id.btnHindi);

        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        if (preferences.contains("SelectedLanguage")) {
            redirectToLogin();
            return;
        }
        btnEnglish.setOnClickListener(view -> setLanguage("en"));
        btnHindi.setOnClickListener(view -> setLanguage("hi"));

    }
    private void setLanguage(String languageCode) {
        // Save selected language in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("AppPreferences", MODE_PRIVATE).edit();
        editor.putString("SelectedLanguage", languageCode);
        editor.apply();

        // Apply selected language
        LanguageHelper.setLocale(this, languageCode);

        // Redirect to Login page
        redirectToLogin();
    }
    private void redirectToLogin() {
        Intent intent = new Intent(languageselection.this,regitratio.class);
        startActivity(intent);
        finish();
    }
}