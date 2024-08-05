package com.example.resy.transformers;

public interface Transformer<D, T> {

    public T transformToB(D object);
    public D transformToA(T object);
}
