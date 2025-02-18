package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;

    public Optional<Tree> findTreeByName(String name) {
        Optional<Tree> tree = treeRepository.findByName(name);
        if (tree.isPresent()) {
            log.info("Tree found: {}", tree.get());
        } else {
            log.error("No tree found with userId");
        }
        return tree;
    }

    public void saveTree(Tree tree) {
        Tree savedTree = treeRepository.save(tree);
        log.info("Tree saved successfully: {}", savedTree);
    }

    public void ensureTreeExists(String treeId) {
        if (!treeRepository.existsById(treeId)) {
            throw new NodeNotFoundException("Tree not found with ID: " + treeId);
        }
    }

    public Tree findTreeById(String treeId) {
        return treeRepository.findTreeById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId));
    }

    public Set<Person> findPersonsById(String treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId)).getPersons();
    }

    public Set<Family> findFamiliesById(String treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId)).getFamilies();
    }

    public Set<Event> findEventsById(String treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId)).getEvents();
    }

    public Set<Citation> findCitationsById(String treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId)).getCitations();
    }

    public Set<Source> findSourcesById(String treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId)).getSources();
    }

    public Set<File> findFilesById(String treeId) {
        return treeRepository.findById(treeId)
                .orElseThrow(() -> new NodeNotFoundException("Tree not found with ID: " + treeId)).getFiles();
    }
}