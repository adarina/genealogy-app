package com.ada.genealogyapp.person.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public class BasePersonParams {
    private String userId;
    private String treeId;
    private String personId;
}