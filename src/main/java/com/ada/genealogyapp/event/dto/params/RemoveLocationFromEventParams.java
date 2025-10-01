package com.ada.genealogyapp.event.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RemoveLocationFromEventParams extends BaseEventParams {
    private String locationId;
}
