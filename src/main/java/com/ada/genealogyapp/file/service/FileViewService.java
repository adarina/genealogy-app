package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.dto.FileFilterRequest;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.dto.params.GetFileParams;
import com.ada.genealogyapp.file.dto.params.GetFilesParams;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FileViewService {

    private final ObjectMapper objectMapper;

    private final FileRepository fileRepository;

    private final TreeService treeService;

    @Value("${file.upload.base-url}")
    private String baseUrl;


    public Page<FileResponse> getFiles(GetFilesParams params) throws JsonProcessingException {
        FileFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), FileFilterRequest.class);
        Page<FileResponse> page = fileRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getName(), filterRequest.getType(), baseUrl, params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public FileResponse getFile(GetFileParams params) {
        FileResponse fileResponse = fileRepository.find(params.getUserId(), params.getTreeId(), params.getFileId(), baseUrl);
        treeService.ensureUserAndTreeExist(params, fileResponse);
        return fileResponse;
    }
}
