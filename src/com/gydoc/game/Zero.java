package com.gydoc.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Zero extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.light_off);
        LightOffView mainView = (LightOffView) findViewById(R.id.lightOffView);

        if (savedInstanceState == null) {
            mainView.setMode(LightOffView.MODE_NEW);
        } else {
            mainView.setMode(LightOffView.MODE_NEW);
        }
    }
}
