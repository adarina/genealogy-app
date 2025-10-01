package com.ada.genealogyapp.date.controller;

import com.ada.genealogyapp.date.type.KindType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/kind")
public class DateKindTypeController {

    @GetMapping
    public List<KindType> getKindTypes() {
        return Arrays.asList(KindType.values());
    }
}