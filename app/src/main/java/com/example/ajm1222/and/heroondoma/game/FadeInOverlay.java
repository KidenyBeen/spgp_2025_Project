package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;

public class FadeInOverlay implements IGameObject
{
    private float alpha = 1.0f;
    private float duration = 1.5f;
    private float elapsed = 0f;
    private final Paint paint = new Paint();
    private final MainScene scene;

    public FadeInOverlay(MainScene mainScene) {

        this.scene = mainScene;
        paint.setColor(Color.BLACK);
    }

    @Override
    public void update() {
        elapsed += GameView.frameTime;
        alpha = 1.0f - (elapsed / duration);
        if (alpha <= 0f) {
            scene.remove(MainScene.Layer.ui, this); // 자동 제거
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (alpha <= 0f) return;
        paint.setAlpha((int)(alpha * 255));
        canvas.drawRect( 0,-500, Metrics.width, 3000, paint);
    }
}
