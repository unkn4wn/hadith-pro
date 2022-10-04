package com.islamicproapps.hadithpro.settings;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceFragmentCompat;

import com.islamicproapps.hadithpro.R;

public class SettingsActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImageView actionBarSymbol = this.findViewById(R.id.actionbar_left);
        actionBarSymbol.setImageResource(R.drawable.symbol_back);

        drawerLayout = this.findViewById(R.id.drawerLayout);
        actionBarSymbol.setOnClickListener(view -> {
         this.onBackPressed();
        });

        TextView actionBarTitle = this.findViewById(R.id.actionbar_title);
        actionBarTitle.setText("Settings");



    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public void onBackPressed() {
super.onBackPressed();
    }
}