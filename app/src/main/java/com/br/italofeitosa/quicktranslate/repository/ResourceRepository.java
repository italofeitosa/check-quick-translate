package com.br.italofeitosa.quicktranslate.repository;

import android.app.ProgressDialog;

import com.br.italofeitosa.quicktranslate.model.Resource;

import java.util.List;

/**
 * @author italofeitosa on 16/06/17.
 */

public interface ResourceRepository {

    List<Resource> getResourcesList(ProgressDialog progress);

    List<Resource> getFromFilter(ProgressDialog progress, String language, String module);

    List<String> getLanguages();

    List<String> getModule();

    List<Resource> searchValue(String search, String language, String module, ProgressDialog progress);

}