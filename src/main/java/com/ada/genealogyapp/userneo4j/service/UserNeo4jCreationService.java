//package com.ada.genealogyapp.userneo4j.service;
//
//import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
//import com.ada.genealogyapp.userneo4j.model.UserNeo4j;
//import com.ada.genealogyapp.userneo4j.repository.UserNeo4jRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//
//@Slf4j
//@Service
//public class UserNeo4jCreationService {
//
//
//    private final UserNeo4jRepository userNeo4jRepository;
//
//    public UserNeo4jCreationService(UserNeo4jRepository userNeo4jRepository) {
//        this.userNeo4jRepository = userNeo4jRepository;
//    }
//
//    @TransactionalInNeo4j
//    public UserNeo4j createUserNeo4j(Long id) {
//
//        UserNeo4j userNeo4j = UserNeo4j.builder()
//                .id(id).build();
//
//        userNeo4jRepository.save(userNeo4j);
//        return userNeo4j;
//    }
//}
