package com.example.datospersonalistaalmacen.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.datospersonalistaalmacen.Utils;
import com.example.datospersonalistaalmacen.bean.UnaPersona;
import com.example.datospersonalistaalmacen.model.contentprovider.StorageProvider;
import com.example.datospersonalistaalmacen.model.loaders.UnaPersonaJsonLoader;
import com.example.datospersonalistaalmacen.model.loaders.UnaPersonaXmlLoader;
import com.example.datospersonalistaalmacen.model.loaders.UrlDataLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UnaPersonaStorage implements Parcelable {

    private final static String KEY_NAME = "NAME";
    private final static String KEY_SURNAME = "SURNAME";
    private final static String KEY_AGE = "AGE";
    private final static String KEY_PHONE = "PHONE";
    private final static String KEY_DRIVING_LICENSE = "DRIVING_LICENSE";
    private final static String KEY_ENGLISH_LEVEL = "ENGLISH_LEVEL";
    private final static String KEY_DATE = "DATE";

    private StorageProvider provider;
    private List<UnaPersona> personaList;

    public UnaPersonaStorage(){
        this.personaList = new ArrayList<>();
        this.provider = new StorageProvider();
    }

    protected UnaPersonaStorage(Parcel in) {
        this.personaList = in.createTypedArrayList(UnaPersona.CREATOR);
    }

    public static final Creator<UnaPersonaStorage> STORAGE_CREATOR = new Creator<UnaPersonaStorage>() {
        @Override
        public UnaPersonaStorage createFromParcel(Parcel in) {
            return new UnaPersonaStorage(in);
        }

        @Override
        public UnaPersonaStorage[] newArray(int size) {
            return new UnaPersonaStorage[size];
        }
    };

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
    public void load(){

       this.personaList = new ArrayList<>();

        personaList.add(new UnaPersona("Pepe", "Perez Martin", "10",
                Calendar.getInstance().getTime(), "111222333",
                UnaPersona.EnglishLevel.LOW, false));
        personaList.add(new UnaPersona("David", "Bisbal", "20",
                Calendar.getInstance().getTime(), "111222336",
                UnaPersona.EnglishLevel.MEDIUM, false));
        personaList.add(new UnaPersona("Martinez", "Arias", "30",
                Calendar.getInstance().getTime(), "111222334",
                UnaPersona.EnglishLevel.HIGH, true));
        personaList.add(new UnaPersona("Pedro", "Juanes Jimenez", "13",
                Calendar.getInstance().getTime(), "111222331",
                UnaPersona.EnglishLevel.LOW, true));
        personaList.add(new UnaPersona("Alejandro", "Santos Sanchez", "25",
                Calendar.getInstance().getTime(), "111232336",
                UnaPersona.EnglishLevel.MEDIUM, false));
        personaList.add(new UnaPersona("Jorge", "Alonso Garcia", "31",
                Calendar.getInstance().getTime(), "111262334",
                UnaPersona.EnglishLevel.HIGH, true));
    }

    public void loadUrl(String url){
        UrlDataLoader loader = null;
        if (url.endsWith(".xml")) {
            loader = new UnaPersonaXmlLoader();
        }else{
            loader = new UnaPersonaJsonLoader();
        }
        List<UnaPersona> newPersonaList = loader.load(url);
        this.personaList.addAll(newPersonaList);
    }

    // Store
    public void storeIntoContentProvider() {
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
