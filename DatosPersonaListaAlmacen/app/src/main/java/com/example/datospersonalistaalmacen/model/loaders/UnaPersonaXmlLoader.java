package com.example.datospersonalistaalmacen.model.loaders;

import com.example.datospersonalistaalmacen.bean.UnaPersona;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class UnaPersonaXmlLoader implements UrlDataLoader{

    private final static String KEY_AGENDA = "persona";
    private final static String KEY_NAME = "nombre";
    private final static String KEY_SURNAME = "apellidos";
    private final static String KEY_PHONE = "telefono";
    private final static String KEY_DRIVING_LICENSE = "conduce";
    private final static String KEY_ENGLISH_LEVEL = "nivel_ingles";
    private final static String KEY_DATE = "datereg";

    @Override
    public List<UnaPersona> load(String uri){
        NodeList nl = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new URL(uri).openStream()));
            doc.getDocumentElement().normalize();
            nl = doc.getElementsByTagName(KEY_AGENDA);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            return null;
        }

        List<UnaPersona> personaList = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Element e = (Element) nl.item(i);
            UnaPersona p = this.loadSingle(e);
            personaList.add(p);
        }

        return personaList;
    }

    private UnaPersona loadSingle(Element element){

        NodeList nl = element.getElementsByTagName(KEY_NAME);
        Element e = (Element) nl.item(0);
        nl = e.getChildNodes();
        String name = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(KEY_SURNAME);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        String surname = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(KEY_PHONE);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        String phone = nl.item(0).getNodeValue();

        nl = element.getElementsByTagName(KEY_DRIVING_LICENSE);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        String rawDrivingLicense = nl.item(0).getNodeValue();
        boolean drivingLicense = rawDrivingLicense.equals("S");

        nl = element.getElementsByTagName(KEY_ENGLISH_LEVEL);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        String rawLevel = nl.item(0).getNodeValue();
        UnaPersona.EnglishLevel level;
        switch (rawLevel){
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

        nl = element.getElementsByTagName(KEY_DATE);
        e = (Element) nl.item(0);
        nl = e.getChildNodes();
        String rawDate = nl.item(0).getNodeValue();
        Date date = null;
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(rawDate);
        } catch (ParseException e) {
            date = new Date();
        }

        return new UnaPersona(name,surname,"",date,phone,level,drivingLicense);
    }
}
