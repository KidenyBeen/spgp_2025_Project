package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.res.BitmapPool;

public class HP implements IGameObject
{
    private final Bitmap bitmap;
    private final float right, top, size, gap;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();

    private int hp;
    public HP(int mipmapId, float right, float top, float width , float gap) {
        this.bitmap = BitmapPool.get(mipmapId);
        this.right = right;
        this.size = width;
        this.gap = gap;
        this.top = top;
    }

    public void setHp(int hp)
    {
        this.hp = hp;
    }

    public int getHp()
    {
        return hp;
    }
    public void add(int amount) { //점수를 추가하는
        hp += amount;
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < hp; i++) {
            float x = right - (size + gap) * i;
            RectF dst = new RectF(x, top, x + size, top + size);
            canvas.drawBitmap(bitmap, null, dst, null); // 전체 비트맵 사용
        }
    }
}
