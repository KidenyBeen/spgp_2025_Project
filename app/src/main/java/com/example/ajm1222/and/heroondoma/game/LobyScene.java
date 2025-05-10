package com.example.ajm1222.and.heroondoma.game;

import android.view.MotionEvent;

import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

public class LobyScene extends Scene {
    public enum Layer {
        ui, controller;
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

        add(Layer.ui, new Sprite(R.mipmap.mode1, cx, startY, buttonW, buttonH));
        add(Layer.ui, new Sprite(R.mipmap.mode2, cx, startY + gap, buttonW, buttonH));
        add(Layer.ui, new Sprite(R.mipmap.mode3, cx, startY + gap * 2, buttonW, buttonH));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float[] xy = Metrics.fromScreen(event.getX(), event.getY());
                if (xy[0] < 100 && xy[1] < 100) {

                    MainScene mainScene = new MainScene();
                    GameView.view.pushScene(mainScene);
                    return false;
                }
                return true;
        }
        return false;
    }

}
