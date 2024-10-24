package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class FileViewService {

    private final FileSearchService fileSearchService;

    public FileViewService(FileSearchService fileSearchService) {
        this.fileSearchService = fileSearchService;
    }


    public List<File> getFiles(UUID treeId) {
        return fileSearchService.getFilesByTreeIdOrThrowNodeNotFoundException(treeId);
    }
}
