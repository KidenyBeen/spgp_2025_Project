package com.example.ajm1222.and.heroondoma.game;

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

    public static DollSlice get(int index, float x, float y, float dx, float dy)
    {
        return Scene.top().getRecyclable(DollSlice.class).init(index, x, y, dx, dy);
    }

    public DollSlice()
    {
        super(R.mipmap.doll_sprite_sheet_600x200);
        srcRect = new Rect();
        width = height = 300;
    }

    private DollSlice init(int index, float x, float y, float dx, float dy) //팩토리 패턴으로
    {
        setSrcRect(index);
        setPosition(x, y, width,height);

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
    public void onRecycle()
    {

    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.DollSlice;
    }
}
