package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Rect;

import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.heroondoma.R;

public class FruitSlice extends Sprite implements IRecyclable, ILayerProvider<MainScene.Layer>

{

    public static FruitSlice get(int index, float x, float y, float targetX, float targetY)
    {
        return Scene.top().getRecyclable(FruitSlice.class).init();
    }

    public FruitSlice()
    {
        super(R.mipmap.fruits_100x100_sprite_sheet);
        srcRect = new Rect();
        width = height = 300;
    }

    private FruitSlice init() //팩토리 패턴으로
    {
//        setSrcRect(index);
//        setPosition(x, y, width,height);
//        this.targetX = targetX;
//        this.targetY = targetY;
//        updateCollisionRect();
//        // 방향 벡터 계산
//        dx = targetX - x;
//        dy = targetY - y;
//        float distance = (float)Math.sqrt(dx * dx + dy * dy);
//        float speed = 200f + random.nextFloat() * 300f; // 200~500
//
//        dx = dx / distance * speed;
//        dy = dy / distance * speed;

        return this;
    }


    @Override
    public void update() {

        super.update();
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
