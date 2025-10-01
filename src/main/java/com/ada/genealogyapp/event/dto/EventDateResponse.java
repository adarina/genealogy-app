package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.date.model.PartialDate;
import com.ada.genealogyapp.date.type.KindType;
import com.ada.genealogyapp.date.type.QualityType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventDateResponse {

    private QualityType qualityType;

    private KindType kindType;

    private PartialDate firstDate;

    private PartialDate secondDate;
}
