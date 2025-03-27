package com.ada.genealogyapp.citation.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RemoveSourceFromCitationParams extends BaseCitationParams {
    String sourceId;
}
