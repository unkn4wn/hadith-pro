package com.islamicproapps.hadithpro.section;

public class SectionModel {
    String sectionName;
    int sectionNumber;

    public SectionModel(String sectionName, int sectionNumber) {
        this.sectionName = sectionName;
        this.sectionNumber = sectionNumber;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
}
