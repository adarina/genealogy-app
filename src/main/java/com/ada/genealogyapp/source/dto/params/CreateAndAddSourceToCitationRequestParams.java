package com.ada.genealogyapp.source.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateAndAddSourceToCitationRequestParams extends CreateSourceRequestParams {
    private String citationId;
}