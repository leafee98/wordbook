package com.example.wordbook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.CursorLoader;

import com.example.wordbook.model.Word;
import com.example.wordbook.model.WordLibrary;

import java.util.List;

public class Provider extends ContentProvider {

    private static int MATCHED = 1;
    private WordLibrary wordLib;
    private UriMatcher matcher;

    public static final String AUTHORITY = "com.example.wordbook.provider.Provider";

    private Word getWord(ContentValues values) {
        String word, interpretation, example;
        Long id = values.getAsLong("id");
        word = values.getAsString("word");
        interpretation = values.getAsString("interpretation");
        example  = values.getAsString("example");

        Log.d(this.getClass().getName(), "word resolved: " + id + " " + word + " " + interpretation + " " + example);
        if (word == null || interpretation == null || example == null)
            return null;
        if (id != null)
            return new Word(id, word, interpretation, example);
        else
            return new Word(word, interpretation, example);
    }

    @Override
    public boolean onCreate() {
        this.wordLib = WordLibrary.getInstance(this.getContext());
        matcher = new UriMatcher(0);
        matcher.addURI("com.example.wordbook.provider.Provider", "word", MATCHED);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (matcher.match(uri) == MATCHED && values != null) {
            Word w = this.getWord(values);
            if (w != null) {
                wordLib.insertWord(w);
                return Uri.parse(uri.toString() + "/" + w.getId());
            }
        }
        return null;
    }

    // delete the word whose id equal to the value of selection. the selectionArgs will be ignored.
    // the selection should be a number described in string.
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (matcher.match(uri) == MATCHED && selection != null) {
            try {
                return wordLib.deleteWord(Long.parseLong(selection)) ? 1 : -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }

    // the values show describe a word, which must has everything of a word.
    // this method will update a word in database whose id equal to its.
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (matcher.match(uri) == MATCHED && values != null) {
            Word w = this.getWord(values);
            if (w != null) {
                return this.wordLib.updateWord(w) ? 1 : 0;
            }
        }
        return 0;
    }

    // just fuzzy search the word described by selection, any other parameters will be ignored.
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (matcher.match(uri) == MATCHED) {
            MatrixCursor cursor = new MatrixCursor(new String[]{"id", "word", "interpretation", "example"});
            List<Word> words = this.wordLib.searchWord(selection);
            for (int i = 0; i < words.size(); ++i) {
                Word w = words.get(i);
                cursor.addRow(new Object[] { w.getId(), w.getWord(), w.getInterpretation(), w.getExample()});
            }
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.example.provider.Provider.word";
    }
}
