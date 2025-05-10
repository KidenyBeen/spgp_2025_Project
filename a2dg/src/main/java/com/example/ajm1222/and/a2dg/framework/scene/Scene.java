package com.example.ajm1222.and.a2dg.framework.scene;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.example.ajm1222.and.a2dg.framework.interfaces.IBoxCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.ILayerProvider;
import com.example.ajm1222.and.a2dg.framework.interfaces.IRecyclable;
import com.example.ajm1222.and.a2dg.framework.view.GameView;
import com.example.ajm1222.and.a2dg.framework.interfaces.IGameObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene {
    //Scene은 GameObject들의 집합으로 봐야해
    private static final String TAG = Scene.class.getSimpleName();
    protected ArrayList<ArrayList<IGameObject>> layers = new ArrayList<>(); //오브젝트들은 레이어로 관리된다.

    // Game Object Management
    protected void initLayers(int layerCount) { //레이어 안에 오브젝트들을 초기화 시켜주는거
        layers.clear();
        for (int i = 0; i < layerCount; i++) {
            layers.add(new ArrayList<>());
        }
    }
    public <E extends Enum<E>> void add(E layer, IGameObject gameObject) { //추가하는거
        int layerIndex = layer.ordinal();
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.add(gameObject);
        //Log.d(TAG, gameObjects.size() + " objects in " + this);
    }

    public void add(ILayerProvider<?> gameObject) { //본인의 레이어를 제공하는 오브젝트에 경우  index나 enum값을 넘기지 않아도 add가 되도록
        Enum<?> e = gameObject.getLayer();
        int layerIndex = e.ordinal();
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.add(gameObject);
    }

    public <E extends Enum<E>> void remove(E layer, IGameObject gobj) { //레이어와 오브젝트를 넣으면 삭제해주는거
        int layerIndex = layer.ordinal();
        remove(layerIndex, gobj);

        }

    public void remove(ILayerProvider<?> gameObject) {// 레이어를 제공하는 오브젝트를 준 경우 삭제하는거
        Enum<?> e = gameObject.getLayer();
        int layerIndex = e.ordinal();
        remove(layerIndex, gameObject);
    }

    private void remove(int layerIndex, IGameObject gobj) {
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.remove(gobj);
        if (gobj instanceof IRecyclable) {
            collectRecyclable((IRecyclable) gobj);
            ((IRecyclable) gobj).onRecycle();
        }
    }

    public <E extends Enum<E>> ArrayList<IGameObject> objectsAt(E layer) { //
            int layerIndex = layer.ordinal();
            return layers.get(layerIndex);
        }
        public int count() {

            int total = 0;
            for (ArrayList<IGameObject> layer : layers) {
                total += layer.size();
            }
            return total;
        }
    public <E extends Enum<E>> int countAt(E layer) {
        int layerIndex = layer.ordinal();
        return layers.get(layerIndex).size();
    }

    //////////////////////////////////////////////////
    //  Object Recycling (재활용 관련)
// ===============================

    // GameObject를 재활용 목록에 추가한다.
    // 클래스별로 관리하며, 추후 동일 타입의 오브젝트를 다시 꺼내 쓸 수 있도록 저장한다.
    protected HashMap<Class, ArrayList<IRecyclable>> recycleBin = new HashMap<>();
    public void collectRecyclable(IRecyclable object) {
        Class clazz = object.getClass();
        ArrayList<IRecyclable> bin = recycleBin.get(clazz);
        if (bin == null) {
            bin = new ArrayList<>();
            recycleBin.put(clazz, bin);
        }

        bin.add(object);

    }

    // 지정한 클래스 타입의 재활용 가능한 객체를 꺼내온다.
    // 없으면 newInstance()로 새로 생성하여 반환한다.
    public <T extends IRecyclable> T getRecyclable(Class<T> clazz) {
        ArrayList<IRecyclable> bin = recycleBin.get(clazz);
        if (bin == null || bin.isEmpty()) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return clazz.cast(bin.remove(0));
    }

    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    // Game Loop Functions
    public void update() {
        for (ArrayList<IGameObject> gameObjects : layers) {
            int count = gameObjects.size();
            for (int i = count - 1; i >= 0; i--) {
                IGameObject gobj = gameObjects.get(i);
                gobj.update();
            }
        }
    }
    protected static Paint bboxPaint;
    public void draw(Canvas canvas)
        {
            for (ArrayList<IGameObject> gameObjects : layers) {
                for (IGameObject gobj : gameObjects) {
                    gobj.draw(canvas);
                }
            }

            if (GameView.drawsDebugStuffs) { //빌드가 아닐때 디버그 일때
                if (bboxPaint == null) {
                    bboxPaint = new Paint();
                    bboxPaint.setStyle(Paint.Style.STROKE);
                    bboxPaint.setColor(Color.RED);
                }
                for (ArrayList<IGameObject> gameObjects : layers) { //충돌체 박스를 갖고있는 애들에 박스를 그려라
                    for (IGameObject gobj : gameObjects) {
                        if (gobj instanceof IBoxCollidable) {
                            RectF rect = ((IBoxCollidable) gobj).getCollisionRect();
                            canvas.drawRect(rect, bboxPaint);
                        }
                    }
                }
            }
        }

    //////////////////////////////////////////////////
    // Scene Stack Functions

    public void push() {
        GameView.view.pushScene(this);
    }
    public static Scene pop() {
        return GameView.view.popScene();
    }
    public static Scene top() {
        return GameView.view.getTopScene();
    }

    //////////////////////////////////////////////////
    // Overridables
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }


    public void onEnter() {
        Log.v(TAG, "onEnter: " + getClass().getSimpleName());
    }
    public void onPause() {
        Log.v(TAG, "onPause: " + getClass().getSimpleName());
    }
    public void onResume() {
        Log.v(TAG, "onResume: " + getClass().getSimpleName());
    }
    public void onExit() {
        Log.v(TAG, "onExit: " + getClass().getSimpleName());
    }


    public boolean onBackPressed() {
        return false;
    }
}
