package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;

import com.example.ajm1222.and.a2dg.framework.activity.GameActivity;
import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.heroondoma.R;

import java.util.Random;

public class FruitsGenerator implements IGameObject {

    private final Random random = new Random();

    public static final float GEN_INTERVAL = 3.0f;

    private final MainScene scene;

    private float fruitsTime = 0;

    private int wave;

    public FruitsGenerator(MainScene mainScene) {this.scene = mainScene;}

    @Override
    public void update()
    {
        fruitsTime -= GameView.frameTime;
        if(fruitsTime < 0)
        {
            generate();
            fruitsTime = GEN_INTERVAL;
        }
    }

    private void generate()
    {

    }

    @Override
    public void draw(Canvas canvas)
    {

    }
}
