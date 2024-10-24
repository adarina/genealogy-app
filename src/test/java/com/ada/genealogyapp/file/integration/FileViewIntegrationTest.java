package com.ada.genealogyapp.file.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileViewIntegrationTest extends IntegrationTestConfig {

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
    void shouldGetAllFiles() throws Exception {
        Tree tree = new Tree();
        treeRepository.save(tree);

        File file = new File();
        file.setPath("/sth");
        file.setFileTree(tree);

        fileRepository.save(file);

        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/files", tree.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
