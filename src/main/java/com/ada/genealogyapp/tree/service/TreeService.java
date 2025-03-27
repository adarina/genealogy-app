package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.dto.params.DeleteTreeParams;
import com.ada.genealogyapp.tree.dto.params.SaveTreeParams;
import com.ada.genealogyapp.tree.dto.params.UpdateTreeParams;
import com.ada.genealogyapp.tree.model.Tree;

import java.util.Set;

public interface TreeService {

    void saveTree(SaveTreeParams params);
    void updateTree(UpdateTreeParams params);
    void deleteTree(DeleteTreeParams params);
    void ensureUserAndTreeExist(BaseParams params, Object response);
    void checkUserExistence(String userId, Object response);
    Tree findTreeById(String treeId);
    Set<Person> findPersonsById(String treeId);
    Set<Family> findFamiliesById(String treeId);
    Set<Event> findEventsById(String treeId);
    Set<Citation> findCitationsById(String treeId);
    Set<Source> findSourcesById(String treeId);
    Set<File> findFilesById(String treeId);
}
