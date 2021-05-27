package com.example.datospersonalistaalmacen.model.export;

import com.example.datospersonalistaalmacen.bean.UnaPersona;
import com.google.gson.Gson;

import java.util.List;


public class JSONExporter extends Exporter{
    @Override
    public String transform(List<UnaPersona> personaList) {
        Gson gson = new Gson();
        return gson.toJson(personaList);
    }
}
