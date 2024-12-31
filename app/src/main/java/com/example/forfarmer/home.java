package com.example.forfarmer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.forfarmer.R;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home extends BaseActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        bottomNavigationView= findViewById(R.id.bottomNavigation);

        loadFragment(new BlankFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {





           Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new BlankFragment();
            } else if (itemId == R.id.nav_add){
                selectedFragment = new addMachine_fragment();
            } else if (itemId == R.id.nav_profile){
                selectedFragment = new profile_fragment();
            }
            else if (itemId == R.id.nav_services){
                selectedFragment = new ServiecFragment();
            }

            return loadFragment( selectedFragment);
        });

    }
    private boolean loadFragment(Fragment frame) {
        if(frame!=null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.framelayout, frame)
                    .commit();
            return true;
        }
        return false;
    }

}