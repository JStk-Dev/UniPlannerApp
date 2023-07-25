package com.utp.proyecto_android.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class NotesDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notas.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla "Nota" y sus columnas
    public static final String TABLE_NOTAS = "notas";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_CONTENIDO = "contenido";

    // Sentencia SQL para crear la tabla "Nota"
    private static final String SQL_CREATE_TABLE_NOTAS =
            "CREATE TABLE " + TABLE_NOTAS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_TITULO + " TEXT," +
                    COLUMN_CONTENIDO + " TEXT" +
                    ")";

    public NotesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public NotesDBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
