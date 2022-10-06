package com.islamicproapps.hadithpro.bookmark;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.hadith.HadithAdapter;
import com.islamicproapps.hadithpro.hadith.HadithGradesModel;
import com.islamicproapps.hadithpro.hadith.HadithInterface;
import com.islamicproapps.hadithpro.hadith.HadithModel;
import com.islamicproapps.hadithpro.helper.MyDatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment implements HadithInterface {
    ArrayList<HadithModel> hadithModels;

    ArrayList<String> hadith_id, hadith_arabic, hadith_translated, hadith_gradeScholars, hadith_grades, hadith_reference, hadith_bookreference,hadith_language;
    MyDatabaseHelper myDatabaseHelper;

    public BookmarkFragment() {
        // Required empty public constructor
    }


    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_bookmark, container, false);

        TextView actionBarTitle = requireActivity().findViewById(R.id.actionbar_title);
        actionBarTitle.setText(getResources().getString(R.string.title_bookmark));

        hadith_id = new ArrayList<>();
        hadith_arabic = new ArrayList<>();
        hadith_translated = new ArrayList<>();
        hadith_gradeScholars = new ArrayList<>();
        hadith_grades = new ArrayList<>();
        hadith_reference = new ArrayList<>();
        hadith_bookreference = new ArrayList<>();
        hadith_language = new ArrayList<>();
        myDatabaseHelper = new MyDatabaseHelper(requireContext(),mView);

        displayHadithData();

        hadithModels = new ArrayList<>();
        RecyclerView recyclerView = mView.findViewById(R.id.bookmarkRecyclerView);
        setupHadithModels();

        HadithAdapter adapter = new HadithAdapter(requireContext(), hadithModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return mView;
    }

    private void setupHadithModels() {
        System.out.println(hadith_arabic.size());
        for (int i = 0; i < hadith_arabic.size(); i++) {
            displayHadithGradesData(hadith_reference.get(i),hadith_language.get(i));
            ArrayList<HadithGradesModel> hadithGradesModels = new ArrayList<>();
            for (int j = 0; j < hadith_gradeScholars.size(); j++) {
                hadithGradesModels.add(new HadithGradesModel(hadith_gradeScholars.get(j), hadith_grades.get(j)));
            }
            hadithModels.add(new HadithModel(String.valueOf(String.valueOf(i)), hadith_arabic.get(i), hadith_translated.get(i), hadith_reference.get(i), hadith_bookreference.get(i), hadith_language.get(i),hadithGradesModels));
            hadith_gradeScholars = new ArrayList<>();
            hadith_grades = new ArrayList<>();
        }
    }

    private void displayHadithData() {
        Cursor cursor = myDatabaseHelper.readHadithData();
        if (cursor.getCount() == 0) {
            //   Toast.makeText(requireContext(), "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                hadith_id.add(cursor.getString(0));
                hadith_arabic.add(cursor.getString(1));
                hadith_translated.add(cursor.getString(2));
                hadith_reference.add(cursor.getString(3));
                hadith_bookreference.add(cursor.getString(4));
                hadith_language.add(cursor.getString(5));
            }
        }
    }

    private void displayHadithGradesData(String reference,String language) {
        Cursor cursor = myDatabaseHelper.readHadithGradesData(reference,language);
        if (cursor.getCount() == 0) {
            //  Toast.makeText(requireContext(), "no data", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                hadith_reference.add(cursor.getString(0));
                hadith_gradeScholars.add(cursor.getString(1));
                hadith_grades.add(cursor.getString(2));
                hadith_language.add(cursor.getString(3));
            }
        }
    }

    @Override
    public void onItemClick(int position) {

    }
}