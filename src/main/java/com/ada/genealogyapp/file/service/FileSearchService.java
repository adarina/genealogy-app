package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileSearchService {

    private final FileRepository fileRepository;

    public FileSearchService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

}
