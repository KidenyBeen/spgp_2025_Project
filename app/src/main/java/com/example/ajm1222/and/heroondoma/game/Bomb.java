package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IBoxCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.heroondoma.R;

import java.util.Random;

public class Bomb  extends Sprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer>
{

    private final Random random = new Random();
    public static final float _INTERVAL = 0.5f;
    private float BombTime = 0;
    private boolean currentColliding = false;         // 현재 충돌 중인지
    private boolean perviousColliding = false;        // 이전 프레임에 충돌했는지

    private float targetX, targetY;

    protected RectF collisionRect = new RectF();
    private final MainScene scene;
    public static Bomb get( float x, float y, float targetX, float targetY)
    {
        return Scene.top().getRecyclable(Bomb.class).init( x, y, targetX, targetY);
    }

    public Bomb()
    {
        super(R.mipmap.fruits_100x100_sprite_sheet);
        srcRect = new Rect();
        width = height = 300;
        scene = (MainScene) Scene.top();
    }

    private Bomb init( float x, float y, float targetX, float targetY) //팩토리 패턴으로
    {
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

    private void updateCollisionRect() {
        collisionRect.set(dstRect);
        //collisionRect.inset(50f, 50f);
    }

    private float collisionX, collisionY; //터치한 지점의 위치 값
    private float perviousCollsionX, perviousCollsionY;

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

        BombTime += GameView.frameTime;

        if(getCollisionDistance() > 50) //빠르게 선을 그으면 된다 ==> 선 판정이 되었을떄.
        {
//            scene.addScore(10);
//            scene.remove(this);
            //터지고, 체력이 깎인다.
        }

        if(BombTime >= _INTERVAL)
        {
            perviousCollsionX = collisionX;
            perviousCollsionY = collisionY;
            BombTime = 0;
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
    public void update() {
        // dx, dy는 방향 * 속도 벡터


        // 목표 지점에 거의 도달했으면 멈추고 재활용


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

        //이 아래는 충돌 관련 처리라 충돌을 제어하는 모든 오브젝트들은 이걸 처리해야한다.

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

    @Override
    public void onRecycle()
    {

    }

    @Override
    public MainScene.Layer getLayer() {
        return MainScene.Layer.Bomb;
    }

    @Override
    public RectF getCollisionRect()
    {
        return collisionRect;

    }

}
