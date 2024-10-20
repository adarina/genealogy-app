package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.tree.service.TreeTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}")
public class TreeTransactionController {

    private final TreeTransactionService treeTransactionService;

    public TreeTransactionController(TreeTransactionService treeTransactionService) {
        this.treeTransactionService = treeTransactionService;
    }

    @PostMapping("/commit")
    public ResponseEntity<Void> commit() {
        treeTransactionService.commitChanges();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancel() {
        treeTransactionService.rollbackChanges();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
