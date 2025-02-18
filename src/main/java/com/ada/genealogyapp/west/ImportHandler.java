package com.ada.genealogyapp.west;

import com.ada.genealogyapp.tree.model.Tree;

import java.util.Map;

public interface ImportHandler<T, R> {
    void handle(T input, Tree tree, Map<String, Object> context);
}
