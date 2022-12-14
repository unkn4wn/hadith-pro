package com.islamicproapps.hadithpro.settings;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.Preference;
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
        actionBarSymbol.setOnClickListener(view -> this.onBackPressed());

        TextView actionBarTitle = this.findViewById(R.id.actionbar_title);
        actionBarTitle.setText(getResources().getString(R.string.title_settings));

        ImageView actionBarSearch = this.findViewById(R.id.actionbar_search);
        actionBarSearch.setVisibility(View.GONE);
        ImageView actionBarSettings = this.findViewById(R.id.actionbar_settings);
        actionBarSettings.setVisibility(View.GONE);



    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(@NonNull Preference preference) {
            if (preference.getKey().equals("updates")) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/unkn4wn/hadith-pro/releases")));
            }
            return super.onPreferenceTreeClick(preference);

        }
    }

    @Override
    public void onBackPressed() {
super.onBackPressed();
    }
}