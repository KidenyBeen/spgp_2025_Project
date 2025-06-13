package com.example.ajm1222.and.a2dg.framework.objects;

import android.graphics.Canvas;
import android.graphics.Rect;

public class AnimSprite extends Sprite {
    protected float fps; //초당 프레임 수 => 속도를 의미한다
    protected int frameCount; // 전체 프레임 개수
    protected int frameWidth; // 한 프레임의 이미지 크기
    protected int frameHeight; //

    //protected final long createdOn; // 객체가 생성된 시각
    protected long createdOn;
    public AnimSprite(int mipmapId, float fps) //리소스 ID, 애니메이션 속도, 비어있으면 프레임 개수 0으로 자동 계산
    {
        this(mipmapId, fps, 0);
    }
    public AnimSprite(int mipmapId, float fps, int frameCount) //
    {
        super(mipmapId);
        srcRect = new Rect();
        createdOn = System.currentTimeMillis();
        setFrameInfo(fps, frameCount);
    }

    public AnimSprite(int mipmapId, float fps, int frameWidth, int frameHeight)  //스프라이트 시트가 가로로만 있지 않은 경우를 위해서
    {
        super(mipmapId);
        srcRect = new Rect();
        createdOn = System.currentTimeMillis();
        setFrameInfo(fps, frameWidth, frameHeight);
    }

    private void setFrameInfo(float fps, int frameCount) //가로로만 있는경우
    {
        this.fps = fps;
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        if (frameCount == 0)
        {
            this.frameWidth = imageHeight;
            this.frameHeight = imageHeight;
            this.frameCount = imageWidth / imageHeight;
        }

        else
        {
            this.frameWidth = imageWidth / frameCount;
            this.frameHeight = imageHeight;
            this.frameCount = frameCount;
        }
    }

    public void setFrameInfo(float fps, int frameWidth, int frameHeight)  //스프라이트 시트가 가로x세로fh rntjdehls ruddn
    {
        this.fps = fps;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();

        int columns = imageWidth / frameWidth;
        int rows = imageHeight / frameHeight;

        this.frameCount = columns * rows;
    }

    public void setImageResourceId(int mipmapId, float fps)
    {
        setImageResourceId(mipmapId, fps, 0);
    }

    public void setImageResourceId(int mipmapId, float fps, int frameCount)
    {
        super.setImageResourceId(mipmapId);
        setFrameInfo(fps, frameCount);
    }

    @Override
    public void draw(Canvas canvas) { //아직 가로에 대해서만 처리하는걸로 둔다.
        // AnimSprite 는 단순반복하는 이미지이므로 time 을 update 에서 꼼꼼히 누적하지 않아도 된다.
        // draw 에서 생성시각과의 차이로 frameIndex 를 계산한다.
        long now = System.currentTimeMillis();
        float time = (now - createdOn) / 1000.0f;
        int frameIndex = Math.round(time * fps) % frameCount;
        srcRect.set(frameIndex * frameWidth, 0, (frameIndex + 1) * frameWidth, frameHeight);
        canvas.drawBitmap(bitmap, srcRect, dstRect, null);
    }
}
