package com.islamicproapps.hadithpro.intro;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.islamicproapps.hadithpro.MainActivity;
import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.helper.SharedPreferencesHelper;

import java.sql.Array;

public class IntroActivity extends AppCompatActivity {
    public View view;
    private MaterialButton tvNext, tvSkip;
    private ViewPager viewPager;
    private LinearLayout layoutDots;
    private int[] layouts;
    private TextView[] dots;
    private MyViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        boolean firstStart = SharedPreferencesHelper.getValue(this, "firstStart", true);

        if (!firstStart) {
            launchHomeScreen();
            finish();
        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.black));


        tvNext = findViewById(R.id.tvNext);
        tvSkip = findViewById(R.id.tvSkip);
        viewPager = findViewById(R.id.viewPager);
        layoutDots = findViewById(R.id.layoutDots);


        layouts = new int[]{
                R.layout.intro_one,
                R.layout.intro_two,
                R.layout.intro_three
        };

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current + 2);
                } else {
                    launchHomeScreen();
                }
            }
        });

        viewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(onPageChangeListener);

        addBottomDots(0);
        changeStatusBarColor();
    }

    private void changeStatusBarColor() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                tvSkip.setVisibility(View.GONE);
                tvNext.setText("Get Started");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvNext.getLayoutParams();
                params.removeRule(RelativeLayout.ALIGN_PARENT_END);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

                MaterialSwitch materialSwitchArabic = findViewById(R.id.arabicSwitch);
                MaterialSwitch materialSwitchBengali = findViewById(R.id.bengaliSwitch);
                MaterialSwitch materialSwitchEnglish = findViewById(R.id.englishSwitch);
                MaterialSwitch materialSwitchFrench = findViewById(R.id.frenchSwitch);
                MaterialSwitch materialSwitchIndonesian = findViewById(R.id.indonesianSwitch);
                MaterialSwitch materialSwitchTamil = findViewById(R.id.tamilSwitch);
                MaterialSwitch materialSwitchTurkish = findViewById(R.id.turkishSwitch);
                MaterialSwitch materialSwitchUrdu = findViewById(R.id.urduSwitch);

                MaterialSwitch[] materialSwitches = {materialSwitchArabic, materialSwitchBengali, materialSwitchEnglish, materialSwitchFrench, materialSwitchIndonesian, materialSwitchTamil, materialSwitchTurkish, materialSwitchUrdu};
                String[] languages = {"ara", "ben", "eng", "fra", "ind", "tam", "tur", "urd"};

                materialSwitches[SharedPreferencesHelper.getValue(IntroActivity.this,"introLanguage",2)].setChecked(true);

                for (int i = 0;i<materialSwitches.length;i++) {
                    int currentPosition = i;
                    materialSwitches[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                disableAllSwitch(materialSwitches);
                                compoundButton.setChecked(true);
                                SharedPreferencesHelper.storeValue(IntroActivity.this,"language",languages[currentPosition]);
                                SharedPreferencesHelper.storeValue(IntroActivity.this,"introLanguage",currentPosition);
                            }
                        }
                    });
                }
            } else {
                tvSkip.setVisibility(View.VISIBLE);
                tvNext.setText("Next");
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvNext.getLayoutParams();
                params.removeRule(RelativeLayout.CENTER_IN_PARENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        int[] activeColors = getResources().getIntArray(R.array.active);
        int[] inActiveColors = getResources().getIntArray(R.array.inactive);
        layoutDots.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(50);
            dots[i].setTextColor(inActiveColors[currentPage]);
            layoutDots.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[currentPage].setTextColor(activeColors[currentPage]);
        }
    }


    public class MyViewPagerAdapter extends PagerAdapter {

        LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + 1;
    }

    private void launchHomeScreen() {
        SharedPreferencesHelper.storeValue(this, "firstStart", false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    private void enableSwitch(MaterialSwitch materialSwitch) {
        materialSwitch.setEnabled(true);
    }

    private void disableAllSwitch(MaterialSwitch[] materialSwitches) {
        for (MaterialSwitch materialSwitch : materialSwitches) {
            materialSwitch.setChecked(false);
        }
    }

    private void checkSwitches(MaterialSwitch[] materialSwitches) {
        for (MaterialSwitch materialSwitch : materialSwitches) {
            if (materialSwitch.isChecked()) {
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}