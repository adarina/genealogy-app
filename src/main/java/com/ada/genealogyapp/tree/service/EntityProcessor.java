package com.ada.genealogyapp.tree.service;


@FunctionalInterface
public interface EntityProcessor<T, P> {
    void process(T entity, P params);
}
