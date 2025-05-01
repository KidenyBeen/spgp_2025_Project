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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            startActivity(new Intent(this, HeroOnDomaActivity.class));
//        }
//        return super.onTouchEvent(event);
//    }

    public void onBtnStartGame(View view) {
        // start game activity here

    }
    public void onBtnOption(View view) {
        // start game activity here

    }
}