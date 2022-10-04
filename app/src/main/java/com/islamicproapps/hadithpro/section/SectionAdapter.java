package com.islamicproapps.hadithpro.section;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.islamicproapps.hadithpro.R;

import java.util.ArrayList;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.MyViewHolder> implements Filterable {
    private  final SectionInterface sectionInterface;

    Context context;
    ArrayList<SectionModel> sectionModels;
    ArrayList<SectionModel> sectionModelsFull;

    public SectionAdapter(Context context,  ArrayList<SectionModel> sectionModels,SectionInterface sectionInterface) {
        this.context = context;
        this.sectionModels = sectionModels;
        this.sectionInterface = sectionInterface;
        sectionModelsFull = new ArrayList<>(sectionModels);
    }

    @NonNull
    @Override
    public SectionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_section,parent,false);
        return new SectionAdapter.MyViewHolder(view,sectionInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter.MyViewHolder holder, int position) {
        holder.sectionName.setText(sectionModels.get(position).getSectionName());
        holder.sectionNumber.setText(String.valueOf(sectionModels.get(position).getSectionNumber()));
    }

    @Override
    public int getItemCount() {
        return sectionModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView sectionName,sectionNumber;

        public MyViewHolder(@NonNull View itemView,SectionInterface sectionInterface) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.sectionName);
            sectionNumber = itemView.findViewById(R.id.sectionNumber);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sectionInterface != null) {
                        int pos = getBindingAdapterPosition();

                        if(pos!=RecyclerView.NO_POSITION) {
                            sectionInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        return sectionFilter;
    }

    private Filter sectionFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<SectionModel> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length()==0 ) {
                filteredList.addAll(sectionModelsFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (SectionModel item:sectionModelsFull) {
                    if (item.getSectionName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        sectionModels.clear();
        sectionModels.addAll((ArrayList)filterResults.values);
        notifyDataSetChanged();
        }
    };
}

