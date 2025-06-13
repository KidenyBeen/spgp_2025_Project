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
import com.example.ajm1222.and.a2dg.framework.interfaces.ITouchable;
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

    private void remove(int layerIndex, IGameObject gobj) { //
        ArrayList<IGameObject> gameObjects = layers.get(layerIndex);
        gameObjects.remove(gobj);
        if (gobj instanceof IRecyclable) {
            collectRecyclable((IRecyclable) gobj);
            ((IRecyclable) gobj).onRecycle();
        }
    }

    //특정 레이어의 GameObject 리스트를 반환한다
    public <E extends Enum<E>> ArrayList<IGameObject> objectsAt(E layer) { //
            int layerIndex = layer.ordinal();
            return layers.get(layerIndex);
        }
    // 전체 레이어를 순회하며 포함된 GameObject 수를 합산한다.
        public int count() {

            int total = 0;
            for (ArrayList<IGameObject> layer : layers) {
                total += layer.size();
            }
            return total;
        }
    // 특정 레이어에 포함된 GameObject 수를 반환한다.
    public <E extends Enum<E>> int countAt(E layer) {
        int layerIndex = layer.ordinal();
        return layers.get(layerIndex).size();
    }

    // 디버깅용 문자열로 각 레이어별 오브젝트 개수를 출력한다. 예: [1, 3, 0, 5]
    public String getDebugCounts() {
        StringBuilder sb = new StringBuilder();
        for (ArrayList<IGameObject> gameObjects : layers) {
            if (sb.length() == 0) {
                sb.append('[');
            } else {
                sb.append(',');
            }
            sb.append(gameObjects.size());
        }
        sb.append(']');
        return sb.toString();
    }

    public String getDebugCounts2() { //리 사이클링에 쌓이는거 하는 중
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < layers.size(); i++) {
            if (i > 0) sb.append(',');
            ArrayList<IGameObject> layer = layers.get(i);
            if (layer.size() > 0 && layer.get(0) instanceof IRecyclable) {
                Class<?> clazz = layer.get(0).getClass();
                ArrayList<IRecyclable> bin = recycleBin.get(clazz);
                sb.append(bin != null ? bin.size() : 0);
            } else {
                sb.append(0);
            }
        }
        sb.append(']');
        return sb.toString();
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
            //count를 for 시작하기 전에 size를 조정하고 들어가는 이유는 삭제와 같은 행위가 생겼을때 for문을 시작하기전 해당 리스트의 길이를 동적으로 변경하기 위해서
            int count = gameObjects.size();
            //뒤에서 부터 읽는 이유는 삭제와 같은 상황이 발생했을때 인덱스 범위 오차 그리고 다음을 건너뛰고 동작하는 문제를 해결할 수 있는 가장 간단한 방법.
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
    public static void popAll() {
        GameView.view.popAllScenes();
    }

    //////////////////////////////////////////////////
    // Overridables
    public boolean isTransparent() {
        return false;
    }
    protected int getTouchLayerIndex() {
        return -1;
    }
    protected ITouchable capturingTouchable;
    public boolean onTouchEvent(MotionEvent event) {
        int touchLayer = getTouchLayerIndex();
        if (touchLayer < 0) return false;

        if (capturingTouchable != null) {
            boolean processed = capturingTouchable.onTouchEvent(event);
            if (!processed || event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "Capture End: " + capturingTouchable);
                capturingTouchable = null;
            }
            return processed;
        }

        ArrayList<IGameObject> gameObjects = layers.get(touchLayer);
        for (IGameObject gobj : gameObjects) {
            if (!(gobj instanceof ITouchable)) {
                continue;
            }
            boolean processed = ((ITouchable) gobj).onTouchEvent(event);
            if (processed) {
                capturingTouchable = (ITouchable) gobj;
                Log.d(TAG, "Capture Start: " + capturingTouchable);
                return true;
            }
        }
        return false;
    }
//    public boolean onTouchEvent(MotionEvent event) {
//        return false;
//    }


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
