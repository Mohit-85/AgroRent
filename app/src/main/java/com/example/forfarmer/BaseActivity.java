package com.example.forfarmer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent's onCreate method
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String languageCode = preferences.getString("SelectedLanguage", "en"); // Default to English

        // Apply selected language
        LanguageHelper.setLocale(this, languageCode);  }
}

