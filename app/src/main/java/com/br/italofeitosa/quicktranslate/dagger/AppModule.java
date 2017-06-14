package com.br.italofeitosa.quicktranslate.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.br.italofeitosa.quicktranslate.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * @author italofeitosa on 14/06/17.
 */
@Module
public class AppModule {

    private Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        Realm.init(application);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        return Realm.getDefaultInstance();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }
}
