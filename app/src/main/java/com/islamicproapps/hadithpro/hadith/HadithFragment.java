package com.islamicproapps.hadithpro.hadith;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islamicproapps.hadithpro.R;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class HadithFragment extends Fragment implements HadithInterface {
    ArrayList<HadithModel> hadithModels;
    String bookId = "";
    int sectionId = 0;
    String sectionIdName = "";
    TextView actionBarTitle;
    TextView sectionNumber, sectionText, sectionName;

    RecyclerView recyclerView;
    HadithAdapter adapter;
    SharedPreferences sharedPreferences;


    public static HadithFragment newInstance(String bookId, int sectionId, String sectionIdName) {
        HadithFragment fragment = new HadithFragment();
        Bundle args = new Bundle();
        args.putString("BookId", bookId);
        args.putInt("SectionId", sectionId);
        args.putString("SectionIdName", sectionIdName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookId = getArguments().getString("BookId");
        sectionId = getArguments().getInt("SectionId");
        sectionIdName = getArguments().getString("SectionIdName");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView = inflater.inflate(R.layout.fragment_hadith, container, false);

        ImageView actionBarSymbol = requireActivity().findViewById(R.id.actionbar_left);
        actionBarSymbol.setImageResource(R.drawable.symbol_back);
        actionBarSymbol.setOnClickListener(view -> requireActivity().onBackPressed());

        actionBarTitle = requireActivity().findViewById(R.id.actionbar_title);

        sectionNumber = mView.findViewById(R.id.sectionNumber);
        sectionText = mView.findViewById(R.id.sectionText);
        sectionName = mView.findViewById(R.id.sectionName);

         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        hadithModels = new ArrayList<>();
        recyclerView = mView.findViewById(R.id.hadithRecyclerView);
        setupHadithModels();

         adapter = new HadithAdapter(requireContext(), hadithModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inflate the layout for this fragment
        return mView;
    }

    private void setupHadithModels() {
        try {
            //read the arabic hadith json and the translated json according to the language
            String language = sharedPreferences.getString("language", "eng");
            String hadithBookArabic = IOUtils.toString(requireActivity().getAssets().open("ara-" + bookId + ".json"));
            String hadithBookEnglish = IOUtils.toString(requireActivity().getAssets().open(language + "-" + bookId + ".json"));

            //convert the arabic Ahadith to an JsonArray
            JSONArray hadithsArabic = new JSONObject(hadithBookArabic).getJSONArray("hadiths");

            //convert the translated Ahadith to an JsonArray
            JSONObject hadithBookEnglishObject = new JSONObject(hadithBookEnglish);
            JSONArray hadithsEnglish = hadithBookEnglishObject.getJSONArray("hadiths");

            // get the book name in full length and set the Action Bar
            String fullBookName = hadithBookEnglishObject.getJSONObject("metadata").getString("name");
            actionBarTitle.setText(fullBookName);

            //get amount of chapters
            int amountOfChapters = hadithBookEnglishObject.getJSONObject("metadata").getJSONObject("sections").length();

            sectionNumber.setText(sectionId + "/" + amountOfChapters);
            sectionText.setText("Chapter " + sectionId);
            sectionName.setText(sectionIdName);

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

                if (currentChapterNumber == sectionId) {
                    String hadithArabicText = specificHadithArabic.getString("text"); //specific arabic hadith text
                    String hadithEnglishText = specificHadithEnglish.getString("text"); //specific translated hadith text

                    // only add ahadith which are translated. If there is no translation, dont show the hadith
                    if (!hadithEnglishText.isEmpty()) {

                        //add grades from specific hadith
                        JSONArray grades = specificHadithEnglish.getJSONArray("grades");
                        ArrayList<HadithGradesModel> hadithGradesModels = new ArrayList<>();
                        for (int j = 0; j < grades.length(); j++) {
                            hadithGradesModels.add(new HadithGradesModel(grades.getJSONObject(j).getString("name"), grades.getJSONObject(j).getString("grade")));
                        }

                        hadithModels.add(new HadithModel(specificHadithBookReference.getString("hadith"), hadithArabicText, hadithEnglishText, referenceText.toString(), referenceBookText.toString(),language, hadithGradesModels));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        //  Toast.makeText(getApplicationContext(),hadithModels.get(position).getHadithGrades(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}