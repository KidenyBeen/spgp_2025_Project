package com.example.ajm1222.and.a2dg.framework.util;

import android.graphics.RectF;

import com.example.ajm1222.and.a2dg.framework.interfaces.IBoxCollidable;
import com.example.ajm1222.and.a2dg.framework.interfaces.IDotCollidable;

public class CollisionHelper {

    public static boolean collides(IBoxCollidable obj1, IBoxCollidable obj2) {
        RectF r1 = obj1.getCollisionRect();
        RectF r2 = obj2.getCollisionRect();
        return collides(r1, r2);
    }
    public static boolean collides(RectF r1, RectF r2) {
        if (r1.left > r2.right) return false;
        if (r1.top > r2.bottom) return false;
        if (r1.right < r2.left) return false;
        if (r1.bottom < r2.top) return false;
        return true;
    }

    public static boolean collides(IDotCollidable dot, IBoxCollidable box) //점과 사각형의 충돌 처리 추가
    {
        float[] pt = dot.getCollisionPoint();
        RectF rect = box.getCollisionRect();
        return rect.contains(pt[0], pt[1]);
    }
}
