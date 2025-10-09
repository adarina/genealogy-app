package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.source.dto.params.*;

import java.util.List;
import java.util.Map;

public interface SourceService {

    void saveSource(SaveSourceParams params);

    void deleteSource(DeleteSourceParams params);

    void updateSource(UpdateSourceParams params);

    void saveSourcesBatch(String userId, String treeId, List<Map<String, Object>> sourcesData);

}
