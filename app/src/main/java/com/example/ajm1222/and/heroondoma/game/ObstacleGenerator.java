package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;

import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;

import java.util.Random;

public class ObstacleGenerator implements IGameObject
{
    private final Random random = new Random();

    //이 시간에 대해서는 나중에 좀 더 다듬어서 처리할듯
    public static final float GEN_INTERVAL_BOMB = 20.0f;
    public static final float GEN_INTERVAL_DOLL = 10.0f;

    private final MainScene scene;

    private float bombTime = 20.0f;
    private float dollTime = 10.0f;

    private int wave; //아마 wave를 써서 일정 주기로 변동을 줄려고 넣은 변수인듯

    public ObstacleGenerator(MainScene mainScene) {this.scene = mainScene;}

    @Override
    public void update()
    {
        bombTime -= GameView.frameTime;
        dollTime -= GameView.frameTime;
        if(bombTime < 0)
        {
            generate(0);
            bombTime = GEN_INTERVAL_BOMB;
        }

        if(dollTime < 0)
        {
            generate(1);
            bombTime = GEN_INTERVAL_DOLL;
        }
    }

    private void generate(int selectNum)
    {
        // 화면 테두리 바깥 원형 랜덤 위치 생성
        double angle = random.nextFloat() * Math.PI * 2;

        float spawnRadius = Math.max(Metrics.width, Metrics.height) + 200f;
        float spawnX = Metrics.width / 2f + (float)(Math.cos(angle) * spawnRadius);
        float spawnY = Metrics.height / 2f + (float)(Math.sin(angle) * spawnRadius);

        // 내부 목표 지점 (테두리 안쪽 랜덤)
        float targetX = random.nextFloat() * Metrics.width;
        float targetY = random.nextFloat() * Metrics.height;


        switch (selectNum)
        {
            case 0:
            {
                scene.add(Bomb.get(spawnX,spawnY,targetX,targetY));
                break;
            }
            case 1:
            {
                scene.add(Doll.get(spawnX,spawnY,targetX,targetY));
               break;
            }
            default:
            {
                break;
            }
        }

    }

    @Override
    public void draw(Canvas canvas)
    {

    }
}
