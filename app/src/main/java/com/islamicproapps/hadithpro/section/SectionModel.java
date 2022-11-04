package com.islamicproapps.hadithpro.section;

public class SectionModel {
    int sectionNumber;
    String sectionName;

    public SectionModel(int sectionNumber,String sectionName) {
        this.sectionNumber = sectionNumber;
        this.sectionName = sectionName;
    }

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
