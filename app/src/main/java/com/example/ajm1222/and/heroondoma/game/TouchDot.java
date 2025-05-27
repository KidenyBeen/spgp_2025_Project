package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.ajm1222.and.a2dg.framework.interfaces.IDotCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.util.RectUtil;
import com.example.ajm1222.and.heroondoma.R;

public class TouchDot implements IRecyclable, IGameObject, IDotCollidable, ILayerProvider<MainScene.Layer> {

    protected float x, y, dx, dy;
    protected boolean active;
    protected float pts[] = new float[2];
    public static TouchDot get(float x, float y)
    {
        return Scene.top().getRecyclable(TouchDot.class).init(x, y);

    }

    public TouchDot()
    {

    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.pts[0] = x;
        this.pts[1] = y;

    }
    private TouchDot init(float x, float y) //팩토리 패턴으로
    {
        setPosition(x, y);
        return this;
    }

    public float[] getCollisionPoint()
    {
        return pts;
    }
    public void onRecycle()
    {
    }

    public MainScene.Layer getLayer() {
        return MainScene.Layer.TouchDot;
    }
    public void update()
    {

    }
    public void draw(Canvas canvas)
    {

    }
}
