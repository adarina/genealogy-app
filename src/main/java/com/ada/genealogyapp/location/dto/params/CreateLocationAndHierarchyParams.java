package com.ada.genealogyapp.location.dto.params;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateLocationAndHierarchyParams extends BaseParams {

    private String hierarchy;

    private AddressParams address;

    private Double latitude;

    private Double longitude;

}
