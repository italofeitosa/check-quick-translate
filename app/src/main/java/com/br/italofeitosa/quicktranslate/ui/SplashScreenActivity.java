package com.br.italofeitosa.quicktranslate.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.br.italofeitosa.quicktranslate.Application;
import com.br.italofeitosa.quicktranslate.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author italofeitosa on 14/06/17.
 */
public class SplashScreenActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((Application) getApplication()).component().inject(SplashScreenActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(!isConnected){
            Toast.makeText(this, R.string.connection, Toast.LENGTH_LONG).show();
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                finish();
                Intent intent = new Intent();
                intent.setClass(SplashScreenActivity.this, TranslateActivity.class);
                startActivity(intent);
            }
        },3000);
    }
}
