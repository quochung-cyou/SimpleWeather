package com.quochung.minimalweather;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;

public class LogoActivity extends AppCompatActivity implements LifecycleObserver {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

    }
    @Override
    public void onResume(){
        super.onResume();
        new Handler().postDelayed(() -> {
            Intent vaoungdung = new Intent(LogoActivity.this, MainActivity.class);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            startActivity(vaoungdung);
            finish();
        }, 100);

    }


}
