package com.com.technoparkproject.view.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.com.technoparkproject.R;
import com.com.technoparkproject.view.fragments.HomeFragment;
import com.com.technoparkproject.view.fragments.PersonalPageFragment;
import com.com.technoparkproject.view.fragments.PlaylistFragment;
import com.com.technoparkproject.view.fragments.RecordFragment;
import com.com.technoparkproject.view.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private String currentFragment = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.currentFragment = null;
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(navigationListener);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            String nameSelectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    nameSelectedFragment = "home";
                    break;
                case R.id.nav_playlist:
                    selectedFragment = new PlaylistFragment();
                    nameSelectedFragment = "playlist";
                    break;
                case R.id.nav_record:
                    selectedFragment = new RecordFragment();
                    nameSelectedFragment = "record";
                    break;
                case R.id.nav_settings:
                    selectedFragment = new SettingsFragment();
                    nameSelectedFragment = "settings";
                    break;
                case R.id.nav_personal_page:
                    selectedFragment = new PersonalPageFragment();
                    nameSelectedFragment = "personal_page";
                    break;
            }

            if (currentFragment != nameSelectedFragment) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                currentFragment = nameSelectedFragment;
            }
            return true;
        }
    };
}
