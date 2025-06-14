package com.example.ajm1222.and.heroondoma.game;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import com.example.ajm1222.and.a2dg.framework.objects.Button;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

public class LobyScene extends Scene {
    private static final String TAG = LobyScene.class.getSimpleName();
    public enum Layer {
        bg,ui, controller, touch;
        public static final int COUNT = values().length;
    }

    public enum Mode
    {
        TIME_LIMITED,    // 시간 제한 모드
        SCORE_TARGET,    // 목표 점수 모드
        INFINITE;         // 무한 모드
        public static final int COUNT = values().length;
    }

    private final float cx;
    private final float startY;
    private final float gap;
    private final float buttonW;
    private final float buttonH;
    public LobyScene()
    {
        initLayers(Layer.COUNT);

        cx = Metrics.width / 2f;  // 화면 중앙 X
        startY = 400f;           // 첫 버튼 Y
        gap = 300f;               // 버튼 간격
        buttonW = 600f;
        buttonH = 200f;

        float w = Metrics.width, h = Metrics.height;

        add(LobyScene.Layer.bg, new Sprite(R.mipmap.doma, w/2,h/2,w,h));

        add(Layer.touch, new Button(R.mipmap.mode1, cx, startY, buttonW, buttonH, new Button.OnTouchListener()
        {
            @Override
            public boolean onTouch(boolean pressed) {
                Log.d(TAG, "Button: TIME_LIMITED");
                ChangeMode(Mode.TIME_LIMITED);
                return false;
            }
        }));

        add(Layer.touch, new Button(R.mipmap.mode2, cx, startY + gap, buttonW, buttonH,new Button.OnTouchListener()
        {
            @Override
            public boolean onTouch(boolean pressed) {
                Log.d(TAG, "Button: SCORE_TARGET");
                ChangeMode(Mode.SCORE_TARGET);
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.mode3, cx, startY + gap * 2, buttonW, buttonH,new Button.OnTouchListener()
        {
            @Override
            public boolean onTouch(boolean pressed) {
                Log.d(TAG, "Button: INFINITE");
                ChangeMode(Mode.INFINITE);
                return false;
            }
        }));
    }
    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }
    private void ChangeMode(Mode mode)
    {
        MainScene mainScene = new MainScene(mode);
        GameView.view.pushScene(mainScene);

    }

//    @Override //이게 있으면 터치 이벤트가 동작을 안하네
//    public boolean onTouchEvent(MotionEvent event) {
////        switch (event.getAction()) {
////            case MotionEvent.ACTION_DOWN:
////            case MotionEvent.ACTION_MOVE:
////                float[] xy = Metrics.fromScreen(event.getX(), event.getY());
////                if (xy[0] < 100 && xy[1] < 100) {
////
////                    MainScene mainScene = new MainScene();
////                    GameView.view.pushScene(mainScene);
////                    return false;
////                }
////                return true;
////        }
//        return false;
//    }

}
