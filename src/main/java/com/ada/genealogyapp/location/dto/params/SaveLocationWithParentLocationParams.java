package com.ada.genealogyapp.location.dto.params;

import com.ada.genealogyapp.location.model.Location;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;



@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaveLocationWithParentLocationParams extends BaseLocationParams {

    private Location location;

    private String parentLocationId;
}
