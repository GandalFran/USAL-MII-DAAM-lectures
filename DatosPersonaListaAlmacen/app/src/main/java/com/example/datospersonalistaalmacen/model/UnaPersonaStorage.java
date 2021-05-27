package com.example.datospersonalistaalmacen.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.datospersonalistaalmacen.util.Utils;
import com.example.datospersonalistaalmacen.bean.UnaPersona;
import com.example.datospersonalistaalmacen.model.contentprovider.StorageProvider;
import com.example.datospersonalistaalmacen.model.export.Exporter;
import com.example.datospersonalistaalmacen.model.export.XMLExporter;
import com.example.datospersonalistaalmacen.model.loaders.UnaPersonaDummyLoader;
import com.example.datospersonalistaalmacen.model.loaders.UnaPersonaJsonLoader;
import com.example.datospersonalistaalmacen.model.loaders.UnaPersonaXmlLoader;
import com.example.datospersonalistaalmacen.model.loaders.UrlDataLoader;

import java.util.ArrayList;
import java.util.List;

public class UnaPersonaStorage implements Parcelable {

    private static final String JSON_FILE = "agenda.json";
    private static final String XML_FILE = "agenda.xml";

    public enum StorageType {
        FILE_JSON,
        FILE_XML,
        CONTENT_PROVIDER
    }

    private final static String KEY_NAME = "NAME";
    private final static String KEY_SURNAME = "SURNAME";
    private final static String KEY_AGE = "AGE";
    private final static String KEY_PHONE = "PHONE";
    private final static String KEY_DRIVING_LICENSE = "DRIVING_LICENSE";
    private final static String KEY_ENGLISH_LEVEL = "ENGLISH_LEVEL";
    private final static String KEY_DATE = "DATE";

    private StorageProvider provider;
    private List<UnaPersona> personaList;

    public UnaPersonaStorage(Context context){
        this.personaList = new ArrayList<>();
        this.provider = new StorageProvider(context, this.personaList);
    }

    protected UnaPersonaStorage(Parcel in) {
        this.personaList = in.createTypedArrayList(UnaPersona.CREATOR);
    }

    public static final Creator<UnaPersonaStorage> CREATOR = new Creator<UnaPersonaStorage>() {
        @Override
        public UnaPersonaStorage createFromParcel(Parcel in) {
            return new UnaPersonaStorage(in);
        }

        @Override
        public UnaPersonaStorage[] newArray(int size) {
            return new UnaPersonaStorage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel d, int flags) {
        d.writeTypedList(this.personaList);
    }

    // CRUD
    public void add(UnaPersona p){
        this.personaList.add(p);
    }

    public void update(UnaPersona p, int position){
        this.personaList.set(position, p);
    }

    public void remove(UnaPersona p){
        this.personaList.remove(p);
    }

    public List<UnaPersona> getPersonaList() {
        return personaList;
    }

    public void setPersonaList(List<UnaPersona> personaList) {
        this.personaList = personaList;
    }

    // Load
    public void load(String url){
        UrlDataLoader loader = null;
        if(url == null || url.isEmpty()){
            loader = new UnaPersonaDummyLoader();
        }else if (url.endsWith(".xml")) {
            loader = new UnaPersonaXmlLoader();
        }else{
            loader = new UnaPersonaJsonLoader();
        }

        List<UnaPersona> newPersonaList;
        try {
            newPersonaList = loader.load(url);
        }catch (Exception e){
            loader = new UnaPersonaDummyLoader();
            newPersonaList = loader.load(null);
        }
        this.personaList.addAll(newPersonaList);
    }

    // Store
    public void store(ContextWrapper cw, StorageType type){
        Exporter e;

        switch (type){
            case FILE_XML:
                e = new XMLExporter();
                e.export(cw, XML_FILE, this.personaList);
                break;
            case FILE_JSON:
                e = new XMLExporter();
                e.export(cw, JSON_FILE, this.personaList);
                break;
            case CONTENT_PROVIDER:
                this.storeIntoContentProvider();
                break;
        }
    }

    private void storeIntoContentProvider() {
        for (UnaPersona p : this.personaList) {

            // build values
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, p.getName());
            values.put(KEY_SURNAME, p.getSurname());
            values.put(KEY_AGE, p.getAge());
            values.put(KEY_PHONE, p.getPhone());
            values.put(KEY_DRIVING_LICENSE, p.isDrivingLicense());
            values.put(KEY_ENGLISH_LEVEL, p.getEnglishLevel().ordinal());
            values.put(KEY_DATE, Utils.getDateString(p.getDate()));

            // store into content provider
            this.provider.insert(StorageProvider.CONTENT_URI, values);
        }
    }

}
