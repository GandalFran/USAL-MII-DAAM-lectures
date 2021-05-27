package com.example.datospersonalistaalmacen.model.export;

import android.util.Xml;

import com.example.datospersonalistaalmacen.util.Utils;
import com.example.datospersonalistaalmacen.bean.UnaPersona;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class XMLExporter extends Exporter{
    @Override
    public String transform(List<UnaPersona> personaList) {
        StringWriter sw = new StringWriter();
        XmlSerializer xml = Xml.newSerializer();
        try {
            xml.setOutput(sw);
            xml.startDocument("UTF-8", true);
            xml.startTag("", "users");

            for (UnaPersona p: personaList) {
                xml.startTag("", "item");

                xml.startTag("", "name");
                xml.text(p.getName());
                xml.endTag("", "name");

                xml.startTag("", "surname");
                xml.text(p.getSurname());
                xml.endTag("", "surname");

                xml.startTag("", "phone");
                xml.text(p.getPhone());
                xml.endTag("", "phone");

                xml.startTag("", "drivingLicense");
                xml.text(p.isDrivingLicense() ? "S": "N");
                xml.endTag("", "drivingLicense");

                xml.startTag("", "englishLevel");
                xml.text(p.getEnglishLevel().name());
                xml.endTag("", "englishLevel");

                xml.startTag("", "date");
                xml.text(Utils.getDateString(p.getDate()));
                xml.endTag("", "date");

                xml.startTag("", "name");
                xml.text(p.getName());
                xml.endTag("", "name");

                xml.endTag("", "item");
            }

            xml.endTag("", "users");
            xml.endDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }
}