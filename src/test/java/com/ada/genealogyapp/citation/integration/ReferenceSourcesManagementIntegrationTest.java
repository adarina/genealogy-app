package com.ada.genealogyapp.citation.integration;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.repository.SourceRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReferenceSourcesManagementIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    CitationRepository citationRepository;

    @Autowired
    SourceRepository sourceRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        sourceRepository.deleteAll();
        citationRepository.deleteAll();

    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        sourceRepository.deleteAll();
        citationRepository.deleteAll();

    }

    @Test
    void shouldAddExistingSourceToCitationSuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Citation citation = new Citation();
        citationRepository.save(citation);

        Source source = new Source();
        sourceRepository.save(source);

        UUIDRequest uuidRequest = new UUIDRequest();
        uuidRequest.setId(source.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/citations/{citationId}", tree.getId(), citation.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/citations/{citationId}/sources/addExistingSource", tree.getId(), citation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uuidRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/commit", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Citation savedCitation = citationRepository.findById(citation.getId()).orElseThrow();
        assertNotNull(savedCitation.getSources().iterator().next().getId());
        assertEquals(source.getId(), savedCitation.getSources().iterator().next().getId());
    }
}
