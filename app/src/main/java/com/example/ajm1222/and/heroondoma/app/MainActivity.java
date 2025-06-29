package com.example.ajm1222.and.heroondoma.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ajm1222.and.heroondoma.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnStartGame(View view) {

        startActivity(new Intent(this, HeroOnDomaActivity.class));

    }

    public void onBtnOption(View view) {

        startActivity(new Intent(this, OptionActivity.class));
    }
}