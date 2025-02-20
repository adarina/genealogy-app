package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.model.Source;

public interface SourceService {

    void ensureSourceExists(String sourceId);
    void saveSource(String userId, String treeId, Source source);
    void updateSource(String userId, String treeId, String sourceId, Source source);
}
