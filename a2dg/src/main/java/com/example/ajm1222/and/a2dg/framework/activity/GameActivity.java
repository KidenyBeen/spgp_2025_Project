package com.example.ajm1222.and.a2dg.framework.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ajm1222.and.a2dg.framework.view.GameView;


public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);

        setContentView(gameView); //직접 view를 띄우는 방식

        setFullScreen();

        gameView.setEmptyStackListener(new GameView.OnEmptyStackListener() {
            @Override
            public void onEmptyStack() {
                finish();
            }
        });

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback); //뒤로가기 버튼 누르는 이벤트 처리하는거
    }

    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed()
        {
            gameView.onBackPressed();
        }
    };

//    @Override
//    public void onBackPressed() { //deprecated 되서 이건 쓰지 않을거다
//        //super.onBackPressed();
//        gameView.onBackPressed();
//    }
    @SuppressWarnings("deprecation")
    public void setFullScreen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // API 30 이상: 최신 방식
            WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.setSystemBarsBehavior(
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                );
                insetsController.hide(WindowInsets.Type.systemBars());
            }
        } else {
            // API 29 이하: 기존 방식
            int flags = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            gameView.setSystemUiVisibility(flags);
        }
    }
}