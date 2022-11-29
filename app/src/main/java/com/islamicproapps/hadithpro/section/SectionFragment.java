package com.islamicproapps.hadithpro.section;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.hadith.HadithFragment;
import com.islamicproapps.hadithpro.helper.SharedPreferencesHelper;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.util.ArrayList;


public class SectionFragment extends Fragment implements SectionInterface {
    ArrayList<SectionModel> sectionModels;
    String bookId = "";
    TextView actionBarTitle;
    androidx.appcompat.widget.SearchView searchView;

    public static SectionFragment newInstance(String bookId) {
        SectionFragment fragment = new SectionFragment();
        Bundle args = new Bundle();
        args.putString("BookId", bookId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookId = getArguments().getString("BookId");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_section, container, false);

        ImageView actionBarSymbol = requireActivity().findViewById(R.id.actionbar_left);
        actionBarSymbol.setImageResource(R.drawable.symbol_back);
        actionBarSymbol.setOnClickListener(view -> requireActivity().onBackPressed());

        actionBarTitle = requireActivity().findViewById(R.id.actionbar_title);

        sectionModels = new ArrayList<>();
        RecyclerView recyclerView = mView.findViewById(R.id.sectionRecyclerView);
        setupSectionModels();

        SectionAdapter adapter = new SectionAdapter(requireContext(), sectionModels, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        searchView = mView.findViewById(R.id.searchView);
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });

        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return mView;
    }

    private void setupSectionModels() {
        try {
            String language = SharedPreferencesHelper.getValue(requireContext(),"language","eng");
            String everything = IOUtils.toString(requireActivity().getAssets().open(language + "-" + bookId + ".json"));
            JSONObject hadithBookObject = new JSONObject(everything);
            JSONObject metadata = hadithBookObject.getJSONObject("metadata");
            String bookName = metadata.getString("name");
            actionBarTitle.setText(bookName);

            JSONObject section = metadata.getJSONObject("sections");

            for (int i = 0; i < section.length(); i++) {
                String[] sectionname = new String[i + 1];
                sectionname[i] = section.getString(String.valueOf(i));

                if (!sectionname[i].isEmpty()) {
                    sectionModels.add(new SectionModel(i, sectionname[i]));
                }
            }
        } catch (Exception e) {
            sectionModels.add(new SectionModel(404,getString(R.string.no_translation)));
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(int position) {
        if(sectionModels.get(position).getSectionNumber()!= 404) {
            final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.fragment, HadithFragment.newInstance(bookId, sectionModels.get(position).getSectionNumber(), sectionModels.get(position).getSectionName()), "NewFragmentTag");
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.clearFocus();
        searchView.setQuery("", false); // clear the text
    }
}