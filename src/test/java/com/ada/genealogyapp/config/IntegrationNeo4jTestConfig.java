//package com.ada.genealogyapp.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.neo4j.harness.Neo4j;
//import org.neo4j.harness.Neo4jBuilders;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public abstract class IntegrationNeo4jTestConfig {
//
//    @Autowired
//    public MockMvc mockMvc;
//
//    @Autowired
//    public ObjectMapper objectMapper;
//
//    private static Neo4j neo4j;
//
//    @BeforeAll
//    static void startNeo4j() {
//        neo4j = Neo4jBuilders
//                .newInProcessBuilder()
//                .withDisabledServer() // Use this to disable the server if it's not needed
//                .build();
//    }
//
//    @DynamicPropertySource
//    static void setNeo4jProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.neo4j.uri", neo4j::boltURI);
//        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
//        registry.add("spring.neo4j.authentication.password", () -> null);
//    }
//
//    @AfterAll
//    static void stopNeo4j() {
//        neo4j.close();
//    }
//
//
//
//}
