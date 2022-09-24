package com.example.kuro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class AnimePage extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_page);

        Intent intent = getIntent();
        String message = intent.getStringExtra("id");

        textView = findViewById(R.id.animeId);
        textView.setText(message);
    }
}