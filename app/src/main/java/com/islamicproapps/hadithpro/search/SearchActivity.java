package com.islamicproapps.hadithpro.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.TextViewCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.hadith.HadithAdapter;
import com.islamicproapps.hadithpro.hadith.HadithGradesModel;
import com.islamicproapps.hadithpro.hadith.HadithInterface;
import com.islamicproapps.hadithpro.hadith.HadithModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements HadithInterface {
    ArrayList<HadithModel> hadithModels;
    String submittedText = "";

    ArrayList<String> displayName;
    boolean isSearchText, isSearchNumber;
    boolean isSearchBukhari, isSearchMuslim, isSearchNasai, isSearchAbudawud, isSearchTirmidhi, isSearchIbnmajah, isSearchMalik;

    CircularProgressIndicator circularProgressIndicator;
    TextView cancelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

         cancelText = this.findViewById(R.id.cancel_text);
        MaterialCardView cancelButton = this.findViewById(R.id.cancel_search);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        isSearchText = true;
        isSearchNumber = true;

        isSearchBukhari = true;
        isSearchMuslim = true;
        isSearchNasai = true;
        isSearchAbudawud = true;
        isSearchTirmidhi = true;
        isSearchIbnmajah = true;
        isSearchMalik = true;

        MaterialCardView filterCardView = this.findViewById(R.id.filterCardView);
        filterCardView.setOnClickListener(view -> showDialogFilter());

        RecyclerView recyclerView = this.findViewById(R.id.searchRecyclerView);

        androidx.appcompat.widget.SearchView searchView = this.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hadithModels = new ArrayList<>(1000);
                setupHadithModels();
                HadithAdapter adapter = new HadithAdapter(SearchActivity.this, hadithModels, SearchActivity.this);
                if(isSearchText && isSearchNumber) {
                    adapter.getFilter().filter(query);
                } else if(isSearchText) {
                    adapter.getTextFilter().filter(query);
                } else if (isSearchNumber) {
                    adapter.getNumberFilter().filter(query);
                }
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                return false;
            }
        });



    }

    private void setupHadithModels() {
        try {
            displayName = new ArrayList<>();
            if (isSearchBukhari) displayName.add("bukhari.min");
            if (isSearchMuslim) displayName.add("muslim.min");
            if (isSearchNasai) displayName.add("nasai.min");
            if (isSearchAbudawud) displayName.add("abudawud.min");
            if (isSearchTirmidhi) displayName.add("tirmidhi.min");
            if (isSearchIbnmajah) displayName.add("ibnmajah.min");
            if (isSearchMalik) displayName.add("malik.min");
            for (int k = 0; k < displayName.size(); k++) {


                //read the arabic hadith json and the translated json according to the language
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String language = sharedPreferences.getString("language", "eng");
                String hadithBookArabic = IOUtils.toString(getAssets().open("ara-" + displayName.get(k) + ".json"));
                String hadithBookEnglish = IOUtils.toString(getAssets().open(language + "-" + displayName.get(k) + ".json"));

                //convert the arabic Ahadith to an JsonArray
                JSONArray hadithsArabic = new JSONObject(hadithBookArabic).getJSONArray("hadiths");

                //convert the translated Ahadith to an JsonArray
                JSONObject hadithBookEnglishObject = new JSONObject(hadithBookEnglish);
                JSONArray hadithsEnglish = hadithBookEnglishObject.getJSONArray("hadiths");

                // get the book name in full length and set the Action Bar
                String fullBookName = hadithBookEnglishObject.getJSONObject("metadata").getString("name");


                for (int i = 0; i < hadithsEnglish.length(); i++) {
                    //Get one specific arabic Hadith
                    JSONObject specificHadithArabic = hadithsArabic.getJSONObject(i);
                    //Get one specific translated Hadith
                    JSONObject specificHadithEnglish = hadithsEnglish.getJSONObject(i);

                    //Get reference texts from translated Json
                    JSONObject specificHadithBookReference = specificHadithEnglish.getJSONObject("reference");
                    int currentChapterNumber = specificHadithBookReference.getInt("book"); //Current chapter
                    String currentHadithNumberInChapter = specificHadithBookReference.getString("hadith"); //Current hadith inside the chapter
                    String currentHadithNumber = specificHadithEnglish.getString("hadithnumber"); //Current absolute Number of hadith

                    //Display reference text correctly
                    StringBuilder referenceText = new StringBuilder();
                    StringBuilder referenceBookText = new StringBuilder();
                    referenceText.append(fullBookName).append(" ").append(currentHadithNumber);
                    referenceBookText.append("Book ").append(currentChapterNumber).append(", ").append("Hadith ").append(currentHadithNumberInChapter);

                    String hadithArabicText = specificHadithArabic.getString("text"); //specific arabic hadith text
                    String hadithEnglishText = specificHadithEnglish.getString("text"); //specific translated hadith text

                        //add grades from specific hadith
                        JSONArray grades = specificHadithEnglish.getJSONArray("grades");
                        ArrayList<HadithGradesModel> hadithGradesModels = new ArrayList<>();
                        for (int j = 0; j < grades.length(); j++) {
                            hadithGradesModels.add(new HadithGradesModel(grades.getJSONObject(j).getString("name"), grades.getJSONObject(j).getString("grade")));
                        }
                        // only add ahadith which are translated. If there is no translation, dont show the hadith
                        if (!hadithEnglishText.isEmpty()) {
                            hadithModels.add(new HadithModel("", hadithArabicText, hadithEnglishText, referenceText.toString(), referenceBookText.toString(), language, hadithGradesModels,submittedText));
                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDialogFilter() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_filter);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        TextView titleFilter = dialog.findViewById(R.id.title_filter);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleFilter, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        TypedValue typedValuePrimary = new TypedValue();
        this.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValuePrimary, true);
        int primaryColor = typedValuePrimary.data;

        TypedValue typedValueOnBackgroundColor = new TypedValue();
        this.getTheme().resolveAttribute(com.google.android.material.R.attr.colorOnBackground, typedValueOnBackgroundColor, true);
        int onBackgroundColor = typedValueOnBackgroundColor.data;

        TypedValue typedValueSurface = new TypedValue();
        this.getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurface, typedValueSurface, true);
        int surfaceColor = typedValueSurface.data;

        MaterialButton buttonText = dialog.findViewById(R.id.buttonText);
        MaterialButton buttonNumber = dialog.findViewById(R.id.buttonNumber);

        MaterialButton buttonBukhari = dialog.findViewById(R.id.buttonBukhari);
        MaterialButton buttonMuslim = dialog.findViewById(R.id.buttonMuslim);
        MaterialButton buttonNasai = dialog.findViewById(R.id.buttonNasai);
        MaterialButton buttonAbudawud = dialog.findViewById(R.id.buttonAbudawud);
        MaterialButton buttonTirmidhi = dialog.findViewById(R.id.buttonTirmidhi);
        MaterialButton buttonIbnmajah = dialog.findViewById(R.id.buttonIbnmajah);
        MaterialButton buttonMalik = dialog.findViewById(R.id.buttonMalik);


        buttonText.setTextColor(isSearchText ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonText.setBackgroundColor(isSearchText ? primaryColor : surfaceColor);
        buttonNumber.setTextColor(isSearchNumber ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonNumber.setBackgroundColor(isSearchNumber ? primaryColor : surfaceColor);

        buttonBukhari.setTextColor(isSearchBukhari ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonBukhari.setBackgroundColor(isSearchBukhari ? primaryColor : surfaceColor);
        buttonMuslim.setTextColor(isSearchMuslim ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonMuslim.setBackgroundColor(isSearchMuslim ? primaryColor : surfaceColor);
        buttonNasai.setTextColor(isSearchNasai ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonNasai.setBackgroundColor(isSearchNasai ? primaryColor : surfaceColor);
        buttonAbudawud.setTextColor(isSearchAbudawud ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonAbudawud.setBackgroundColor(isSearchAbudawud ? primaryColor : surfaceColor);
        buttonTirmidhi.setTextColor(isSearchTirmidhi ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonTirmidhi.setBackgroundColor(isSearchTirmidhi ? primaryColor : surfaceColor);
        buttonIbnmajah.setTextColor(isSearchIbnmajah ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonIbnmajah.setBackgroundColor(isSearchIbnmajah ? primaryColor : surfaceColor);
        buttonMalik.setTextColor(isSearchMalik ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonMalik.setBackgroundColor(isSearchMalik ? primaryColor : surfaceColor);


        buttonText.setOnClickListener(view -> {
            buttonText.setTextColor(isSearchText ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonText.setBackgroundColor(isSearchText ? surfaceColor : primaryColor);
            isSearchText = !isSearchText;
        });

        buttonNumber.setOnClickListener(view -> {
            buttonNumber.setTextColor(isSearchNumber ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonNumber.setBackgroundColor(isSearchNumber ? surfaceColor : primaryColor);
            isSearchNumber = !isSearchNumber;
        });

        buttonBukhari.setOnClickListener(view -> {
            buttonBukhari.setTextColor(isSearchBukhari ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonBukhari.setBackgroundColor(isSearchBukhari ? surfaceColor : primaryColor);
            isSearchBukhari = !isSearchBukhari;
        });

        buttonMuslim.setOnClickListener(view -> {
            buttonMuslim.setTextColor(isSearchMuslim ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonMuslim.setBackgroundColor(isSearchMuslim ? surfaceColor : primaryColor);
            isSearchMuslim = !isSearchMuslim;
        });

        buttonNasai.setOnClickListener(view -> {
            buttonNasai.setTextColor(isSearchNasai ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonNasai.setBackgroundColor(isSearchNasai ? surfaceColor : primaryColor);
            isSearchNasai = !isSearchNasai;
        });

        buttonAbudawud.setOnClickListener(view -> {
            buttonAbudawud.setTextColor(isSearchAbudawud ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonAbudawud.setBackgroundColor(isSearchAbudawud ? surfaceColor : primaryColor);
            isSearchAbudawud = !isSearchAbudawud;
        });

        buttonTirmidhi.setOnClickListener(view -> {
            buttonTirmidhi.setTextColor(isSearchTirmidhi ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonTirmidhi.setBackgroundColor(isSearchTirmidhi ? surfaceColor : primaryColor);
            isSearchTirmidhi = !isSearchTirmidhi;
        });

        buttonIbnmajah.setOnClickListener(view -> {
            buttonIbnmajah.setTextColor(isSearchIbnmajah ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonIbnmajah.setBackgroundColor(isSearchIbnmajah ? surfaceColor : primaryColor);
            isSearchIbnmajah = !isSearchIbnmajah;
        });

        buttonMalik.setOnClickListener(view -> {
            buttonMalik.setTextColor(isSearchMalik ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonMalik.setBackgroundColor(isSearchMalik ? surfaceColor : primaryColor);
            isSearchMalik = !isSearchMalik;
        });

            dialog.setOnDismissListener((DialogInterface dialogInterface) -> {
                //setAlarm();
            });

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onItemClick(int position) {

    }
}