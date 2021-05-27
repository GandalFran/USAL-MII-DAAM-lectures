package com.example.datospersonalistaalmacen.model.loaders;

import com.example.datospersonalistaalmacen.bean.UnaPersona;

import java.util.List;

public interface UrlDataLoader {

    List<UnaPersona> load(String url) throws Exception;
}
