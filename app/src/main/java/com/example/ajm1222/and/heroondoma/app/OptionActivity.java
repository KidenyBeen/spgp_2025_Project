package com.example.ajm1222.and.heroondoma.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ajm1222.and.heroondoma.R;

public class OptionActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "sound_preferences";
    private static final String KEY_MASTER = "master_volume";
    private static final String KEY_BGM = "bgm_volume";
    private static final String KEY_SFX = "sfx_volume";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        SeekBar mainSeek = findViewById(R.id.seekbar_main_volume);
        SeekBar bgmSeek = findViewById(R.id.seekbar_bgm_volume);
        SeekBar sfxSeek = findViewById(R.id.seekbar_sfx_volume);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); //찾아보니 SharedPreferences라는 값을 저장하고 액티비티에서 사용가능하게 해주는게 있음

        // 초기값 설정 ==> 한 번 설정했던 값을 계속 반영하기 위해 getFloat를 쓰는중
        mainSeek.setProgress((int)(prefs.getFloat(KEY_MASTER, 1.0f) * 100)); //존재하지 않으면 기본 값 1.0f로 넣도록 한다.
        bgmSeek.setProgress((int)(prefs.getFloat(KEY_BGM, 1.0f) * 100));
        sfxSeek.setProgress((int)(prefs.getFloat(KEY_SFX, 1.0f) * 100));

        mainSeek.setOnSeekBarChangeListener(makeListener(KEY_MASTER));
        bgmSeek.setOnSeekBarChangeListener(makeListener(KEY_BGM));
        sfxSeek.setOnSeekBarChangeListener(makeListener(KEY_SFX));
    }

    private SeekBar.OnSeekBarChangeListener makeListener(String key) {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float value = progress / 100.0f;

                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                prefs.edit().putFloat(key, value).apply();


            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        };
    }
}


