package com.example.ajm1222.and.heroondoma.game;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IBoxCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.objects.Sprite;
import com.example.ajm1222.and.a2dg.framework.res.Sound;
import com.example.ajm1222.and.a2dg.framework.scene.Scene;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.view.Metrics;
import com.example.ajm1222.and.heroondoma.R;

import java.util.Random;

public class Doll  extends Sprite implements IRecyclable, IBoxCollidable, ILayerProvider<MainScene.Layer>
{
    private final Random random = new Random();
    public static final float _INTERVAL = 0.5f;
    private float DollTime = 0;
    private boolean currentColliding = false;         // 현재 충돌 중인지
    private boolean perviousColliding = false;        // 이전 프레임에 충돌했는지

    private static final float DOUBLE_CLICK_INTERVAL = 0.5f; //더블클릭을 위한 간격초
    private int doubleClickCount = 0; //더블클릭 카운트
    private float doublicClickTime = 0.0f; //

    private float lifeTime; // 던져지고 몇초 뒤에 삭제
    private final float throwPower = 800; //던지는 속도
    private boolean throwSet; // 던지는게 활성화 되면 다른 움직임 제약을 해제하기 위해서

    private int n_index;

    private static final int SIZE = 200;
    private float targetX, targetY;

    protected RectF collisionRect = new RectF();
    private final MainScene scene;
    public static Doll get(int index, float x, float y, float targetX, float targetY)
    {
        return Scene.top().getRecyclable(Doll.class).init(index ,x, y, targetX, targetY);
    }

    public Doll()
    {
        super(R.mipmap.doll_sprite_sheet_600x200);
        srcRect = new Rect();
        width = height = 300;
        scene = (MainScene) Scene.top();
    }

    private Doll init( int index , float x, float y, float targetX, float targetY) //팩토리 패턴으로
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

        throwSet = false;
        lifeTime = 0.0f;

        return this;
    }
    private void setSrcRect(int index) {
        int x = index ;

        int left = x * (SIZE) ;

        srcRect.set(left, 0, left + SIZE,  SIZE);
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

    private Path createSliceMask(float dx, float dy) {
        float cx = x;
        float cy = y;

        //
        float perpX = -dy * 150f; // 수직 방향
        float perpY = dx * 150f;

        Path path = new Path();
        path.moveTo(cx + perpX, cy + perpY);
        path.lineTo(cx - perpX, cy - perpY);

        //
        path.lineTo(cx - perpX + dx * 1000, cy - perpY + dy * 1000);
        path.lineTo(cx + perpX + dx * 1000, cy + perpY + dy * 1000);
        path.close();

        return path;
    }
    private void onCollisionEnter() //충돌 시작
    {
        perviousCollsionX = collisionX;
        perviousCollsionY = collisionY;
    }
    private void onCollisionStay() //충돌 도중
    {

        DollTime += GameView.frameTime;

        if(getCollisionDistance() > 50) //빠르게 선을 그으면 된다 ==> 선 판정이 되었을떄.
        {
            scene.addScore(-10);
            scene.remove(this);

            Sound.playEffect(R.raw.slice_apple_on_wood);

            float dx = collisionX - perviousCollsionX;
            float dy = collisionY - perviousCollsionY;

            float length = (float) Math.sqrt(dx * dx + dy * dy);
            if (length == 0) return; // 나누기 0 방지

            dx /= length;
            dy /= length;

            Path leftMask = createSliceMask(-dy, dx);
            Path rightMask = createSliceMask(dy, -dx);

            scene.add(DollSlice.get(n_index,x,y,-dy,dx, leftMask));
            scene.add(DollSlice.get(n_index,x,y,dy,-dx, rightMask));

        }

        if(DollTime >= _INTERVAL)
        {
            perviousCollsionX = collisionX;
            perviousCollsionY = collisionY;
            DollTime = 0;
        }
    }

    private void onCollisionExit() //충돌한 이후 나갈때
    {
        perviousCollsionX = -1;
        perviousCollsionY = -1;
        collisionX = -1;
        collisionY = -1;

        if(!throwSet)
        {
            doubleClickCount += 1; //충돌을 떠날때 한 번만 호출되니까
        }
    }

    private float getCollisionDistance() //점과 직선 사이의 거리를 알기 위해
    {
        float dx = collisionX - perviousCollsionX;
        float dy = collisionY - perviousCollsionY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public void update() {
        doublicClickTime += GameView.frameTime;

        if(!throwSet) //던지기 성공하기 전까지는 이걸 계속 호출
        {
            boolean arrivedX = (dx < 0 && x < targetX) || (dx > 0 && x >= targetX);
            boolean arrivedY = (dy < 0 && y < targetY) || (dy > 0 && y > targetY);

            if (arrivedX && arrivedY) {
                // 정확히 도착한 위치로 위치 조정
                dx = 0;
                dy = 0;
                setPosition(targetX, targetY, width, height);
                //Scene.top().remove(this); // 씬에서 제거
                return;
            }
        }
        else //던지기 성공 후 2.0초 뒤에 삭제
        {
            lifeTime += GameView.frameTime;
            if(lifeTime > 2.0f)
            {
                Scene.top().remove(this);
            }
        }

        if (doubleClickCount >= 2) //2번 입력이 되었으면 처리가 가능해야하지 ==>
        {
            double angle = random.nextFloat() * Math.PI * 2;

            float spawnRadius = Math.max(Metrics.width, Metrics.height) + 200f;
            float spawnX = Metrics.width / 2f + (float)(Math.cos(angle) * spawnRadius);
            float spawnY = Metrics.height / 2f + (float)(Math.sin(angle) * spawnRadius);
            dx = spawnX - x;
            dy = spawnY - y;

            float distance = (float)Math.sqrt(dx * dx + dy * dy);

            dx = dx/distance * throwPower;
            dy = dy/distance * throwPower;

            throwSet = true;
            doubleClickCount = 0;

            Sound.playEffect(R.raw.swipe2);
        }

        if (doublicClickTime >= DOUBLE_CLICK_INTERVAL) {
            doubleClickCount = 0;
            doublicClickTime = 0.0f;
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
        return MainScene.Layer.Doll;
    }

    @Override
    public RectF getCollisionRect()
    {
        return collisionRect;

    }

}
