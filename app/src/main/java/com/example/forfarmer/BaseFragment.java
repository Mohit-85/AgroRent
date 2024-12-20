package com.example.forfarmer;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Locale;

public class BaseFragment extends Fragment {


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Get selected language from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        String languageCode = preferences.getString("SelectedLanguage", "en"); // Default to English

        // Apply the selected language
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
