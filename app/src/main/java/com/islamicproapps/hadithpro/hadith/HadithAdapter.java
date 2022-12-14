package com.islamicproapps.hadithpro.hadith;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.snackbar.Snackbar;
import com.islamicproapps.hadithpro.R;
import com.islamicproapps.hadithpro.helper.MyDatabaseHelper;
import com.islamicproapps.hadithpro.helper.SharedPreferencesHelper;
import com.islamicproapps.hadithpro.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HadithAdapter extends RecyclerView.Adapter<HadithAdapter.MyViewHolder> {
    private final HadithInterface hadithInterface;

    Context context;
    List<HadithModel> mList;
    private List<HadithGradesModel> list = new ArrayList<>();

    HadithModel mModel;


    public HadithAdapter(Context context, List<HadithModel> mList, HadithInterface hadithInterface) {
        this.context = context;
        this.mList = mList;
        this.hadithInterface = hadithInterface;
    }

    @NonNull
    @Override
    public HadithAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview_hadith, parent, false);
        return new HadithAdapter.MyViewHolder(view, hadithInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HadithAdapter.MyViewHolder holder, int position) {

        holder.hadithNumber.setText(mList.get(position).getHadithNumber());

        holder.hadithArabicName.setText(mList.get(position).getHadithArabicName());
        holder.hadithEnglishName.setText(mList.get(position).getHadithEnglishName());

        holder.referenceText.setText(mList.get(position).getReferenceText());
        holder.referenceBookText.setText(mList.get(position).getReferenceBookText());

        HadithModel model = mList.get(position);
        mModel = model;

        boolean isExpandable = model.isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);

        if (isExpandable) {
            holder.mArrowImage.setImageResource(R.drawable.ic_baseline_arrow_upward_24);
        } else {
            holder.mArrowImage.setImageResource(R.drawable.ic_baseline_arrow_downward_24);
        }

        //set Hadith Grades
        HadithGradesAdapter adapter = new HadithGradesAdapter(list);
        holder.nestedRecyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        // holder.nestedRecyclerView.setHasFixedSize(true);
        holder.nestedRecyclerView.setAdapter(adapter);
        holder.linearLayout.setOnClickListener(v -> {
            model.setExpandable(!model.isExpandable());
            list = model.getNestedList();
            notifyItemChanged(holder.getBindingAdapterPosition());
        });

        //dont show Grades if there are no grades
        if (model.getNestedList().size() == 0) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.gradeTitleDivider.setVisibility(View.GONE);
        } else {
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.gradeTitleDivider.setVisibility(View.VISIBLE);
        }

        //Mark word after searching a word
        if (!model.getMarkedWord().isEmpty()) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, typedValue, true);
            int color = typedValue.data;

            if(SearchActivity.isSearchTranslatedText && holder.hadithEnglishName.getText().toString().contains(model.getMarkedWord())) {
                String textString = holder.hadithEnglishName.getText().toString();
                int startIndex = textString.indexOf(model.getMarkedWord());
                int endIndex = startIndex + model.getMarkedWord().length();
                Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
                spanText.setSpan(new BackgroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.hadithEnglishName.setText(spanText);
            }

            if(SearchActivity.isSearchNumber && holder.referenceText.getText().toString().contains(model.getMarkedWord())) {
                String textString = holder.referenceText.getText().toString();
                int startIndex = textString.indexOf(model.getMarkedWord());
                int endIndex = startIndex + model.getMarkedWord().length();
                Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
                spanText.setSpan(new BackgroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.referenceText.setText(spanText);
            }

            if(SearchActivity.isSearchArabicText && holder.hadithArabicName.getText().toString().contains(model.getMarkedWord())) {
                String textString = holder.hadithArabicName.getText().toString();
                int startIndex = textString.indexOf(model.getMarkedWord());
                int endIndex = startIndex + model.getMarkedWord().length();
                Spannable spanText = Spannable.Factory.getInstance().newSpannable(textString);
                spanText.setSpan(new BackgroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.hadithArabicName.setText(spanText);
            }
        }

        // implement copying Hadith
        holder.copyCardView.setOnClickListener(view -> showDialogCopy(holder, view));

        // check if Hadith is bookmarked. If yes fill icon
        String currentReferenceText = holder.referenceText.getText().toString();
        if (SharedPreferencesHelper.getValue(context, model.getLanguage() + currentReferenceText, false)) {
            holder.bookmarkIcon.setImageResource(R.drawable.symbol_bookmark_on);
        } else {
            holder.bookmarkIcon.setImageResource(R.drawable.symbol_bookmark_off);
        }

        // change text size or hide text according to settings
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        holder.hadithArabicName.setTextSize(sharedPreferences.getInt("textsize_arabic", 21));
        holder.hadithEnglishName.setTextSize(sharedPreferences.getInt("textsize_translation", 18));
        holder.hadithArabicName.setVisibility(sharedPreferences.getBoolean("display_arabic", true) ? View.VISIBLE : View.GONE);
        holder.hadithEnglishName.setVisibility(sharedPreferences.getBoolean("display_translation", true) ? View.VISIBLE : View.GONE);


        holder.bookmarkCardView.setOnClickListener(view -> {
            MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(context, view);
            if (SharedPreferencesHelper.getValue(context, model.getLanguage() + currentReferenceText, false)) {
                holder.bookmarkIcon.setImageResource(R.drawable.symbol_bookmark_off);
                SharedPreferencesHelper.storeValue(context, model.getLanguage() + currentReferenceText, false);
                // delete hadith and grades from database if bookmark is removed
                myDatabaseHelper.deleteOneRowReference("hadiths", currentReferenceText, model.getLanguage());
                myDatabaseHelper.deleteOneRowReference("hadithsgrades", currentReferenceText, model.getLanguage());
            } else {
                holder.bookmarkIcon.setImageResource(R.drawable.symbol_bookmark_on);
                SharedPreferencesHelper.storeValue(context, model.getLanguage() + currentReferenceText, true);

                myDatabaseHelper.addHadith(holder.hadithArabicName.getText().toString(), holder.hadithEnglishName.getText().toString(), currentReferenceText, holder.referenceBookText.getText().toString(), model.getLanguage());

                for (int i = 0; i < model.getNestedList().size(); i++) {
                    myDatabaseHelper.addHadithGrades(currentReferenceText, model.getNestedList().get(i).hadithGradesScholar, model.getNestedList().get(i).hadithGradesAuthenticity, model.getLanguage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hadithNumber, hadithArabicName, hadithEnglishName;
        TextView referenceText, referenceBookText;

        private final LinearLayout linearLayout;
        private final RelativeLayout expandableLayout;
        private final ImageView mArrowImage;
        private final RecyclerView nestedRecyclerView;
        private final MaterialDivider gradeTitleDivider;

        private final MaterialCardView copyCardView;
        private final MaterialCardView bookmarkCardView;
        private final ImageView bookmarkIcon;

        public MyViewHolder(@NonNull View itemView, HadithInterface hadithInterface) {
            super(itemView);

            hadithNumber = itemView.findViewById(R.id.hadithNumber);
            hadithArabicName = itemView.findViewById(R.id.hadithNameArabic);
            hadithEnglishName = itemView.findViewById(R.id.hadithNameEnglish);

            referenceText = itemView.findViewById(R.id.referenceText);
            referenceBookText = itemView.findViewById(R.id.referenceBookText);

            gradeTitleDivider = itemView.findViewById(R.id.gradeDivider);
            linearLayout = itemView.findViewById(R.id.gradesLinearLayout);
            expandableLayout = itemView.findViewById(R.id.expandable_layout);
            mArrowImage = itemView.findViewById(R.id.gradesImage);
            nestedRecyclerView = itemView.findViewById(R.id.child_rv);

            copyCardView = itemView.findViewById(R.id.copyCardView);
            bookmarkCardView = itemView.findViewById(R.id.bookmarkCardView);
            bookmarkIcon = itemView.findViewById(R.id.bookmarkIcon);

            itemView.setOnClickListener(view -> {
                if (hadithInterface != null) {
                    int pos = getBindingAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        hadithInterface.onItemClick(pos);
                    }
                }
            });
        }
    }


    private void showDialogCopy(HadithAdapter.MyViewHolder holder, View mView) {
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_copy);

        ImageButton closeButton = dialog.findViewById(R.id.closeBottomsheetButton);

        TextView titleFilter = dialog.findViewById(R.id.title_filter);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(titleFilter, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);

        MaterialCheckBox copyArabic = dialog.findViewById(R.id.copyArabic);
        MaterialCheckBox copyTranslated = dialog.findViewById(R.id.copyTranslated);
        MaterialCheckBox copyGrades = dialog.findViewById(R.id.copyGrades);
        MaterialCheckBox copyReference = dialog.findViewById(R.id.copyReference);

        MaterialButton copyButton = dialog.findViewById(R.id.copyButton);
        copyButton.setOnClickListener(view -> {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(copyArabic.isChecked() ? holder.hadithArabicName.getText() : "")
                    .append((copyTranslated.isChecked() ? "\n" + holder.hadithEnglishName.getText() : ""));

            if (copyGrades.isChecked()) {
                stringBuilder.append("\n");
                stringBuilder.append(context.getResources().getString(R.string.search_grade_title)).append(": ");
                for (int i = 0; i < mModel.getNestedList().size(); i++) {
                    stringBuilder.append("\n");
                    stringBuilder.append(mModel.getNestedList().get(i).hadithGradesScholar);
                    stringBuilder.append(": ");
                    stringBuilder.append(mModel.getNestedList().get(i).hadithGradesAuthenticity);
                }
            }
            stringBuilder.append(copyReference.isChecked() ? "\n" + context.getResources().getString(R.string.reference)+": " + holder.referenceText.getText() : "")
                    .append(copyReference.isChecked() ? "\n" + context.getResources().getString(R.string.reference_book)+": " + holder.referenceBookText.getText() : "");
            stringBuilder.append("\n");
            stringBuilder.append(context.getString(R.string.copyright));

            if (!stringBuilder.toString().isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) mView.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", stringBuilder);
                clipboard.setPrimaryClip(clip);
                Snackbar.make(mView, context.getResources().getString(R.string.message_copy_success), Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(mView, context.getResources().getString(R.string.message_copy_error), Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        closeButton.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = com.google.android.material.R.style.Animation_Design_BottomSheetDialog;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}

