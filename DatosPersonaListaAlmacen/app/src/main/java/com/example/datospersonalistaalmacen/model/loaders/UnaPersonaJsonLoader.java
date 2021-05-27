package com.example.datospersonalistaalmacen.model.loaders;

import com.example.datospersonalistaalmacen.bean.UnaPersona;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UnaPersonaJsonLoader implements UrlDataLoader{

    private final static String KEY_AGENDA = "agenda";
    private final static String KEY_NAME = "nombre";
    private final static String KEY_SURNAME = "apellidos";
    private final static String KEY_PHONE = "tfno";
    private final static String KEY_DRIVING_LICENSE = "conduce";
    private final static String KEY_ENGLISH_LEVEL = "ingles";
    private final static String KEY_DATE = "registro";

    @Override
    public List<UnaPersona> load(String uri){

        JSONObject requestBody = null;
        try {

            HttpURLConnection connection = (HttpURLConnection) new URL(uri).openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            requestBody = new JSONObject(sb.toString());

            connection.disconnect();
            reader.close();

        } catch (JSONException | IOException | SecurityException e) {
            e.printStackTrace();
            return null;
        }

        JSONArray rawUsersArray = null;
        try {
            rawUsersArray = requestBody.getJSONArray(KEY_AGENDA);
        } catch (JSONException e) {
            return null;
        }

        List<UnaPersona> personaList = new ArrayList<>();
        for(int i = 0; i < rawUsersArray.length(); i++) {
            JSONObject rawUnaPersona;
            try {
                rawUnaPersona = rawUsersArray.getJSONObject(i);
            } catch (JSONException e) {
                continue;
            }
            UnaPersona p = this.loadSingle(rawUnaPersona);
            personaList.add(p);
        }

        return personaList;
    }

    private UnaPersona loadSingle(JSONObject objectFromURL){

        Date date;
        boolean drivingLicense;
        UnaPersona.EnglishLevel level;
        String name, surname, phone;

        try {
            name = objectFromURL.getString(KEY_NAME);
        } catch (JSONException e) {
            name = "Unknown";
        }

        try {
            surname = objectFromURL.getString(KEY_SURNAME);
        } catch (JSONException e) {
            surname = "Unknown";
        }

        try {
            phone = objectFromURL.getString(KEY_PHONE);
        } catch (JSONException e) {
            phone = "Unknown";
        }

        try {
            drivingLicense = objectFromURL.getString(KEY_DRIVING_LICENSE).equals("S");
        } catch (JSONException e) {
            drivingLicense = false;
        }

        try {
            switch (objectFromURL.getString(KEY_ENGLISH_LEVEL)){
                case "H":
                    level = UnaPersona.EnglishLevel.HIGH;
                    break;
                case "M":
                    level = UnaPersona.EnglishLevel.MEDIUM;
                    break;
                case "L":
                    level = UnaPersona.EnglishLevel.LOW;
                    break;
                default:
                    level = UnaPersona.EnglishLevel.LOW;
                    break;
            }
        } catch (JSONException e) {
            level = UnaPersona.EnglishLevel.LOW;
        }

        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(objectFromURL.getString(KEY_DATE));
        } catch (JSONException | ParseException e) {
            date = new Date();
        }

        return new UnaPersona(name,surname,"",date,phone,level,drivingLicense);
    }
}
