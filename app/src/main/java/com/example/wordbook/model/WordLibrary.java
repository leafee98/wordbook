package com.example.wordbook.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import com.example.wordbook.ui.share.LogTag;

import java.util.ArrayList;
import java.util.List;

public class WordLibrary {
    //SQLiteQuery sqlQuery = SQLiteQueryBuilder.buildQueryString()

    private SQLiteDatabase wordDb;

    static final String TABLE_NAME = "wordLib";
    private static final String DATABASE_NAME = "wordBook.db";
    private static final int DATABASE_VERSION = 1;

    private static WordLibrary instance;

    public static WordLibrary getInstance(Context context) {
        if (instance == null)
            instance = new WordLibrary(context);
        return instance;
    }

    private WordLibrary(Context context) {
        WordDbHelper wordDbHelper = new WordDbHelper(context, DATABASE_NAME,
                null, DATABASE_VERSION);
        this.wordDb= wordDbHelper.getWritableDatabase();
    }

    public long insertWord(Word w) {
        ContentValues values = new ContentValues();
        values.put("word", w.getWord());
        values.put("interpretation", w.getInterpretation());
        values.put("example", w.getExample());

        w.setId(this.wordDb.insert(TABLE_NAME, null, values));
        if (w.getId() > 0) {
            Log.i(LogTag.wordLibrary, String.format("inserted word: %s", w.toString()));
        } else {
            Log.e(LogTag.wordLibrary, String.format("insert word failed, it's %s", w.toString()));
        }

        return w.getId();
    }

    // return empty list if no word got
    public List<Word> searchWord(String word) {
        Cursor cursor;
        ArrayList<Word> result = new ArrayList<>();

        if (word != null) {
            cursor = this.wordDb.query(TABLE_NAME, null,
                    String.format("word like '%%%s%%'", word),
                    null, null, null, "word");
        } else {
            cursor = this.wordDb.query(TABLE_NAME, null, null,
                    null, null, null, "word");
        }
        if (cursor.moveToFirst()) {
            do {
                result.add(new Word(
                        cursor.getLong(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("word")),
                        cursor.getString(cursor.getColumnIndex("interpretation")),
                        cursor.getString(cursor.getColumnIndex("example"))

                ));
            } while (cursor.moveToNext());
        }
        cursor.close();

        Log.i(LogTag.wordLibrary, String.format("search word, get %d item(s).", result.size()));
        return result;
    }

    public boolean updateWord(Word w) {
        ContentValues values = new ContentValues();
        values.put("word", w.getWord());
        values.put("interpretation", w.getInterpretation());
        values.put("example", w.getExample());
        values.put("id", w.getId());
        long affected = this.wordDb.update(TABLE_NAME, values, "id = ?",
                new String[] { w.getId().toString() });

        if (affected > 0) {
            Log.i(LogTag.wordLibrary, String.format("updated word succeed, the new word is: %s.", w.toString()));
            return true;
        } else {
            Log.e(LogTag.wordLibrary, String.format("updated word failed, the new word is: %s.", w.toString()));
            return true;
        }
    }

    public boolean deleteWord(Long id) {
        long affected = this.wordDb.delete(TABLE_NAME, "id = ?",
                new String[] { id.toString() });

        if (affected > 0) {
            Log.i(LogTag.wordLibrary, String.format("deleted word succeed, its id=%d", id));
            return true;
        } else {
            Log.e(LogTag.wordLibrary, String.format("deleted word failed, its id=%d", id));
            return false;
        }
    }
}
