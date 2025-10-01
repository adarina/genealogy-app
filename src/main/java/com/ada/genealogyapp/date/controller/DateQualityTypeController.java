package com.ada.genealogyapp.date.controller;

import com.ada.genealogyapp.date.type.QualityType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/quality")
public class DateQualityTypeController {

    @GetMapping
    public List<QualityType> getQualityTypes() {
        return Arrays.asList(QualityType.values());
    }
}