package com.islamicproapps.hadithpro.book;

import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.section.SectionFragment;

import java.util.ArrayList;
import java.util.Locale;


public class BookFragment extends Fragment implements BookInterface {
    ArrayList<BookModel> bookModels;
    public BookFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_book, container, false);



        bookModels = new ArrayList<>();

        RecyclerView recyclerView = mView.findViewById(R.id.mRecyclerView2);
        setupHadithModels();

        BookAdapter adapter = new BookAdapter(requireContext(), bookModels,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        return mView;
    }

    private void setupHadithModels() {
        ArrayList<String> bookName = new ArrayList<>();
        bookName.add(getResources().getString(R.string.bukhari_book));
        bookName.add(getResources().getString(R.string.muslim_book));
        bookName.add(getResources().getString(R.string.nasai_book));
        bookName.add(getResources().getString(R.string.abudawud_book));
        bookName.add(getResources().getString(R.string.tirmidhi_book));
        bookName.add(getResources().getString(R.string.ibnmajah_book));
        bookName.add(getResources().getString(R.string.malik_book));
        System.out.println(Locale.getDefault().getISO3Language());


        ArrayList<String> displayName = new ArrayList<>();
        displayName.add("bukhari.min");
        displayName.add("muslim.min");
        displayName.add("nasai.min");
        displayName.add("abudawud.min");
        displayName.add("tirmidhi.min");
        displayName.add("ibnmajah.min");
        displayName.add("malik.min");

        ArrayList<String> bookScholar = new ArrayList<>();
        bookScholar.add(getResources().getString(R.string.bukhari_author));
        bookScholar.add(getResources().getString(R.string.muslim_author));
        bookScholar.add(getResources().getString(R.string.nasai_author));
        bookScholar.add(getResources().getString(R.string.abudawud_author));
        bookScholar.add(getResources().getString(R.string.tirmidhi_author));
        bookScholar.add(getResources().getString(R.string.ibnmajah_author));
        bookScholar.add(getResources().getString(R.string.malik_author));

        ArrayList<Integer> bookIconColor = new ArrayList<>();
        bookIconColor.add(getResources().getColor(R.color.bukhariBook));
        bookIconColor.add(getResources().getColor(R.color.muslimBook));
        bookIconColor.add(getResources().getColor(R.color.nasaiBook));
        bookIconColor.add(getResources().getColor(R.color.abudawudBook));
        bookIconColor.add(getResources().getColor(R.color.tirmidhiBook));
        bookIconColor.add(getResources().getColor(R.color.ibnmajahBook));
        bookIconColor.add(getResources().getColor(R.color.malikBook));

        ArrayList<String> bookIconText = new ArrayList<>();
        bookIconText.add("B");
        bookIconText.add("M");
        bookIconText.add("N");
        bookIconText.add("D");
        bookIconText.add("T");
        bookIconText.add("M");
        bookIconText.add("M");

        for (int i = 0;i<bookName.size();i++) {
            bookModels.add(new BookModel(bookName.get(i),displayName.get(i),"By: " + bookScholar.get(i),bookIconColor.get(i),bookIconText.get(i)));
        }
    }

    @Override
    public void onItemClick(int position) {
        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.fragment, SectionFragment.newInstance(bookModels.get(position).getDisplayName()), "NewFragmentTag");
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
         ImageView actionBarMenu = requireActivity().findViewById(R.id.actionbar_left);
        actionBarMenu.setImageResource(R.drawable.symbol_menu3);
        DrawerLayout drawerLayout = requireActivity().findViewById(R.id.drawerLayout);
        actionBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        TextView actionBarTitle = requireActivity().findViewById(R.id.actionbar_title);
            actionBarTitle.setText(getResources().getString(R.string.title_book));

    }
}