package com.ada.genealogyapp.file.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class FileStorageIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    FileRepository fileRepository;

    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        fileRepository.deleteAll();

    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        fileRepository.deleteAll();
    }

    @Test
    public void shouldStoreFileSuccessfully() throws Exception {
        Tree tree = new Tree();
        treeRepository.save(tree);

        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpeg", "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart("/api/v1/genealogy/trees/{treeId}/files", tree.getId())
                        .file(mockFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isCreated());

        Path destinationFile = Paths.get("upload-dir/test.jpeg");
        assertTrue(Files.exists(destinationFile));
    }
}