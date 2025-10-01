package com.ada.genealogyapp.location.dto.params;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
public class BaseLocationParams {

    private String userId;

    private String treeId;

    private String locationId;
}