package com.example.datospersonalistaalmacen.model.export;

import android.content.ContextWrapper;
import android.os.Environment;

import com.example.datospersonalistaalmacen.bean.UnaPersona;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class Exporter {

    public abstract String transform(List<UnaPersona> personaList);

    public void export(ContextWrapper cw, String file, List<UnaPersona> personaList) {
        String content = this.transform(personaList);
        try {
            File dir = cw.getExternalFilesDir(Environment.getExternalStorageDirectory().getAbsolutePath());
            File f = new File(dir, file);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
