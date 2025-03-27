//package com.ada.genealogyapp.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.Neo4jContainer;
//import org.testcontainers.containers.Neo4jLabsPlugin;
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
//    private static Neo4jContainer<?> neo4jContainer;
//
//    @BeforeAll
//    static void startNeo4jContainer() {
//        neo4jContainer = new Neo4jContainer<>("neo4j:4.2.1")
//                .withLabsPlugins(Neo4jLabsPlugin.APOC)
//                .withoutAuthentication();
//        neo4jContainer.start();
//    }
//
//    @DynamicPropertySource
//    static void setNeo4jProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.neo4j.uri", neo4jContainer::getBoltUrl);
//        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
//        registry.add("spring.neo4j.authentication.password", () -> null);
//    }
//    @AfterAll
//    static void stopNeo4jContainer() {
//        if (neo4jContainer != null) {
//            neo4jContainer.stop();
//        }
//    }
//}
