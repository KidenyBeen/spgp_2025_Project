package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IBoxCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.AnimSprite;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.heroondoma.R;
import java.util.Random;

public class Fruits extends Sprite implements IRecyclable , IBoxCollidable, ILayerProvider<MainScene.Layer>
{
    private final Random random = new Random();
    public static final int Fruits_COUNT = 8;
    private static final int Fruits_IN_A_ROW = 4;
    private static final int SIZE = 100;

    private static final int BORDER = 0;

    protected RectF collisionRect = new RectF();
    private float targetX, targetY;
    public static Fruits get(int index, float x, float y, float targetX, float targetY)
    {
        return Scene.top().getRecyclable(Fruits.class).init(index, x, y, targetX, targetY);
        //Scene.top은 현재 Scene을 의미한다.
        //현재 Scene의 IRecyclabel을 상속하고있으며 Fruits 클래스를 갖고있는 객체를 가져와서 init을 처리한다.
    }

    public Fruits()
    {
        super(R.mipmap.fruits_100x100_sprite_sheet);
        srcRect = new Rect();
        width = height = 300;
    }

    private Fruits init(int index, float x, float y, float targetX, float targetY) //팩토리 패턴으로
    {
        setSrcRect(index);
        setPosition(x, y, width,height);
        this.targetX = targetX;
        this.targetY = targetY;
        updateCollisionRect();
        // 방향 벡터 계산
        dx = targetX - x;
        dy = targetY - y;
        float distance = (float)Math.sqrt(dx * dx + dy * dy);
        float speed = 200f + random.nextFloat() * 300f; // 200~500

        dx = dx / distance * speed;
        dy = dy / distance * speed;

        return this;
    }

    private void setSrcRect(int index) {
        int x = index % Fruits_IN_A_ROW;
        int y = index / Fruits_IN_A_ROW;
        int left = x * (SIZE + BORDER) + BORDER;
        int top = y * (SIZE + BORDER) + BORDER;
        srcRect.set(left, top, left + SIZE, top + SIZE);
    }

    private void updateCollisionRect() {
        collisionRect.set(dstRect);
        //collisionRect.inset(50f, 50f);
    }
    @Override
    public void update() {
        // dx, dy는 방향 * 속도 벡터


        // 목표 지점에 거의 도달했으면 멈추고 재활용
        float remainingX = targetX - x;
        float remainingY = targetY - y;

        boolean arrivedX = (dx < 0 && x < targetX) || (dx > 0 && x >=targetX);
        boolean arrivedY = (dy < 0 && y < targetY) || (dy > 0 && y > targetY);

        if (arrivedX && arrivedY) {
            // 정확히 도착한 위치로 위치 조정
            dx = 0;
            dy = 0;
            setPosition(targetX, targetY, width, height);
            //Scene.top().remove(this); // 씬에서 제거
            return;
        }

        updateCollisionRect();
        // 아직 도달 안 했으면 이동
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

    @Override
    public RectF getCollisionRect()
    {
        return collisionRect;

    }
}
