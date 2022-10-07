package com.islamicproapps.hadithpro.hadith;

import java.util.List;

public class HadithModel {
    String hadithNumber;
    String hadithArabicName;
    String hadithEnglishName;

    String referenceText;
    String referenceBookText;



    String markedWord;

    String language;

    private boolean isExpandable;
    private List<HadithGradesModel> nestedList;

    public HadithModel(String hadithNumber, String hadithArabicName, String hadithEnglishName, String referenceText, String referenceBookText,String language,List<HadithGradesModel> itemList) {
        this.hadithNumber = hadithNumber;
        this.hadithArabicName = hadithArabicName;
        this.hadithEnglishName = hadithEnglishName;

        this.referenceText = referenceText;
        this.referenceBookText = referenceBookText;
        this.language = language;

        isExpandable = false;
        this.nestedList = itemList;

        markedWord = "";

    }

    public HadithModel(String hadithNumber, String hadithArabicName, String hadithEnglishName, String referenceText, String referenceBookText,String language,List<HadithGradesModel> itemList,String markedWord) {
        this.hadithNumber = hadithNumber;
        this.hadithArabicName = hadithArabicName;
        this.hadithEnglishName = hadithEnglishName;

        this.referenceText = referenceText;
        this.referenceBookText = referenceBookText;
        this.language = language;

        isExpandable = false;
        this.nestedList = itemList;

        this.markedWord = markedWord;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getMarkedWord() {
        return markedWord;
    }

    public void setMarkedWord(String markedWord) {
        this.markedWord = markedWord;
    }

}
