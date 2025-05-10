package com.example.ajm1222.and.heroondoma.app;

import android.os.Bundle;

import com.example.ajm1222.and.a2dg.framework.activity.GameActivity;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.heroondoma.BuildConfig;
import com.example.ajm1222.and.heroondoma.game.LobyScene;
import com.example.ajm1222.and.heroondoma.game.MainScene;

public class HeroOnDomaActivity extends GameActivity {

    @Override
    protected  void onCreate(Bundle savedInstanceState)
    {
        GameView.drawsDebugStuffs = BuildConfig.DEBUG;
        super.onCreate(savedInstanceState);
        new LobyScene().push();
    }

}
