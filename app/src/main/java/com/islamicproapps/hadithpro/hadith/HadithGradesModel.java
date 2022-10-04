package com.islamicproapps.hadithpro.hadith;

public class HadithGradesModel {
    String hadithGradesScholar;
    String hadithGradesAuthenticity;


    public HadithGradesModel(String hadithGradesScholar, String hadithGradesAuthenticity) {
        this.hadithGradesScholar = hadithGradesScholar;
        this.hadithGradesAuthenticity = hadithGradesAuthenticity;
    }

    public String getHadithGradesScholar() {
        return hadithGradesScholar;
    }

    public void setHadithGradesScholar(String hadithGradesScholar) {
        this.hadithGradesScholar = hadithGradesScholar;
    }

    public String getHadithGradesAuthenticity() {
        return hadithGradesAuthenticity;
    }

    public void setHadithGradesAuthenticity(String hadithGradesAuthenticity) {
        this.hadithGradesAuthenticity = hadithGradesAuthenticity;
    }
}
