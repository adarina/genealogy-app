package com.ada.genealogyapp.date.model;

import com.ada.genealogyapp.date.type.QualityType;
import com.ada.genealogyapp.date.type.KindType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Date {

    private QualityType qualityType;

    private KindType kindType;

    private PartialDate firstDate;

    private PartialDate secondDate;

}
