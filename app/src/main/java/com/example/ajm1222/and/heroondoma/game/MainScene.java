package com.example.ajm1222.and.heroondoma.game;

import android.view.MotionEvent;

import com.example.ajm1222.and.a2dg.framework.objects.Score;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.heroondoma.R;


public class MainScene extends Scene {

    private static final String TAG = MainScene.class.getSimpleName();
    private final Score score;

    //private final Score score;
    public enum Layer {
         enemy, ui,controller;
        public static final int COUNT = values().length;
    }


    public MainScene()
    {

        initLayers(Layer.COUNT);

        this.score = new Score(R.mipmap.number_24x32, 850f, 50f, 60f);
        score.setScore(12345);
        add(Layer.ui, score); //ILayerProvider를 상속하지 않은 친구에 추가 방법

        add(Layer.controller, new CollisionChecker());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //터치 이벤트를 좀 더 상세히 설정할 필요가 있다.
        //return fighter.onTouch(event);
        return true;
    }
    private void checkCollision() {

    }


}
