package com.islamicproapps.hadithpro.random;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.book.BookAdapter;
import com.islamicproapps.hadithpro.hadith.HadithAdapter;
import com.islamicproapps.hadithpro.hadith.HadithGradesModel;
import com.islamicproapps.hadithpro.hadith.HadithInterface;
import com.islamicproapps.hadithpro.hadith.HadithModel;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class RandomFragment extends Fragment implements HadithInterface {
    ArrayList<HadithModel> hadithModels;

    String[] displayName;
    String hadithBookArabic,hadithBookEnglish;
    String language;

    public RandomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        displayName = new String[7];
        displayName[0] = ("bukhari.min");
        displayName[1] = ("muslim.min");
        displayName[2] = ("nasai.min");
        displayName[3] = ("abudawud.min");
        displayName[4] = ("tirmidhi.min");
        displayName[5] = ("ibnmajah.min");
        displayName[6] = ("malik.min");

        int randomBook = new Random().nextInt(7);



        //read the arabic hadith json and the translated json according to the language
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
         language = sharedPreferences.getString("language", "eng");
        try {
             hadithBookArabic = IOUtils.toString(requireContext().getAssets().open("ara-" + displayName[randomBook] + ".json"));
             hadithBookEnglish = IOUtils.toString(requireContext().getAssets().open(language + "-" + displayName[randomBook] + ".json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_random, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.randomRecyclerView);
        hadithModels= new ArrayList<>();
        setupHadithModels();

        HadithAdapter adapter = new HadithAdapter(requireContext(), hadithModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }


    private void setupHadithModels() {
        try {
                //convert the arabic Ahadith to an JsonArray
                JSONArray hadithsArabic = new JSONObject(hadithBookArabic).getJSONArray("hadiths");

                //convert the translated Ahadith to an JsonArray
                JSONObject hadithBookEnglishObject = new JSONObject(hadithBookEnglish);
                JSONArray hadithsEnglish = hadithBookEnglishObject.getJSONArray("hadiths");

                // get the book name in full length and set the Action Bar
                String fullBookName = hadithBookEnglishObject.getJSONObject("metadata").getString("name");

                int randomHadithNumber = new Random().nextInt(hadithsEnglish.length());

                //Get one specific arabic Hadith
                JSONObject specificHadithArabic = hadithsArabic.getJSONObject(randomHadithNumber);
                //Get one specific translated Hadith
                JSONObject specificHadithEnglish = hadithsEnglish.getJSONObject(randomHadithNumber);

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

                if (!hadithEnglishText.isEmpty()) {
                    //add grades from specific hadith
                    JSONArray grades = specificHadithEnglish.getJSONArray("grades");
                    ArrayList<HadithGradesModel> hadithGradesModels = new ArrayList<>();
                    for (int j = 0; j < grades.length(); j++) {
                        String scholarName = grades.getJSONObject(j).getString("name");
                        String scholarGrade = grades.getJSONObject(j).getString("grade");
                        hadithGradesModels.add(new HadithGradesModel(scholarName, scholarGrade));
                    }
                    // only add ahadith which are translated. If there is no translation, dont show the hadith
                    hadithModels.add(new HadithModel(String.valueOf(1), hadithArabicText, hadithEnglishText, referenceText.toString(), referenceBookText.toString(), language, hadithGradesModels));
                }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView actionBarMenu = requireActivity().findViewById(R.id.actionbar_left);
        actionBarMenu.setImageResource(R.drawable.symbol_menu3);
        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
        actionBarMenu.setOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));

        TextView actionBarTitle = requireActivity().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(getResources().getString(R.string.title_random));
    }
}