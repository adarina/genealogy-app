package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.model.Source;

public interface SourceService {

    void ensureSourceExists(String sourceId);
    void saveSource(String treeId, Source source);
    void updateSource(String treeId, String sourceId, Source source);
}
