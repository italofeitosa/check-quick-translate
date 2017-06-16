package com.br.italofeitosa.quicktranslate.dagger;

import com.br.italofeitosa.quicktranslate.ui.SplashScreenActivity;
import com.br.italofeitosa.quicktranslate.ui.TranslateActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author italofeitosa on 14/06/17.
 */
@Singleton
@Component(modules = {NetworkModule.class, AppModule.class})
public interface AppComponent {

    void inject(SplashScreenActivity activity);
    void inject(TranslateActivity activity);

}
