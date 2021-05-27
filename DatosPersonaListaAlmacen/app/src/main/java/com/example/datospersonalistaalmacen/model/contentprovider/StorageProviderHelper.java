package com.example.datospersonalistaalmacen.model.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.datospersonalistaalmacen.util.Utils;
import com.example.datospersonalistaalmacen.bean.UnaPersona;
import com.example.datospersonalistaalmacen.model.UnaPersonaStorage;

import java.util.List;

public class StorageProviderHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "Usersdb";
    public final static String KEY_ID = "U_ID";
    public final static String KEY_NAME = "NAME";
    public final static String KEY_SURNAME = "SURNAME";
    public final static String KEY_AGE = "AGE";
    public final static String KEY_PHONE = "PHONE";
    public final static String KEY_DRIVING_LICENSE = "DRIVING_LICENSE";
    public final static String KEY_ENGLISH_LEVEL = "ENGLISH_LEVEL";
    public final static String KEY_DATE = "DATE";

    private Context context;
    private List<UnaPersona> personaList;

    public StorageProviderHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, List<UnaPersona> personaList) {
        super(context, name, factory, version);
        this.personaList = personaList;
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void onCreate(SQLiteDatabase database) {
        this.createTable(database);
        this.load(database, this.context);
    }

    private void createTable(SQLiteDatabase database) {
        String select = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);"
                ,TABLE_NAME
                ,KEY_ID
                ,KEY_NAME
                ,KEY_SURNAME
                ,KEY_AGE
                ,KEY_PHONE
                ,KEY_DRIVING_LICENSE
                ,KEY_ENGLISH_LEVEL
                ,KEY_DATE);
        database.execSQL(select);
    }

    private void load(SQLiteDatabase database, Context context) {
        UnaPersonaStorage storage = new UnaPersonaStorage(context);
        for (UnaPersona p: this.personaList) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, p.getName());
            values.put(KEY_SURNAME, p.getSurname());
            values.put(KEY_AGE, p.getAge());
            values.put(KEY_PHONE, p.getPhone());
            values.put(KEY_DRIVING_LICENSE, p.isDrivingLicense());
            values.put(KEY_ENGLISH_LEVEL, p.getEnglishLevel().ordinal());
            values.put(KEY_DATE, Utils.getDateString(p.getDate()));
            database.insert(TABLE_NAME, null, values);
        }
    }
}
