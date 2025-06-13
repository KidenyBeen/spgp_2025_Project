package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;

import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.util.CollisionHelper;

import java.util.ArrayList;

public class CollisionChecker implements IGameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private final MainScene scene;

    public CollisionChecker(MainScene mainScene) {
        this.scene = mainScene; //현재 충돌체크를 할 씬을 지정하는 거
    }
    @Override
    public void update() {

        ArrayList<IGameObject> dots = scene.objectsAt(MainScene.Layer.TouchDot);
        for(int d = dots.size() - 1 ; d >= 0; d--)
        {
            TouchDot dot = (TouchDot)dots.get(d);
            ArrayList<IGameObject> Fruitss = scene.objectsAt(MainScene.Layer.Fruit);
            ArrayList<IGameObject> Bombs = scene.objectsAt(MainScene.Layer.Bomb);
            ArrayList<IGameObject> Dolls = scene.objectsAt(MainScene.Layer.Doll);
            for(int f = Fruitss.size()- 1 ; f >= 0; f--) //과일이랑 충돌
            {
                Fruits fruit = (Fruits)Fruitss.get(f);
                if(CollisionHelper.collides(dot,fruit))
                {
                    fruit.SetCollisionObjectPosition(dot.x, dot.y);
                    fruit.SetOnCollision();

                }
            }
            for(int f = Bombs.size()- 1 ; f >= 0; f--) // 폭탄이랑 충돌
            {
                Bomb bomb = (Bomb)Bombs.get(f);
                if(CollisionHelper.collides(dot,bomb))
                {
                    bomb.SetCollisionObjectPosition(dot.x, dot.y);
                    bomb.SetOnCollision();

                }
            }
            for(int f = Dolls.size()- 1 ; f >= 0; f--) //돌이랑 충돌
            {
                Doll doll = (Doll)Dolls.get(f);
                if(CollisionHelper.collides(dot,doll))
                {
                    doll.SetCollisionObjectPosition(dot.x, dot.y);
                    doll.SetOnCollision();

                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {}
}
