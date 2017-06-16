package com.br.italofeitosa.quicktranslate;

import com.br.italofeitosa.quicktranslate.dagger.AppComponent;
import com.br.italofeitosa.quicktranslate.dagger.AppModule;
import com.br.italofeitosa.quicktranslate.dagger.DaggerAppComponent;
import com.br.italofeitosa.quicktranslate.dagger.NetworkModule;

/**
 * @author italofeitosa on 14/06/17.
 */
public class Application  extends android.app.Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();


    }

    public AppComponent component() {
        return appComponent;
    }
}
