package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.tree.dto.params.DeleteTreeParams;
import com.ada.genealogyapp.tree.dto.params.SaveTreeParams;
import com.ada.genealogyapp.tree.dto.params.UpdateTreeParams;


public interface TreeService {

    void saveTree(SaveTreeParams params);
    void updateTree(UpdateTreeParams params);
    void deleteTree(DeleteTreeParams params);
    void ensureUserAndTreeExist(BaseParams params, Object response);
    void checkUserExistence(String userId, Object response);
}
