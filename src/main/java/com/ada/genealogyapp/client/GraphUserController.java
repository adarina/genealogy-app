package com.ada.genealogyapp.client;


import com.ada.genealogyapp.graphuser.service.GraphUserCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/graph-users")
@RequiredArgsConstructor
public class GraphUserController {

    private final GraphUserCreationService graphUserCreationService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> createGraphUser(@PathVariable String userId) {
        graphUserCreationService.createGraphUser(userId);
        return ResponseEntity.ok().build();
    }
}
