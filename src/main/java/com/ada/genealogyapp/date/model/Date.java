package com.ada.genealogyapp.date.model;

import com.ada.genealogyapp.date.type.QualityType;
import com.ada.genealogyapp.date.type.TypeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Date {

    private QualityType qualityType;

    private TypeType typeType;

    private PartialDate firstDate;

    private PartialDate SecondDate;

}
