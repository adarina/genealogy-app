package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.tree.dto.params.DeleteTreeParams;
import com.ada.genealogyapp.tree.dto.params.SaveTreeParams;
import com.ada.genealogyapp.tree.dto.params.UpdateTreeParams;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TreeDataManager implements TreeService {

    private final TreeRepository treeRepository;

    private final QueryResultProcessor processor;

    public void saveTree(SaveTreeParams params) {
        String result = treeRepository.save(params.getUserId(), params.getTreeId(), params.getTree().getName());
        processor.process(result, Map.of(IdType.USER_ID, params.getUserId(), IdType.TREE_ID, params.getTreeId()));
    }

    public void updateTree(UpdateTreeParams params) {
        String result = treeRepository.update(params.getUserId(), params.getTreeId(), params.getTree().getName());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId()));
    }

    public void deleteTree(DeleteTreeParams params) {
        String result = treeRepository.delete(params.getUserId(), params.getTreeId());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId()));
    }

    public void ensureUserAndTreeExist(BaseParams params, Object response) {
        if (response == null || (response instanceof Page && ((Page<?>) response).isEmpty())) {
            String result = treeRepository.checkTreeAndUserExistence(params.getUserId(), params.getTreeId());
            processor.process(result, Map.of(IdType.USER_ID, params.getUserId(), IdType.TREE_ID, params.getTreeId()));
        }
    }

    public void checkUserExistence(String userId, Object response) {
        if (response instanceof List && ((List<?>) response).isEmpty()) {
            String result = treeRepository.checkUserExistence(userId);
            processor.process(result, Map.of(IdType.USER_ID, userId));
        }
    }
}
