package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.citation.dto.params.GetCitationFilesParams;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CitationFilesViewService {

    private final CitationRepository citationRepository;

    private final TreeService treeService;

    @Value("${file.upload.base-url}")
    private String baseUrl;

    public Page<FileResponse> getCitationFiles(GetCitationFilesParams params) {
        Page<FileResponse> page = citationRepository.findFiles(params.getUserId(), params.getTreeId(), params.getCitationId(), baseUrl, params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }
}
