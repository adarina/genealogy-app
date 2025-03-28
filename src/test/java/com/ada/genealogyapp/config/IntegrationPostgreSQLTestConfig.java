//package com.ada.genealogyapp.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Testcontainers
//@ActiveProfiles("integration-test")
//public abstract class IntegrationPostgreSQLTestConfig {
//
//    @Autowired
//    public MockMvc mockMvc;
//
//    @Autowired
//    public ObjectMapper objectMapper;
//
//    @Container
//    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer<>("postgres:16");
//
//    static {
//        postgreSQLContainer.start();
//    }
//
//    @DynamicPropertySource
//    static void setProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//    }
//
//}
