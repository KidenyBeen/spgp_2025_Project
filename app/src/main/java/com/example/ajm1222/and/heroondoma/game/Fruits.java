package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IBoxCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.AnimSprite;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.res.Sound;
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
    private boolean currentColliding = false;         // 현재 충돌 중인지
    private boolean perviousColliding = false;        // 이전 프레임에 충돌했는지
    private float collisionX, collisionY; //터치한 지점의 위치 값
    private float perviousCollsionX, perviousCollsionY;

    public static final float _INTERVAL = 0.5f;

    private float fruitsTime = 0;

    protected RectF collisionRect = new RectF();

    private int n_index; //

    private final MainScene scene;

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
        scene = (MainScene) Scene.top();
    }

    private Fruits init(int index, float x, float y, float targetX, float targetY) //팩토리 패턴으로
    {
        n_index = index;
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
        collisionRect.inset(50f, 50f);
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

        //

        if(!perviousColliding && currentColliding)
        {
            onCollisionEnter();
        }
        else if (perviousColliding && currentColliding)
        {
            onCollisionStay();
        }
        else if (perviousColliding && !currentColliding)
        {
            onCollisionExit();
        }
        perviousColliding = currentColliding;
        currentColliding = false;

        // 아직 도달 안 했으면 이동
        super.update();
    }

    private Path createSliceMask(float dx, float dy)  //path 공간 사각형을 만들어서 클리핑 마스킹 공간을 만든다.
    {
        float cx = x;
        float cy = y;


        float perpX = -dy * 150f; // 법선 만들었던거의 다시 회전을 시켜 자른 단면 방향 벡터를 얻어낸다.
        float perpY = dx * 150f;

        Path path = new Path();
        path.moveTo(cx + perpX, cy + perpY);
        path.lineTo(cx - perpX, cy - perpY);


        path.lineTo(cx - perpX + dx * 1000, cy - perpY + dy * 1000);
        path.lineTo(cx + perpX + dx * 1000, cy + perpY + dy * 1000);
        path.close();
        //

        return path;
    }


    public void SetCollisionObjectPosition(float x, float y)
    {
        this.collisionX = x;
        this.collisionY = y;
    }
    public void SetOnCollision()
    {
        currentColliding = true;
    }
    private void onCollisionEnter() //충돌 시작
    {
        perviousCollsionX = collisionX;
        perviousCollsionY = collisionY;
    }
    private void onCollisionStay() //충돌 도중
    {
//        scene.remove(this);
//        scene.addScore(10);

        fruitsTime += GameView.frameTime;

        if(getCollisionDistance() > 50) //빠르게 선을 그으면 된다 ==> 선 판정이 되었을떄.
        {
            scene.addScore(10);
            scene.remove(this);

            Sound.playEffect(R.raw.slice_apple_on_wood); //효과음

            float dx = collisionX - perviousCollsionX;
            float dy = collisionY - perviousCollsionY;

            float length = (float) Math.sqrt(dx * dx + dy * dy);
            if (length == 0) return; // 나누기 0 방지

            dx /= length;
            dy /= length;

            Path leftMask = createSliceMask(-dy, dx);
            Path rightMask = createSliceMask(dy, -dx);

            scene.add(FruitSlice.get(n_index,x,y,-dy,dx, leftMask));
            scene.add(FruitSlice.get(n_index,x,y,dy,-dx, rightMask));

        }

        if(fruitsTime >= _INTERVAL)
        {
            perviousCollsionX = collisionX;
            perviousCollsionY = collisionY;
            fruitsTime = 0;
        }
    }

    private void onCollisionExit() //충돌한 이후 나갈때
    {
        perviousCollsionX = -1;
        perviousCollsionY = -1;
        collisionX = -1;
        collisionY = -1;
    }


    private float getCollisionDistance() //점과 직선 사이의 거리를 알기 위해
    {
        float dx = collisionX - perviousCollsionX;
        float dy = collisionY - perviousCollsionY;
        return (float) Math.sqrt(dx * dx + dy * dy);
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
