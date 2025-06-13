package com.example.ajm1222.and.heroondoma.game;

import android.view.MotionEvent;

import com.example.ajm1222.and.a2dg.framework.objects.Score;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;


public class MainScene extends Scene {

    private static final String TAG = MainScene.class.getSimpleName();
    private final Score score;
    private final GameTimer timer;

    private TouchDot activeDot;


    //private final Score score;
    public enum Layer {
         Fruit, FruitSlice,Bomb,Doll,DollSlice,TouchDot , explosion,ui, controller;
        public static final int COUNT = values().length;
    }

    public MainScene(LobyScene.Mode mode)
    {

        initLayers(Layer.COUNT); //Scene내에 Layer 종류별로 배열을 초기화

        this.score = new Score(R.mipmap.number_24x32, 850f, 50f, 60f);
        score.setScore(0);

        this.timer = new GameTimer(R.mipmap.number_24x32, Metrics.width/2.0f, 50f,60f);
        timer.setTime(100);


        add(Layer.controller, new FruitsGenerator(this));
        add(Layer.controller , new CollisionChecker(this));
        add(Layer.controller, new ObstacleGenerator(this));
        add(Layer.ui, score); //ILayerProvider를 상속하지 않은 친구에 추가 방법
        add(Layer.ui, timer);

    }
    public void addScore(int amount) {
        score.add(amount);
    }

    public int getScore() {
        return score.getScore();
    }

    public void addTime(int amount)
    {
        timer.add(amount);
    }
    public int getTime()
    {
        return timer.gettime();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) { //터치 이벤트를 좀 더 상세히 설정할 필요가 있다.
        //return fighter.onTouch(event);
        float[] pts = Metrics.fromScreen(event.getX(), event.getY());
        float x = pts[0], y = pts[1];

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // TouchDot 하나 생성해서 Scene에 등록
                TouchDot dot = TouchDot.get(x, y);
                add(MainScene.Layer.TouchDot, dot);
                activeDot = dot;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (activeDot != null) {
                    activeDot.setPosition(x, y); // 실시간 위치 갱신
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (activeDot != null) {
                    remove(MainScene.Layer.TouchDot, activeDot); // Scene에서 제거 (자동 재활용)
                    activeDot = null;
                }
                break;
            }
        }
        return true;
    }


}
