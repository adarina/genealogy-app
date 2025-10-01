package com.ada.genealogyapp.graphuser.service;


import com.ada.genealogyapp.graphuser.dto.GraphUserResponse;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class GraphUserViewService {

    private final GraphUserRepository graphUserRepository;

    public GraphUserResponse getGraphUser(String userId) {
        return graphUserRepository.find(userId);
    }
}
