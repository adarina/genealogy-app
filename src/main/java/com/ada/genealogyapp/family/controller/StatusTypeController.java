package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.type.StatusType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/status")
public class StatusTypeController {

    @GetMapping
    public List<StatusType> getStatusTypes() {
        return Arrays.asList(StatusType.values());
    }
}