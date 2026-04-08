package com.patrykb.PatFin.pattern.iterator;

public interface PatFinIterator<T> {
    boolean hasNext();
    T next();
}