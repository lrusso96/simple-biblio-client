package com.example.biblio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.example.biblio.fragments.MyBooksFragment;
import com.example.biblio.fragments.PopularFragment;
import com.example.biblio.fragments.RecentFragment;
import com.example.biblio.fragments.SearchFragment;
import com.example.biblio.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean isFirstStart = sharedPrefs.getBoolean("firstStart", true);

        if (isFirstStart) {
            //Launch introduction activity
            Intent i = new Intent(MainActivity.this, Introduction.class);

            runOnUiThread(() -> startActivity(i));

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), "SearchFragment").commit();
        } else {
            if (savedInstanceState == null)
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SearchFragment(), "SearchFragment").commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                String TAG = "";

                switch (item.getItemId()) {
                    case R.id.nav_search:
                        selectedFragment = new SearchFragment();
                        TAG = "SearchFragment";
                        break;
                    case R.id.nav_popular:
                        selectedFragment = new PopularFragment();
                        TAG = "PopularFragment";
                        break;
                    case R.id.nav_recent:
                        selectedFragment = new RecentFragment();
                        TAG = "RecentFragment";
                        break;
                    case R.id.nav_books:
                        selectedFragment = new MyBooksFragment();
                        TAG = "MyBooksFragment";
                        break;
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                        TAG = "SettingsFragment";
                        break;
                }

                if (selectedFragment != null) {
                    Fragment previous_fragment = getSupportFragmentManager().findFragmentByTag(TAG);
                    if (previous_fragment != null && previous_fragment.isVisible()) {
                        return false;

                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment, TAG).commit();
                        return true;
                    }
                }
                return false;
            };
}
