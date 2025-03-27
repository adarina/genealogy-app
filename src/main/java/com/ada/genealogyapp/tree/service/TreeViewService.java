package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class TreeViewService {

    private final TreeRepository treeRepository;

    private final TreeService treeService;

    public List<TreeResponse> getTrees(String userId) {
        List<TreeResponse> trees = treeRepository.find(userId);
        treeService.checkUserExistence(userId, trees);
        return trees;
    }
}