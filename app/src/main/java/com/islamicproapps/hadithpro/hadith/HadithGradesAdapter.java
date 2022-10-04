package com.islamicproapps.hadithpro.hadith;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.islamicproapps.hadithpro.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class HadithGradesAdapter extends RecyclerView.Adapter<HadithGradesAdapter.NestedViewHolder> {
    private List<HadithGradesModel> hadithGradesModelList;
    private View view;

    public HadithGradesAdapter(List<HadithGradesModel> hadithGradesModelList) {
        this.hadithGradesModelList = hadithGradesModelList;
    }

    @NonNull
    @Override
    public HadithGradesAdapter.NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recylerview_hadith_grades , parent , false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HadithGradesAdapter.NestedViewHolder holder, int position) {
        holder.gradesScholar.setText(hadithGradesModelList.get(position).getHadithGradesScholar());
        holder.gradesAuthenticitiy.setText(hadithGradesModelList.get(position).getHadithGradesAuthenticity());
        String gradesAuthenticityText = holder.gradesAuthenticitiy.getText().toString();

        if(gradesAuthenticityText.contains("Daif") || gradesAuthenticityText.contains("Mawdu")) {
            TypedValue typedValue = new TypedValue();
            view.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorError, typedValue, true);
            int color = typedValue.data;
            holder.gradesAuthenticityCardView.setCardBackgroundColor(color);
        }
    }

    @Override
    public int getItemCount() {
        return hadithGradesModelList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView gradesScholar;
        private TextView gradesAuthenticitiy;
        private MaterialCardView gradesAuthenticityCardView;

        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            gradesScholar = itemView.findViewById(R.id.gradesScholar);
            gradesAuthenticitiy = itemView.findViewById(R.id.gradesAuthenticitiy);
            gradesAuthenticityCardView = itemView.findViewById(R.id.gradesAuthenticitiyCardView);
        }
    }
}
