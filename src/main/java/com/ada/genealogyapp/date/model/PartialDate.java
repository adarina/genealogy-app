package com.ada.genealogyapp.date.model;


import com.ada.genealogyapp.date.type.MonthType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartialDate {

    private Integer year;

    private MonthType month;

    private Integer day;
}
