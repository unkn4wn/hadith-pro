package com.islamicproapps.hadithpro.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.islamicproapps.hadithpro.R;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private View mView;
    private static final String DATABASE_NAME = "Bookmark.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME_HADITHS = "hadiths";
    private static final String TABLE_NAME_HADITHSGRADES = "hadithsgrades";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ARABIC = "hadith_arabic";
    private static final String COLUMN_TRANSLATED = "hadith_translated";
    private static final String COLUMN_REFERENCE = "hadith_reference";
    private static final String COLUMN_BOOKREFERENCE = "hadith_bookreference";
    private static final String COLUMN_LANGUAGE = "hadith_language";

    private static final String COLUMN_GRADESCHOLARS = "hadith_scholars";
    private static final String COLUMN_GRADES = "hadith_grades";

    public MyDatabaseHelper(@Nullable Context context, View view) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.mView = view;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryHadiths = "CREATE TABLE " + TABLE_NAME_HADITHS +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ARABIC + " TEXT, " +
                COLUMN_TRANSLATED + " TEXT, " +
                COLUMN_REFERENCE + " TEXT, " +
                COLUMN_BOOKREFERENCE + " TEXT, " +
                COLUMN_LANGUAGE + " TEXT);";

        String queryHadithsGrades = "CREATE TABLE " + TABLE_NAME_HADITHSGRADES +
                " (" + COLUMN_REFERENCE + " TEXT REFERENCES " + TABLE_NAME_HADITHS + "(" + COLUMN_REFERENCE + ")" + ", " +
                COLUMN_GRADESCHOLARS + " TEXT, " +
                COLUMN_GRADES + " TEXT, " +
                COLUMN_LANGUAGE + " TEXT);";
        db.execSQL(queryHadiths);
        db.execSQL(queryHadithsGrades);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HADITHS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HADITHSGRADES);
        onCreate(db);
    }

    public void addHadith(String arabicHadithText, String translatedHadithText, String reference, String bookreference, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ARABIC, arabicHadithText);
        cv.put(COLUMN_TRANSLATED, translatedHadithText);
        cv.put(COLUMN_REFERENCE, reference);
        cv.put(COLUMN_BOOKREFERENCE, bookreference);
        cv.put(COLUMN_LANGUAGE, language);
        System.out.println(cv.getAsString(COLUMN_TRANSLATED));
        long result = db.insert(TABLE_NAME_HADITHS, null, cv);
        if (result == -1) {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_add_error), Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_add), Toast.LENGTH_SHORT).show();
        }
    }

    public void addHadithGrades(String reference, String gradeScholars, String grades, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_REFERENCE, reference);
        cv.put(COLUMN_GRADESCHOLARS, gradeScholars);
        cv.put(COLUMN_GRADES, grades);
        cv.put(COLUMN_LANGUAGE, language);
        long result = db.insert(TABLE_NAME_HADITHSGRADES, null, cv);
        if (result == -1) {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_add_error), Toast.LENGTH_SHORT).show();
        } else {
        }
    }

    public Cursor readHadithData() {
        String query = "SELECT * FROM " + TABLE_NAME_HADITHS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public Cursor readHadithGradesData(String reference, String language) {
        String query = "SELECT * FROM " + TABLE_NAME_HADITHSGRADES +
                " where hadith_reference=" + "\"" + reference + "\"" +
                " and hadith_language="+ "\"" + language + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateData(String row_id, String arabicHadithText, String translatedHadithText, String gradeScholars, String grades, String reference, String bookreference, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ARABIC, arabicHadithText);
        cv.put(COLUMN_TRANSLATED, translatedHadithText);
        cv.put(COLUMN_GRADESCHOLARS, gradeScholars);
        cv.put(COLUMN_GRADES, grades);
        cv.put(COLUMN_REFERENCE, reference);
        cv.put(COLUMN_BOOKREFERENCE, bookreference);

        long result = db.update(tableName, cv, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteOneRow(String tableName, String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tableName, "_id=?", new String[]{row_id});
        if (result == -1) {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_remove_error), Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_remove), Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteOneRowReference(String tableName, String reference, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(tableName, "hadith_reference=? and hadith_language=?", new String[]{reference,language});
        if (result == -1) {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_remove_error), Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(mView, context.getResources().getString(R.string.message_bookmark_remove), Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor getIdOfLastRow(String tableName) {
        String query = "SELECT SUM(\"_id\") FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    public void deleteAllData(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + tableName);
    }

}