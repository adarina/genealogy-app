package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.type.StatusType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class FamilyRequest {

    public StatusType statusType;

}
