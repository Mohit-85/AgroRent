package com.example.forfarmer;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.forfarmer.Fragment.BlankFragment;
import com.example.forfarmer.Fragment.ServiecFragment;
import com.example.forfarmer.Fragment.addMachine_fragment;

import androidx.fragment.app.Fragment;

import com.example.forfarmer.Fragment.profile_fragment;
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