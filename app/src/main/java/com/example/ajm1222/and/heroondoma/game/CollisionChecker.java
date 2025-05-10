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
/*
1. 충돌 박스를 원으로 변경 ==> 과일들을 원으로 생각할꺼니까
2. 원과 선의 충돌 처리를 생각 ==> 터치 시스템에 선으로 표현할 예정.
 */

//        ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
//        for (int e = enemies.size() - 1; e >= 0; e--) {
//            Enemy enemy = (Enemy)enemies.get(e);
//            ArrayList<IGameObject> bullets = scene.objectsAt(MainScene.Layer.bullet);
//            for (int b = bullets.size() - 1; b >= 0; b--) {
//                Bullet bullet = (Bullet)bullets.get(b);
//                if (CollisionHelper.collides(enemy, bullet)) {
//                    Log.d(TAG, "Collision !! : Bullet@" + System.identityHashCode(bullet) + " vs Enemy@" + System.identityHashCode(enemy));
//                    scene.remove(bullet);
//                    scene.remove(enemy);
////                    removed = true;
//                    break;
//                }
//            }
//        }
    }

    @Override
    public void draw(Canvas canvas) {}
}
