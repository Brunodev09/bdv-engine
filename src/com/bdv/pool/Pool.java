package com.bdv.pool;

import java.util.ArrayList;
import java.util.List;

public class Pool <T> {
    private List<T> data;

    public Pool() {
        data = new ArrayList<>();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public void clear() {
        data.clear();
    }

    public void add(T object) {
        data.add(object);
    }

    public void set(int index, T object) {
        data.set(index, object);
    }

    public T get(int index) {
        return data.get(index);
    }

    public int getSize() {
        return data.size();
    }
}
