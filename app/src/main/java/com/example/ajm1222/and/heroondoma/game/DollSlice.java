package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;

import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

public class DollSlice extends Sprite implements IRecyclable, ILayerProvider<MainScene.Layer>
{


    private static final int SIZE = 200;


    private final int speed = 800;

    private float initialX, initialY;

    private Path maskPath = null;

    public static DollSlice get(int index, float x, float y, float dx, float dy, Path maskPath)
    {
        return Scene.top().getRecyclable(DollSlice.class).init(index, x, y, dx, dy,maskPath);
    }

    public DollSlice()
    {
        super(R.mipmap.doll_sprite_sheet_600x200);
        srcRect = new Rect();
        width = height = 300;
    }

    private DollSlice init(int index, float x, float y, float dx, float dy, Path maskPath) //팩토리 패턴으로
    {
        setSrcRect(index);
        setPosition(x, y, width,height);

        initialX = x;
        initialY = y;

        this.maskPath = maskPath;

        this.dx = dx * speed;
        this.dy = dy * speed;

        return this;
    }


    private void setSrcRect(int index) {
        int x = index ;

        int left = x * (SIZE) ;

        srcRect.set(left, 0, left + SIZE,  SIZE);
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
    public void draw(Canvas canvas)
    {
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
        return MainScene.Layer.DollSlice;
    }
}
