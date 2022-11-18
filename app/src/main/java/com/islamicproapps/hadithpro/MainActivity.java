package com.islamicproapps.hadithpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.islamicproapps.hadithpro.book.BookFragment;
import com.islamicproapps.hadithpro.bookmark.BookmarkFragment;
import com.islamicproapps.hadithpro.helper.SharedPreferencesHelper;
import com.islamicproapps.hadithpro.intro.IntroActivity;
import com.islamicproapps.hadithpro.random.RandomFragment;
import com.islamicproapps.hadithpro.search.SearchActivity;
import com.islamicproapps.hadithpro.settings.SettingsActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean firstStart = SharedPreferencesHelper.getValue(this,"firstStart",true);
        if (firstStart) {
            Log.d("strt", String.valueOf(firstStart));
            startActivity(new Intent(MainActivity.this,IntroActivity.class));
        } else {
            Log.d("strt", String.valueOf(firstStart));
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, new BookFragment()); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();

        drawerLayout = findViewById(R.id.drawerLayout);

        ImageView actionBarMenu = this.findViewById(R.id.actionbar_left);
        actionBarMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.book) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.fragment, new BookFragment()); // give your fragment container id in first parameter
                transaction1.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction1.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (item.getItemId() == R.id.bookmark) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.fragment, new BookmarkFragment()); // give your fragment container id in first parameter
                transaction1.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction1.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (item.getItemId() == R.id.random) {
                FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                transaction1.replace(R.id.fragment, new RandomFragment()); // give your fragment container id in first parameter
                transaction1.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction1.commit();
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (item.getItemId() == R.id.rate) {

                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                      startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            } else if (item.getItemId() == R.id.reference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fawazahmed0/hadith-api/blob/1/References.md"));
                startActivity(browserIntent);
            } else if (item.getItemId() == R.id.moreapps) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://dev?id=7498034169100742685")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7498034169100742685")));
                }
            } else if (item.getItemId() == R.id.contribute) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/fawazahmed0/hadith-api#contribution")));
            } else if (item.getItemId() == R.id.sourcecode) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/unkn4wn/hadith-pro")));
            }

            return false;
        });

        ImageView actionBarSearch = this.findViewById(R.id.actionbar_search);
        actionBarSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        ImageView actionbarSettings = this.findViewById(R.id.actionbar_settings);
        actionbarSettings.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent); //, ActivityOptions.makeCustomAnimation(this,android.R.anim.slide_in_left,android.R.anim.slide_out_right).toBundle()
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }
}