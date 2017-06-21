package com.br.italofeitosa.quicktranslate.repository.impl;

import android.app.ProgressDialog;

import com.br.italofeitosa.quicktranslate.model.Resource;
import com.br.italofeitosa.quicktranslate.repository.ResourceRepository;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * @author italofeitosa on 16/06/17.
 */

public class ResourceRepositoryImpl implements ResourceRepository {

    private  Realm realm;

    public ResourceRepositoryImpl(Realm realm){
        this.realm = realm;
    }

    @Override
    public List<Resource> getResourcesList(ProgressDialog progress) {
        RealmQuery<Resource> resourceRealmQuery = realm.where(Resource.class);
        progress.dismiss();
        return resourceRealmQuery.findAll();
    }

    @Override
    public List<Resource> getFromFilter(ProgressDialog progress, String language, String module) {
        RealmQuery<Resource> resourceRealmResults = realm.where(Resource.class).equalTo("languageId", language).equalTo("moduleId",module);
        progress.dismiss();
        return resourceRealmResults.findAll();
    }

    @Override
    public List<String> getLanguages() {
        RealmResults<Resource> resourceRealmResults = realm.where(Resource.class).distinct("languageId");
        List<String> languageList = new ArrayList<>();
        for(Resource resources : resourceRealmResults){
            languageList.add(resources.getLanguageId());
        }

        return languageList;
    }

    @Override
    public List<String> getModule() {
        RealmResults<Resource> resourceRealmResults = realm.where(Resource.class).distinct("moduleId");
        List<String> modueleList = new ArrayList<>();
        for(Resource resources : resourceRealmResults){
            modueleList.add(resources.getModuleId());
        }

        return modueleList;
    }

    @Override
    public List<Resource> searchValue(String search, String language, String module, ProgressDialog progress) {
        RealmResults<Resource> resourceRealmResults = realm.where(Resource.class).equalTo("languageId", language).equalTo("moduleId",module)
                .contains("value", search, Case.INSENSITIVE).findAll();
        progress.dismiss();
        return resourceRealmResults;
    }
}