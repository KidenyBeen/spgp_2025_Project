package com.example.ajm1222.and.heroondoma.game;

import android.content.SharedPreferences;
import android.view.MotionEvent;

import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.objects.Button;
import com.example.ajm1222.and.a2dg.framework.objects.Score;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.res.Sound;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;

import com.example.ajm1222.and.heroondoma.R;


public class MainScene extends Scene {

    private static final String TAG = MainScene.class.getSimpleName();
    private final Score score;
    private final GameTimer timer;

    private final HP hp;
    private TouchDot activeDot;

    private LobyScene.Mode mode;

    //private final Score score;
    public enum Layer {
        bg,Fruit, FruitSlice,Bomb,Doll,DollSlice,TouchDot , explosion,ui, controller, touch;
        public static final int COUNT = values().length;
    }

    public MainScene(LobyScene.Mode mode)
    {

        this.mode = mode;
        initLayers(Layer.COUNT); //Scene내에 Layer 종류별로 배열을 초기화

        this.score = new Score(R.mipmap.number_24x32, 850f, 50f, 60f);
        score.setScore(0);

        this.timer = new GameTimer(R.mipmap.number_24x32, Metrics.width/2.0f, 50f,60f, true);
        timer.setTime(100);

        this.hp = new HP(R.mipmap.heart_200x200, Metrics.width - 100, Metrics.height, 100,10);
        hp.setHp(5);

        float w = Metrics.width, h = Metrics.height;

        add(MainScene.Layer.bg, new Sprite(R.mipmap.doma, w/2,h/2,w,h));

        add(Layer.controller, new FruitsGenerator(this));
        add(Layer.controller , new CollisionChecker(this));
        add(Layer.controller, new ObstacleGenerator(this));
        add(Layer.ui, score); //ILayerProvider를 상속하지 않은 친구에 추가 방법
        add(Layer.ui, timer);
        add(Layer.ui, hp);

        add(Layer.touch, new Button(R.mipmap.btn_pause, 850f, 0.0f, 100f, 100f, new Button.OnTouchListener() {
            @Override
            public boolean onTouch(boolean pressed) {
                if(pressed) {
                    new PauseScene(timer, score, true).push();
                }
                return false;
            }
        }));

        add(Layer.ui, new FadeInOverlay(this));

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

    public void setHP(int amount)
    {
        hp.setHp(amount);
    }
    public void addHp(int amount)
    {
        hp.add(amount);
    }
    public int getHp()
    {
        return hp.getHp();
    }

    @Override
    protected int getTouchLayerIndex() {
    return MainScene.Layer.touch.ordinal();
}
    @Override
    public boolean onTouchEvent(MotionEvent event) { //터치 이벤트를 좀 더 상세히 설정할 필요가 있다.


        // 1. 버튼 터치 처리: DOWN만 버튼에게 넘기고, 이후 처리는 TouchDot과 별개로 진행
        boolean handled = super.onTouchEvent(event);
        if (handled) return true;

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

    @Override
    public void update()
    {
        super.update();

        switch (mode)
        {
            case TIME_LIMITED:
            {
                timer.setActive(true);
                if(getTime() ==0 || getHp() <= 0 ) //시간이 전부 끝나면 혹은 체력이 전부다 닳으면
                {
                    new PauseScene(timer,score, false).push();
                }
                break;
            }
            case INFINITE:
            {
                timer.setActive(false);
                if(getHp() <= 0 ) //체력이 전부다 닳으면
                {
                    new PauseScene(timer,score, false).push();
                }
                break;
            }
            case SCORE_TARGET:
            {
                timer.setActive(false);
                if(score.getScore() >= 1000  || getHp() <= 0) //일정 점수를 넘으면 혹은 체력이 다 닳으면
                {
                    new PauseScene(timer,score, false).push();
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }


    @Override
    public void onEnter() {

        Sound.playMusic(R.raw.funny_bgm_240795);

    }

    @Override
    public void onExit()
    {
        Sound.stopMusic();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }


}
