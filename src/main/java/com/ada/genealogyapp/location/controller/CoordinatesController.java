package com.ada.genealogyapp.location.controller;


import com.ada.genealogyapp.location.dto.CoordinatesRequest;
import com.ada.genealogyapp.location.service.CoordinatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/coordinates")
@AllArgsConstructor
public class CoordinatesController {

    private final CoordinatesService coordinatesService;

    @PostMapping
    public ResponseEntity<?> getCoordinates(@RequestBody CoordinatesRequest coordinatesRequest) {
        return ResponseEntity.ok(coordinatesService.getCoordinates(coordinatesRequest.getLocation(), coordinatesRequest.getState()));
    }
}
