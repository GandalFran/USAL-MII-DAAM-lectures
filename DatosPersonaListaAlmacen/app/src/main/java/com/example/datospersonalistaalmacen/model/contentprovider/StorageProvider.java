package com.example.datospersonalistaalmacen.model.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.example.datospersonalistaalmacen.bean.UnaPersona;

import java.util.List;

public class StorageProvider extends ContentProvider {

    private final static String AUTHORITY_NAME = "net.gandalfran";

    private final static int CODE_ALL_ENTRIES = 1;
    private final static int CODE_SINGLE_ENTRY = 2;

    public static final Uri CONTENT_URI = Uri.parse(String.format("content://%s/%s", AUTHORITY_NAME, StorageProviderHelper.TABLE_NAME));

    private static final  String MIME_SINGLE_ENTRY = String.format("vnd.android.cursor.item/vnd.%s%s", AUTHORITY_NAME, StorageProviderHelper.TABLE_NAME);
    private static final  String MIME_MULTIPLE_ENTRY = String.format("vnd.android.cursor.dir/vnd.%s%s", AUTHORITY_NAME, StorageProviderHelper.TABLE_NAME);

    private static final UriMatcher um = new UriMatcher(UriMatcher.NO_MATCH);

    private final StorageProviderHelper provider;

    static {
        um.addURI(AUTHORITY_NAME, StorageProviderHelper.TABLE_NAME, CODE_ALL_ENTRIES);
        um.addURI(AUTHORITY_NAME, String.format("%s/#", StorageProviderHelper.TABLE_NAME), CODE_SINGLE_ENTRY);
    }

    public StorageProvider(){
        this.provider = null;
    }

    public StorageProvider(Context context, List<UnaPersona> personaList){
        this.provider = new StorageProviderHelper(context, StorageProviderHelper.TABLE_NAME, null, 1, personaList);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public String getType(Uri uri) {
        switch (um.match(uri)) {
            case CODE_ALL_ENTRIES:
                return MIME_MULTIPLE_ENTRY;
            case CODE_SINGLE_ENTRY:
                return MIME_SINGLE_ENTRY;
            default:
                return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = null;
        SQLiteDatabase db = provider.getWritableDatabase();
        if(um.match(uri) == StorageProvider.CODE_ALL_ENTRIES){
            c = db.query(StorageProviderHelper.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        }else{
            long id = ContentUris.parseId(uri);
            c = db.query(StorageProviderHelper.TABLE_NAME,
                    projection,
                    String.format("%s = %s", StorageProviderHelper.KEY_ID, id),
                    selectionArgs,
                    null,
                    null,
                    sortOrder);
        }
        c.setNotificationUri(getContext().getContentResolver(), StorageProvider.CONTENT_URI);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.provider.getWritableDatabase();
        ContentValues cv = (values != null) ? new ContentValues(values) : new ContentValues();
        long id = db.insert(StorageProviderHelper.TABLE_NAME,null, cv);
        if (id > 0) {
            Uri userURI = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(userURI, null);
            return userURI;
        }
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int id;
        SQLiteDatabase db = provider.getWritableDatabase();
        if(um.match(uri) == CODE_ALL_ENTRIES){
            id = db.update(StorageProviderHelper.TABLE_NAME, values, selection, selectionArgs);
        }else{
            String query = String.format("%s = %s %s", StorageProviderHelper.KEY_ID, uri.getPathSegments().get(1));
            if (!TextUtils.isEmpty(selection))
                query = String.format("%s AND (%s)",query, selection);

            id = db.update(StorageProviderHelper.TABLE_NAME, values, query, selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int id;
        SQLiteDatabase db = provider.getWritableDatabase();
        if(um.match(uri) == CODE_ALL_ENTRIES){
            id = db.delete(StorageProviderHelper.TABLE_NAME, selection, selectionArgs);
        }else{
            String query = String.format("%s = %s %s", StorageProviderHelper.KEY_ID, uri.getPathSegments().get(1));
            if (!TextUtils.isEmpty(selection))
                query = String.format("%s AND (%s)",query, selection);
            id = db.delete(StorageProviderHelper.TABLE_NAME, query, selectionArgs);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }
}
