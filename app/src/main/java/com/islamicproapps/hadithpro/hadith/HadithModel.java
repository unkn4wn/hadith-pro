package com.islamicproapps.hadithpro.hadith;

import java.util.List;

public class HadithModel {
    String hadithNumber;
    String hadithArabicName;
    String hadithEnglishName;

    String referenceText;
    String referenceBookText;

    private boolean isExpandable;
    private List<HadithGradesModel> nestedList;

    public HadithModel(String hadithNumber, String hadithArabicName, String hadithEnglishName, String referenceText, String referenceBookText,List<HadithGradesModel> itemList) {
        this.hadithNumber = hadithNumber;
        this.hadithArabicName = hadithArabicName;
        this.hadithEnglishName = hadithEnglishName;

        this.referenceText = referenceText;
        this.referenceBookText = referenceBookText;

        isExpandable = false;
        this.nestedList = itemList;

    }


    public String getHadithArabicName() {
        return hadithArabicName;
    }

    public void setHadithArabicName(String hadithArabicName) {
        this.hadithArabicName = hadithArabicName;
    }

    public String getHadithEnglishName() {
        return hadithEnglishName;
    }

    public void setHadithEnglishName(String hadithEnglishName) {
        this.hadithEnglishName = hadithEnglishName;
    }

    public String getReferenceText() {
        return referenceText;
    }

    public void setReferenceText(String referenceText) {
        this.referenceText = referenceText;
    }

    public String getReferenceBookText() {
        return referenceBookText;
    }

    public void setReferenceBookText(String referenceBookText) {
        this.referenceBookText = referenceBookText;
    }

    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
    }

    public boolean isExpandable() {
        return isExpandable;
    }

    public List<HadithGradesModel> getNestedList() {
        return nestedList;
    }

    public String getHadithNumber() {
        return hadithNumber;
    }

    public void setHadithNumber(String hadithNumber) {
        this.hadithNumber = hadithNumber;
    }

}
