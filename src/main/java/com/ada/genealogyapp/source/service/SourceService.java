package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.model.Source;

public interface SourceService {


    Source findSourceById(String sourceId);

    void ensureSourceExists(String sourceId);

    void saveSource(Source source);
}
