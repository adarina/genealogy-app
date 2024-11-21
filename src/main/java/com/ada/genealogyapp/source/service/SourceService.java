package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.model.Source;

import java.util.UUID;

public interface SourceService {


   Source findSourceById(UUID sourceId);

    void ensureSourceExists(UUID sourceId);

    void saveSource(Source source);
}
