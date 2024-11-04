package com.ada.genealogyapp.tree.service;



import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class TreeViewService {

    private final TreeSearchService treeSearchService;

    public TreeViewService(TreeSearchService treeSearchService) {
        this.treeSearchService = treeSearchService;
    }

    public List<TreeResponse> getTrees() {
        List<Tree> trees = treeSearchService.getTreesOrThrowNodeNotFoundException();
        List<TreeResponse> treeResponses = new ArrayList<>();

        for (Tree tree : trees) {
            TreeResponse response = new TreeResponse();
            response.setId(tree.getId());
            response.setName(tree.getName());

            treeResponses.add(response);
        }
        return treeResponses;
    }
}
