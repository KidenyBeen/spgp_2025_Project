package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

public class FruitSlice extends Sprite implements IRecyclable, ILayerProvider<MainScene.Layer>

{

    private float initialX, initialY;
    private static final int Fruits_IN_A_ROW = 4;

    private static final int SIZE = 100;

    private static final int BORDER = 0;

    private final int speed = 800;

    private Path maskPath = null;

    public static FruitSlice get(int index, float x, float y, float dx, float dy, Path maskPath)
    {
        return Scene.top().getRecyclable(FruitSlice.class).init(index, x, y, dx, dy,maskPath);
    }

    public FruitSlice()
    {
        super(R.mipmap.fruits_100x100_sprite_sheet);
        srcRect = new Rect();
        width = height = 300;
    }

    private FruitSlice init(int index, float x, float y, float dx, float dy, Path maskPath) //팩토리 패턴으로
    {
        setSrcRect(index);
        setPosition(x, y, width,height);
        initialX = x;
        initialY = y;
        this.dx = dx * speed;
        this.dy = dy * speed;
        this.maskPath =maskPath;
        return this;
    }


    private void setSrcRect(int index) {
        int x = index % Fruits_IN_A_ROW;
        int y = index / Fruits_IN_A_ROW;
        int left = x * (SIZE + BORDER) + BORDER;
        int top = y * (SIZE + BORDER) + BORDER;
        srcRect.set(left, top, left + SIZE, top + SIZE);
    }

    @Override
    public void update() {

        super.update();

        if(x < 0 || x> Metrics.width || y < 0 || y > Metrics.height)
        {
            Scene.top().remove(this);
        }
    }


    @Override
    public void draw(Canvas canvas) {


        if (maskPath != null) {
            canvas.save();

            maskPath.offset(x - initialX, y -initialY);
            // 클리핑
            canvas.clipPath(maskPath);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);

            canvas.restore();

            initialX = x;
            initialY = y;
        }
    }

    @Override
    public void onRecycle()
    {

    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.FruitSlice;
    }
}
