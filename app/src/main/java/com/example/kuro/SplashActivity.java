package com.example.kuro;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kuro.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            if (Utils.getUid() != null)
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            else
                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
            finish();
        }, 1000);
    }
}
