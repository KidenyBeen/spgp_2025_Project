package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;
import com.example.ajm1222.and.a2dg.framework.res.BitmapPool;
import com.example.ajm1222.and.a2dg.framework.view.GameView;

public class GameTimer implements IGameObject
{
    private final Bitmap bitmap;
    private final float right, top, dstCharWidth, dstCharHeight;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();
    private final int srcCharWidth, srcCharHeight;
    private int time, displayTime;
    private float timeelapsed;

    public GameTimer(int mipmapId, float right, float top, float width) {
        this.bitmap = BitmapPool.get(mipmapId);
        this.right = right;
        this.top = top;
        this.dstCharWidth = width;
        this.srcCharWidth = bitmap.getWidth() / 10;
        this.srcCharHeight = bitmap.getHeight();
        this.dstCharHeight = dstCharWidth * srcCharHeight / srcCharWidth;
    }

    public void setTime(int time) {// 초기시간을 설정하는
        this.time = time;
    }

    public void add(int amount) { //시간을 추가하는
        time += amount;
    }

    @Override
    public void update() {

        timeelapsed += GameView.frameTime;

        if (timeelapsed >= 1.0f) {
            if (time > 0) {
                time--;
            }
            timeelapsed -= 1.0f;
        }

        if (time < displayTime)
        {
            displayTime--;
        } else if (time > displayTime)
        {
            displayTime++;
        }

    }

    @Override
    public void draw(Canvas canvas) {
        int value = this.displayTime;
        float x = right;
        while (value > 0) {
            int digit = value % 10;
            srcRect.set(digit * srcCharWidth, 0, (digit + 1) * srcCharWidth, srcCharHeight);
            x -= dstCharWidth;
            dstRect.set(x, top, x + dstCharWidth, top + dstCharHeight);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            value /= 10;
        }
    }

    public int gettime() {
        return time;
    }

}
