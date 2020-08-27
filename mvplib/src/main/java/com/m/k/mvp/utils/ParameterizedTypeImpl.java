package com.m.k.mvp.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeImpl implements ParameterizedType {
    private Type rawTyep;

    private Type [] typeArgs;

    public ParameterizedTypeImpl(Type rawType, Type[] typeArgs) {
        this.rawTyep = rawType;
        this.typeArgs = typeArgs;
    }

    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return typeArgs;
    }

    @NonNull
    @Override
    public Type getRawType() {
        return rawTyep;
    }

    @Nullable
    @Override
    public Type getOwnerType() {
        return null;
    }
}
