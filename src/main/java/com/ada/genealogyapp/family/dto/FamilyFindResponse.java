package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.model.Family;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FamilyFindResponse {
    private String message;
    private Family family;
}
