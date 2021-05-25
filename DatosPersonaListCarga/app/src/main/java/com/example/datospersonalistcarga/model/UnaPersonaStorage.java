package com.example.datospersonalistcarga.model;

import com.example.datospersonalistcarga.bean.UnaPersona;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UnaPersonaStorage {

    public static List<UnaPersona> staticStorage = null;

    public List<UnaPersona> load(){

        List<UnaPersona>personaList = new ArrayList<>();

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

        return personaList;
    }
}
