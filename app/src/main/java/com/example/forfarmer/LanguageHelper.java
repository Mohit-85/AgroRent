package com.example.forfarmer;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    public static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        // Android 7.0+ ke liye locale ko set karein
        config.setLocale(locale);
        context.createConfigurationContext(config);

        // Purane versions ke liye locale set karein
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
