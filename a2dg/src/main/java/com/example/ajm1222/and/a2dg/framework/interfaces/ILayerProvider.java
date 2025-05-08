package com.example.ajm1222.and.a2dg.framework.interfaces;

public interface ILayerProvider<E extends  Enum<E>> extends  IGameObject {
    public E getLayer();
}
