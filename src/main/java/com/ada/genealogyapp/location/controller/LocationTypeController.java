package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.location.type.LocationType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/location")
public class LocationTypeController {

    @GetMapping
    public List<LocationType> getLocationTypes() {
        return Arrays.asList(LocationType.values());
    }
}
