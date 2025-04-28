package com.example.ajm1222.and.a2dg.framework.view;

import android.content.Context;


import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.util.AttributeSet;

import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull; //이 값이 절대 null이면 안 된다.
import androidx.annotation.Nullable; //이 값이 null일 수 있다는 애너테이션이다.

import com.example.ajm1222.and.a2dg.framework.scene.Scene;


import java.util.ArrayList;


public class GameView extends View implements Choreographer.FrameCallback {
    //View를 상속받는 클래스는 생성자가 없으면 오류가 난다.

//    private static final float SCREEN_WIDHT = 900f;
//    private static final float SCREEN_HEIGHT = 1600f;

    private static final String TAG = GameView.class.getSimpleName();



//일반적인 생성자로 만든 방식
//    private final Ball ball1 = new Ball(4.5f, 8.0f, 60);
//    private final Ball ball2 = new Ball(6.5f, 3.0f, 80);

    // 아무것도 선언안한 경우에 본인을 직접 호출하고 랜덤하게 값을 지정하도록 하는 방식
//    private final Ball ball1 = new Ball();
//    private final Ball ball2 = new Ball();

// 팩토리 메소드 방식을 이용해서 초기화 함수를 분리한 것
//    private final Ball ball1 = Ball.random();
//    private final Ball ball2 = Ball.random();

//    private final  ArrayList<Ball> balls = new ArrayList<>();
//    private final ArrayList<BouncingCircle> circles = new ArrayList<>();


//    private Scene scene;
    private ArrayList<Scene> sceneStack = new ArrayList<>();
    private static long previousNanos; //
    public static float frameTime; //프레임 간격 시간을 의미하는듯?

    public static GameView view;
    public static boolean drawsDebugStuffs = false;
    public interface OnEmptyStackListener {
        public void onEmptyStack();
    }
    private OnEmptyStackListener emptyStackListener;
    public void setEmptyStackListener(OnEmptyStackListener emptyStackListener) {
        this.emptyStackListener = emptyStackListener;
    }

    public GameView(Context context)
    {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) { //Preview를 위해서 기본적으로 선언해줘야하는 추가적인 생성자 하지만
        super(context, attrs);
        init();
    }

    //AttributeSet set; //멤버변수롤 AttributeSet을 선언만해도 Preview가 나온다 하지만 생성자를 넣는 방법으로 하자.

    private void init()
    {
        //실질적인 생성자 역할
        GameView.view = this;

        //ballBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.soccer_ball_240); //BitMap 읽는 방식인가보오

        scheduleUpdate(); //한 번 호출해서 Update를 일정 주기마다 호출
    }
//    public void setCurrentScene(Scene scene)
//    {
//        this.scene = scene;
//    }
    public void pushScene(Scene scene) //scene를 stack에 쌓는 과정이고
    {
//        this.sceneStack.add(scene);
        int last = sceneStack.size() - 1;
        if (last >= 0) {
            sceneStack.get(last).onPause();
        }
        sceneStack.add(scene);
        scene.onEnter();
    }
    public Scene getTopScene() { //stack에서 scene을 읽는과정
        //return sceneStack.getLast();
        // Call requires API level 35 (current min is 24): java. util. ArrayList#getLast
        int last = sceneStack.size() - 1;
        if (last < 0) return null;
        return sceneStack.get(last);
    }
    public Scene popScene() { //stack에서 scene을 뽑아내느 과정
        int last = sceneStack.size() - 1;
        if (last < 0) {
            notifyEmptyStack();
            return null;
        }
        //return sceneStack.remove(last);
        Scene top = sceneStack.remove(last);
        top.onExit();
        if (last >= 1) {
            sceneStack.get(last - 1).onResume();
        }
        else {
            notifyEmptyStack();
        }
        return top;
    }

    private void notifyEmptyStack() {
        if (emptyStackListener != null) {
            emptyStackListener.onEmptyStack();
        }
    }
    public void changeScene(Scene scene) {
        int last = sceneStack.size() - 1;
        if (last < 0) return;
        sceneStack.get(last).onExit();
        sceneStack.add(scene);
        scene.onEnter();
    }

    public void onBackPressed() {
        int last = sceneStack.size() - 1;
        //if (last < 0) return; // finish activity here ?

        if (last < 0) { //Stack이 완전히 비어 있을 때에도 Back 버튼에 의하여 Activity가 종료가 가능하게 바뀐것
            notifyEmptyStack(); // finish activity here
            return;
        }
        Scene scene = sceneStack.get(last);
        boolean handled = scene.onBackPressed();
        if (handled) return;

        popScene();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { //View 크기가 바뀔 때 자동으로 호출하는 생명주기 함수
        super.onSizeChanged(w, h, oldw, oldh);
        Metrics.onSize(w,h);
    }
    @Override
    protected void onDraw(@NonNull Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.save();
        Metrics.concat(canvas);
        if (drawsDebugStuffs) {
            drawDebugBackground(canvas);
        }//출시할때 빼면 된다능
//        for (BouncingCircle bc : circles) {
//            bc.draw(canvas);
//        }
//        for(Ball ball : balls) //C#의 foreach문이랑 같아보이네
//        {
//            ball.draw(canvas);
//        }
//        fighter.draw(canvas);
        Scene scene = getTopScene();
        if(scene != null)
        {
            scene.draw(canvas);
        }
        canvas.restore();
        if (drawsDebugStuffs) { //디버깅일때만 출력이 될 것이다. build.gradle.kts에 추가해둔 내용이 존재하며 이걸로 인해 가능한것
            drawDebugInfo(canvas);
        }
    }

    // 터치 이벤트를 제어하는거니까 이건 내 프로젝트에서 충분히 쓰인다.
    @Override
    public boolean onTouchEvent(MotionEvent event) //안드로이드에서 터치 이벤트 제어
    {
        Scene scene = getTopScene();
        if(scene != null)
        {
            return scene.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    //private long lastUpdateTime = 0;
    private void scheduleUpdate() // postDelayed로 일정 주기마다 호출되도록 설정하고 있다.
    {
//        postDelayed(gameLoopRunnable
//        ,1000/60); // 1/60 60프레임

        // 무조건 1000/60이 아니라 프레임간의 간격 시간을 구해서 최대 거기까지 기다리도록 설정하는 방법.
//        long now = System.currentTimeMillis();
//        long elapsedSinceLastUpdate = now - lastUpdateTime;
//        long targetDelay = 1000/60;
//        long delay = Math.max(0,targetDelay - elapsedSinceLastUpdate);
//
//        postDelayed(gameLoopRunnable,delay);
//        lastUpdateTime = now;

        //Choreographer.getInstance().postFrameCallback(gameLoopCallback);
        Choreographer.getInstance().postFrameCallback(this);
    }

    //안에 쓰는 함수가 doFrame 하나 뿐이라서 Choreographer 인터페이스를 상속하고 doFrame을 일반 함수로 오버라이드한 이후 this로 가르켜서도 할 수 있다.
    @Override
    public void doFrame(long nanos) {
        //Log.d(TAG, "Nanos = " + nanos + " frameTime = " + frameTime);
        if(previousNanos != 0)
        {
            frameTime = (nanos - previousNanos) / 1_000_000_000f; //_를 넣어서 읽기 쉽게 만든거 뿐 우리가 1,000쓰는거랑 같다고 보면된다
            update();
            invalidate();
        }
        previousNanos = nanos;
        if(isShown())
        {
            scheduleUpdate();
        }
    }

//   private final Choreographer.FrameCallback gameLoopCallback = new Choreographer.FrameCallback() {
//       @Override
//       public void doFrame(long nanos) {
//           update();
//           invalidate();
//           scheduleUpdate();
//       }
//   };


// 이건 postDelayed를 사용해서 1/60초 만들었을때 쓴 것
//    private final Runnable gameLoopRunnable = new Runnable() {
//        @Override
//        public void run() {
//            update();
//            invalidate();
//            scheduleUpdate();
//        }
//    };

    private void update() // 게임 루프에서 돌아가는 함수니까 move => update로 이름을 변경했다
    {
//        for (BouncingCircle bc : circles) {
//            bc.update();
//        }
//        for (Ball ball : balls) {
//            ball.update();
//        }
//        fighter.update();

        //Log.d(TAG, "com.example.ajm1222.and.samplegame.game.Ball Rect = " + ballRect);
        Scene scene = getTopScene();
        if(scene != null)
        {
            scene.update();
        }
    }

    private Paint borderPaint, gridPaint, fpsPaint;
    private void drawDebugBackground(@NonNull Canvas canvas) {
        if (borderPaint == null) {

            borderPaint = new Paint();
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(10f);
            borderPaint.setColor(Color.RED);

            gridPaint = new Paint();
            gridPaint.setStyle(Paint.Style.STROKE);
            gridPaint.setStrokeWidth(1f);
            gridPaint.setColor(Color.GRAY);
        }

        canvas.drawRect(Metrics.borderRect, borderPaint);
        for (float x = Metrics.GRID_UNIT; x < Metrics.width; x += Metrics.GRID_UNIT) {
            canvas.drawLine(x, 0, x, Metrics.height, gridPaint);
        }

            for (float y = Metrics.GRID_UNIT; y < Metrics.height; y += Metrics.GRID_UNIT) {
                canvas.drawLine(0, y, Metrics.width, y, gridPaint);
            }
    }
    private void drawDebugInfo(Canvas canvas) {
        if (fpsPaint == null) {
            fpsPaint = new Paint();
            fpsPaint.setColor(Color.BLUE);
            fpsPaint.setTextSize(100f);
        }

        int fps = (int) (1.0f / frameTime);
        canvas.drawText("FPS: " + fps, 100f, 200f, fpsPaint);
    }



}
