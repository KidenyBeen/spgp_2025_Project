package com.example.ajm1222.and.heroondoma.game;

import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.AnimSprite;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;

public class Fruits extends Sprite implements IRecyclable , ILayerProvider<MainScene.Layer>
{
    public static Fruits get()
    {
        return Scene.top().getRecyclable(Fruits.class).init();
    }
    public Fruits()
    {
        super(0);
    }

    private Fruits init()
    {
        //초기화 하는 부분
        return this;
    }

    @Override
    public void onRecycle()
    {

    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.Fruit;
    }
}
