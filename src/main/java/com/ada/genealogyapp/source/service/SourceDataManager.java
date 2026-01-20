package com.ada.genealogyapp.source.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.source.dto.params.*;
import com.ada.genealogyapp.source.repository.SourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SourceDataManager implements SourceService {

    private final SourceRepository sourceRepository;

    private final QueryResultProcessor processor;

    public void saveSource(SaveSourceParams params) {
        String result = sourceRepository.save(params.getUserId(), params.getTreeId(), params.getSourceId(), params.getSource().getName());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.SOURCE_ID, params.getSourceId()));
    }

    public void updateSource(UpdateSourceParams params) {
        String result = sourceRepository.update(params.getUserId(), params.getTreeId(), params.getSourceId(), params.getSource().getName());
        processor.process(result, Map.of(IdType.SOURCE_ID, params.getSourceId()));
    }

    public void deleteSource(DeleteSourceParams params) {
        String result = sourceRepository.delete(params.getUserId(), params.getTreeId(), params.getSourceId());
        processor.process(result, Map.of(IdType.SOURCE_ID, params.getSourceId()));
    }
}
