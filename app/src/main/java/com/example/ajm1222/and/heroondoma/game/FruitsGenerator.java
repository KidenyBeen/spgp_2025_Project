package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;

import com.example.ajm1222.and.a2dg.framework.activity.GameActivity;
import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
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
        int index = random.nextInt(8);
        // 화면 테두리 바깥 원형 랜덤 위치 생성
        double angle = random.nextFloat() * Math.PI * 2;

        float spawnRadius = Math.max(Metrics.width, Metrics.height) + 200f;
        float spawnX = Metrics.width / 2f + (float)(Math.cos(angle) * spawnRadius);
        float spawnY = Metrics.height / 2f + (float)(Math.sin(angle) * spawnRadius);

        // 내부 목표 지점 (테두리 안쪽 랜덤)
        float targetX = random.nextFloat() * Metrics.width;
        float targetY = random.nextFloat() * Metrics.height;


        scene.add(Fruits.get(index,spawnX,spawnY,targetX,targetY));
    }

    @Override
    public void draw(Canvas canvas)
    {

    }
}
