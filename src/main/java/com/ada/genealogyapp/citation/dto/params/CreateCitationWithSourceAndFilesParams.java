package com.ada.genealogyapp.citation.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateCitationWithSourceAndFilesParams extends CreateCitationRequestParams {
    private String sourceId;
    private List<String> filesIds;
}
