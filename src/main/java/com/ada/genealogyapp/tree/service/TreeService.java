package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;

import java.util.Optional;
import java.util.Set;

public interface TreeService {
    Optional<Tree> findTreeByName(String name);
    void saveTree(Tree tree);
    void ensureTreeExists(String treeId);
    Tree findTreeById(String treeId);
    Set<Person> findPersonsById(String treeId);
    Set<Family> findFamiliesById(String treeId);
    Set<Event> findEventsById(String treeId);
    Set<Citation> findCitationsById(String treeId);
    Set<Source> findSourcesById(String treeId);
    Set<File> findFilesById(String treeId);
}
