package com.example.wordbook.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class WordDbHelper extends SQLiteOpenHelper {
    private Context context;
    private final static String DROP_TABLE =
            String.format("drop table %s if exists;", WordLibrary.TABLE_NAME);
    private final static String CREATE_TABLE = "create table " + WordLibrary.TABLE_NAME + " ("
            + "id integer primary key autoincrement, "
            + "word text,"
            + "interpretation text,"
            + "example text );";

    public WordDbHelper(Context context, String databaseName,
                        SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databaseName, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(context, "database created", Toast.LENGTH_SHORT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABLE);
        Toast.makeText(context, "database upgraded", Toast.LENGTH_SHORT);
    }
}
