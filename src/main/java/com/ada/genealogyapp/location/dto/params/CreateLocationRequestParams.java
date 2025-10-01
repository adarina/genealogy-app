package com.ada.genealogyapp.location.dto.params;

import com.ada.genealogyapp.location.dto.LocationRequest;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateLocationRequestParams extends BaseParams {

    private LocationRequest locationRequest;

}
