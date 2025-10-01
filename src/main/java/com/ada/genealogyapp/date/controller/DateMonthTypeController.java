package com.ada.genealogyapp.date.controller;

import com.ada.genealogyapp.date.type.MonthType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/month")
public class DateMonthTypeController {

    @GetMapping
    public List<MonthType> getMonthTypes() {
        return Arrays.asList(MonthType.values());
    }
}