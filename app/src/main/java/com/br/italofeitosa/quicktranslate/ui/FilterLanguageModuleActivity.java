package com.br.italofeitosa.quicktranslate.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

import com.br.italofeitosa.quicktranslate.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author  italofeitosa on 15/06/17.
 */

public class FilterLanguageModuleActivity extends Activity {

    @BindView(R.id.spinerLanguage)
    Spinner spinnerLanguage;

    @BindView(R.id.spinerModule)
    Spinner spinnerModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //((Application) getApplication()).component().inject(FilterLanguageModuleActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_language_module);
        ButterKnife.bind(this);
    }



}
