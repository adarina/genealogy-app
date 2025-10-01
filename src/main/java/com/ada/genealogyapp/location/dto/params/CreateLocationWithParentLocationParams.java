package com.ada.genealogyapp.location.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateLocationWithParentLocationParams extends CreateLocationRequestParams {

    private String parentLocationId;

}
