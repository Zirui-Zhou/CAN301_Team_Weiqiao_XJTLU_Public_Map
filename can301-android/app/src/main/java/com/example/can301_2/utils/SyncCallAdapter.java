package com.example.can301_2.utils;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class SyncCallAdapter<T> implements CallAdapter<T, T> {

    private final Type returnType;

    public SyncCallAdapter(Type returnType) {
        this.returnType = returnType;
    }

    @NonNull
    @Override
    public Type responseType() {
        return returnType;
    }

    @Override
    public T adapt(Call<T> call) {
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class Factory extends CallAdapter.Factory {
        @Override
        public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
            return new SyncCallAdapter(returnType);
        }
    }
}