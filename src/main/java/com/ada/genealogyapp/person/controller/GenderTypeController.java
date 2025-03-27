package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.type.GenderType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/gender")
public class GenderTypeController {

    @GetMapping
    public List<GenderType> getGenderTypes() {
        return Arrays.asList(GenderType.values());
    }
}