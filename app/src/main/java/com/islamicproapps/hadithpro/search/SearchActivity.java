package com.islamicproapps.hadithpro.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.TextViewCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
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
    MaterialCardView cancelButton;
    CircularProgressIndicator circularProgressIndicator;
    TextView cancelText;

    ArrayList<String> displayName;
    public static boolean isSearchArabicText,isSearchTranslatedText, isSearchNumber;
    boolean isSearchBukhari, isSearchMuslim, isSearchNasai, isSearchAbudawud, isSearchTirmidhi, isSearchIbnmajah, isSearchMalik;
    public static boolean isSearchSahih, isSearchDaif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

         cancelButton = this.findViewById(R.id.cancel_cardView);
        cancelButton.setOnClickListener(view -> onBackPressed());
        circularProgressIndicator = this.findViewById(R.id.progress_circular);

        cancelText = this.findViewById(R.id.cancel_text);

        isSearchArabicText = true;
        isSearchTranslatedText = true;
        isSearchNumber = true;

        isSearchBukhari = true;
        isSearchMuslim = true;
        isSearchNasai = true;
        isSearchAbudawud = true;
        isSearchTirmidhi = true;
        isSearchIbnmajah = true;
        isSearchMalik = true;

        isSearchSahih = true;
        isSearchDaif = true;

        MaterialCardView filterCardView = this.findViewById(R.id.filterCardView);
        filterCardView.setOnClickListener(view -> showDialogFilter());

        RecyclerView recyclerView = this.findViewById(R.id.searchRecyclerView);

        androidx.appcompat.widget.SearchView searchView = this.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cancelText.setVisibility(View.GONE);
                circularProgressIndicator.setVisibility(View.VISIBLE);
                circularProgressIndicator.setIndeterminate(true);

                Handler handler = new Handler();
                Runnable runnable = () -> {
                    submittedText = query;
                    hadithModels = new ArrayList<>();
                    setupHadithModels();
                    HadithAdapter adapter = new HadithAdapter(SearchActivity.this, hadithModels, SearchActivity.this);
                handler.post(() -> {
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    circularProgressIndicator.setVisibility(View.GONE);
                    cancelText.setVisibility(View.VISIBLE);
                    if(hadithModels.size()==0) {
                        Snackbar.make(recyclerView,"No results found", Toast.LENGTH_SHORT).show();
                    }
                });
                };
                Thread thread = new Thread(runnable);
                thread.start();

                return false;
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
            int count = 0;
            outer:
            for (int k = 0; k < displayName.size(); k++) {


                //read the arabic hadith json and the translated json according to the language
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                String language = sharedPreferences.getString("language", "eng");
                String hadithBookArabic = IOUtils.toString(getAssets().open("ara-" + displayName.get(k).substring(0,displayName.get(k).length()-4) + "1.min.json"));
                String hadithBookEnglish = IOUtils.toString(getAssets().open(language + "-" + displayName.get(k) + ".json"));

                //convert the arabic Ahadith to an JsonArray
                JSONArray hadithsArabic = new JSONObject(hadithBookArabic).getJSONArray("hadiths");

                //convert the translated Ahadith to an JsonArray
                JSONObject hadithBookEnglishObject = new JSONObject(hadithBookEnglish);
                JSONArray hadithsEnglish = hadithBookEnglishObject.getJSONArray("hadiths");

                // get the book name in full length and set the Action Bar
                String fullBookName = hadithBookEnglishObject.getJSONObject("metadata").getString("name");


                for (int i = 0; i < hadithsArabic.length(); i++) {
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
                    boolean containsArabicOrEnglish =hadithEnglishText.contains(submittedText)||hadithArabicText.contains(submittedText);
                    if (!hadithEnglishText.isEmpty() && (isSearchTranslatedText||isSearchArabicText) && (containsArabicOrEnglish) || currentHadithNumber.contains(submittedText) && isSearchNumber) {
                        //add grades from specific hadith
                        JSONArray grades = specificHadithEnglish.getJSONArray("grades");
                        ArrayList<HadithGradesModel> hadithGradesModels = new ArrayList<>();
                        boolean foundDaif = false;
                        for (int j = 0; j < grades.length(); j++) {
                            String scholarName = grades.getJSONObject(j).getString("name");
                            String scholarGrade = grades.getJSONObject(j).getString("grade");
                            if (scholarGrade.toLowerCase().contains("daif") || scholarGrade.toLowerCase().contains("mawdu")|| scholarGrade.toLowerCase().contains("munqar") || scholarGrade.toLowerCase().contains("shadh")) {
                                foundDaif = true;
                            }
                            hadithGradesModels.add(new HadithGradesModel(scholarName, scholarGrade));
                        }
                        // only add ahadith which are translated. If there is no translation, dont show the hadith
                        if (isSearchSahih && isSearchDaif) {
                            hadithModels.add(new HadithModel(String.valueOf(++count), hadithArabicText, hadithEnglishText, referenceText.toString(), referenceBookText.toString(), language, hadithGradesModels, submittedText));
                        } else if (isSearchSahih) {
                            if (!foundDaif)
                                hadithModels.add(new HadithModel(String.valueOf(++count), hadithArabicText, hadithEnglishText, referenceText.toString(), referenceBookText.toString(), language, hadithGradesModels, submittedText));

                        } else if (isSearchDaif) {
                            if (foundDaif)
                                hadithModels.add(new HadithModel(String.valueOf(++count), hadithArabicText, hadithEnglishText, referenceText.toString(), referenceBookText.toString(), language, hadithGradesModels, submittedText));
                        }
                    }
                    // show only first 100 items
                    if (count == 200) {
                        break outer;
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

        MaterialButton buttonArabicText = dialog.findViewById(R.id.buttonArabicText);
        MaterialButton buttonTranslatedText = dialog.findViewById(R.id.buttonTranslatedText);
        MaterialButton buttonNumber = dialog.findViewById(R.id.buttonNumber);

        MaterialButton buttonBukhari = dialog.findViewById(R.id.buttonBukhari);
        MaterialButton buttonMuslim = dialog.findViewById(R.id.buttonMuslim);
        MaterialButton buttonNasai = dialog.findViewById(R.id.buttonNasai);
        MaterialButton buttonAbudawud = dialog.findViewById(R.id.buttonAbudawud);
        MaterialButton buttonTirmidhi = dialog.findViewById(R.id.buttonTirmidhi);
        MaterialButton buttonIbnmajah = dialog.findViewById(R.id.buttonIbnmajah);
        MaterialButton buttonMalik = dialog.findViewById(R.id.buttonMalik);

        MaterialButton buttonSahih = dialog.findViewById(R.id.buttonSahih);
        MaterialButton buttonDaif = dialog.findViewById(R.id.buttonDaif);

        buttonArabicText.setTextColor(isSearchArabicText ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonArabicText.setBackgroundColor(isSearchArabicText ? primaryColor : surfaceColor);
        buttonTranslatedText.setTextColor(isSearchTranslatedText ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonTranslatedText.setBackgroundColor(isSearchTranslatedText ? primaryColor : surfaceColor);
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

        buttonSahih.setTextColor(isSearchSahih ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonSahih.setBackgroundColor(isSearchSahih ? primaryColor : surfaceColor);
        buttonDaif.setTextColor(isSearchDaif ? getResources().getColor(R.color.white) : onBackgroundColor);
        buttonDaif.setBackgroundColor(isSearchDaif ? primaryColor : surfaceColor);


        buttonArabicText.setOnClickListener(view -> {
            buttonArabicText.setTextColor(isSearchArabicText ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonArabicText.setBackgroundColor(isSearchArabicText ? surfaceColor : primaryColor);
            isSearchArabicText = !isSearchArabicText;
        });

        buttonTranslatedText.setOnClickListener(view -> {
            buttonTranslatedText.setTextColor(isSearchTranslatedText ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonTranslatedText.setBackgroundColor(isSearchTranslatedText ? surfaceColor : primaryColor);
            isSearchTranslatedText = !isSearchTranslatedText;
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

        buttonSahih.setOnClickListener(view -> {
            buttonSahih.setTextColor(isSearchSahih ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonSahih.setBackgroundColor(isSearchSahih ? surfaceColor : primaryColor);
            isSearchSahih = !isSearchSahih;
        });

        buttonDaif.setOnClickListener(view -> {
            buttonDaif.setTextColor(isSearchDaif ? onBackgroundColor : getResources().getColor(R.color.white));
            buttonDaif.setBackgroundColor(isSearchDaif ? surfaceColor : primaryColor);
            isSearchDaif = !isSearchDaif;
        });

        //    dialog.setOnDismissListener(dialogInterface -> setAlarm());

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